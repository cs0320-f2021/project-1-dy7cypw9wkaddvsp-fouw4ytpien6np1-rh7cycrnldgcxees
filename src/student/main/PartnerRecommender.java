package edu.brown.cs.student.main;

import edu.brown.cs.student.api.client.ApiClient;
import edu.brown.cs.student.api.main.ApiAggregator;
import edu.brown.cs.student.bloom.bloomfilter.AndSimilarityComparator;
import edu.brown.cs.student.bloom.bloomfilter.BloomFilter;
import edu.brown.cs.student.bloom.bloomfilter.BloomFilterRecommender;
import edu.brown.cs.student.kdtree.coordinates.Coordinate;
import edu.brown.cs.student.kdtree.coordinates.KdTree;
import edu.brown.cs.student.kdtree.searchAlgorithms.KdTreeSearch;
import edu.brown.cs.student.orm.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PartnerRecommender {
    private HashMap<String, Classmate> classmatesMap = new HashMap<>();

    //make list of classmates for kd tree loading:
    private List<Coordinate<String>> classmatesList = new ArrayList<>();

    private ApiAggregator api = new ApiAggregator();
    private Database db = new Database();
    private KdTree<String> kdTree;

    public PartnerRecommender(){}

    public int setData () throws Exception {
        classmatesMap = new HashMap<>();
        classmatesList = new ArrayList<>();
        api = new ApiAggregator();
        db = new Database();

        // counts total number of classmates:
        int numClassmates = 0;

        // call get getData on API to return list of classmates,
        // put each classmate from list into hashmap
        // have separate method to load in sql data for each classmate in hashmap
        List<Map<String,String>> classmatesMaps = api.getAPIData("classmate");
        List<Classmate> classmateObjects = new ArrayList<>();
        for (Map<String,String> map: classmatesMaps) {
            try {
                classmateObjects.add(new Classmate(map));
            }
            catch (Exception e) {
                System.out.println("issue with hashmap parseing");
                throw e;
            }

        }

        // change path to integration.sqlite3:
        this.db.changePath("data/integration/integration.sqlite3");
        // run this raw query once to join all 4 tables together:

        // this part is commented out because the tables are already created.
        // need to try to fix this somehow so it checks if the tables already exist or not?
        /*
        // join pos and neg traits:
        this.db.rawQuery("CREATE TABLE pos_agg AS SELECT id, group_concat(trait) " +
            "AS positive FROM positive GROUP BY id;");
        this.db.rawQuery("CREATE TABLE neg_agg AS SELECT id, group_concat(trait) " +
            "AS negative FROM negative GROUP BY id;");
        this.db.rawQuery("CREATE TABLE pos_and_neg_traits " +
            "AS SELECT pos_agg.id, positive, negative FROM pos_agg " +
            "JOIN neg_agg ON pos_agg.id=neg_agg.id;");

        this.db.rawQuery("CREATE TABLE int_agg AS SELECT id, group_concat(interest) " +
            "AS interests FROM interests GROUP BY id;");
        // join above table with interests:
        this.db.rawQuery("CREATE TABLE interests_traits " +
            "AS SELECT int_agg.id, positive, negative, interests FROM int_agg JOIN " +
            "pos_and_neg_traits ON int_agg.id=pos_and_neg_traits.id;");
        // finally, join that result with skills:
        this.db.rawQuery("CREATE TABLE aggregate " +
            "AS SELECT skills.id, positive, negative, interests, name, commenting, testing, OOP, " +
            "algorithms, teamwork, frontend FROM interests_traits " +
            "JOIN skills ON interests_traits.id=skills.id;");
        // columns of aggregate are id (integer), positive (string), negative (string), interests (string),
        // name (String), commenting (integer), testing (integer), OOP (integer),
        // algorithms (integer), teamwork (integer), frontend (integer)


         */



        // match classmate to the corresponding SQL entry in aggregate by id:
        for (Classmate classmate : classmateObjects) {
            ResultSet rs = db.returnRSFromQuery("id",
                String.valueOf(classmate.getId()), "aggregate");
            classmate.populateFields(rs);
            // for each classmate, add them to a HM of IDs -> Classmate
            classmatesMap.put(classmate.getId(), classmate);

            // add classmate to list of classmates which will go in the KdTree
            classmatesList.add(classmate);

            // increment classmates count by 1:
            numClassmates++;
        }

        kdTree = new KdTree<>(7, classmatesList);

        // get lists of unique tags for each categorical attribute
        // arrange these into bitmaps for each classmate
        return numClassmates;
    }

    // Write methods to load data from hashmap into kdtree and bloom filter(s)
    // PASS IN: info about target classmate in order to conduct search (student id)

    /**
     * Given a target student, returns List of student IDs in preference order based on partner compatibility
     * @param numRecs - number of students to return
     * @param studentID - student ID of target student
     * @return - list of Strings representing student IDs in order of partner compatibility
     */
    public List<String> getRecsFromStudentID(int numRecs, String studentID) {
        Classmate target = classmatesMap.get(studentID);
        // run bloom filter on list of all students
        //================================================================================
        // TO-DO: fix this line vvv I just put random values in to see if everything else was working by the filter
        // but this filter recommends the same top partners for everyone so something is broken
        BloomFilter<String> filter = new BloomFilter<>(0.1,180);
        //================================================================================

        AndSimilarityComparator comparator = new AndSimilarityComparator(filter);
        BloomFilterRecommender<Classmate> bloomFilterRecommender = new BloomFilterRecommender<>(classmatesMap, 0.1);
        bloomFilterRecommender.setBloomFilterComparator(comparator);

        List<Classmate> bloomFilterRecs = bloomFilterRecommender.getTopKRecommendations(target, classmatesMap.size()-1);
        System.out.println(bloomFilterRecs.toString());

        //run kd tree on students
        KdTreeSearch<String> search = new KdTreeSearch<>();
        List<Coordinate<String>> kdFilterRecs = search.getNearestNeighborsResult(classmatesMap.size()-1, target, kdTree.getRoot(), true);
        System.out.println(kdFilterRecs.toString());

        // combine these two lists by finding the average of the ranking from each
        // filter for each item and sorting by the average ranking

        return new ArrayList<>();
    }

    //use a treemap? instead of a hashmap? (should allow you to sort)
}
