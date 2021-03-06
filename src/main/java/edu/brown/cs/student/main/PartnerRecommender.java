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
import org.checkerframework.checker.units.qual.A;

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

    public PartnerRecommender() throws SQLException, ClassNotFoundException {}

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
            classmate.setSQLData(rs);
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
        BloomFilter<String> filter = new BloomFilter<>(0.01,classmatesMap.size());
        //================================================================================

        AndSimilarityComparator comparator = new AndSimilarityComparator(filter);
        BloomFilterRecommender<Classmate> bloomFilterRecommender = new BloomFilterRecommender<>(
            classmatesMap, 0.1);
        bloomFilterRecommender.setBloomFilterComparator(comparator);

        List<Classmate> bloomFilterRecs = bloomFilterRecommender.getTopKRecommendations(
            target, classmatesMap.size() - 1);

        // create a hashmap of IDs to Rank for the Bloom Filter output
        HashMap<String, Integer> bloomResultsMap = new HashMap<>();
        for (int i = 0; i < bloomFilterRecs.size(); i++) {
            bloomResultsMap.put(bloomFilterRecs.get(i).getId(), i);
        }

        // run kd tree on students
        KdTreeSearch<String> search = new KdTreeSearch<>();
        List<Coordinate<String>> kdFilterRecs = search.getNearestNeighborsResult(
            classmatesMap.size() - 1, target, kdTree.getRoot(), true);

        // create a hashmap of IDs to Rank for the KD Tree
        HashMap<String, Integer> kdResultsMap = new HashMap<>();
        for (int i = 0; i < kdFilterRecs.size(); i++) {
            kdResultsMap.put(kdFilterRecs.get(i).getId(), i);
        }

        // since we have IDs from 1 to 61, we can
        ArrayList<RankingsHelper> ranks = new ArrayList<>();
        for (int i = 1; i < 62; i++) {
            if (i == Integer.parseInt(studentID)) {
                continue;
            }
            int bloomRank = bloomResultsMap.get(Integer.toString(i));
            int kdRank = kdResultsMap.get(Integer.toString(i));
            double averageRank = (bloomRank + kdRank) / 2;
            ranks.add(new RankingsHelper(Integer.toString(i), averageRank));
        }

        // sort ranks by the rank field in RankingsHelper
        ranks.sort(Comparator.comparing(RankingsHelper::getRank));

//        for (RankingsHelper rankHelper : ranks) {
//            System.out.println(rankHelper.toString());
//        }
//
//        // print out top k results by average rank
//        System.out.println("Found top " + numRecs + " matches. Here are the IDs:");
//        for (int i = 0; i < numRecs; i++) {
//            RankingsHelper currRankObj = ranks.get(i);
//            System.out.println(currRankObj.getId());
//        }
        List<String> rankedIDs = new ArrayList<>();
        for (int i=0;i<numRecs;i++) {
            rankedIDs.add(ranks.get(i).getId());
        }
        return rankedIDs;
    }

    /**
     * Helper method for generateGroups - takes in an ID and recursively finds the rest of a group for it
     * @param nextID - ID to find rest of group for
     * @param leftToFind - how many group members this group still needs to find
     * @param currGroup - List of group members found for this ID so far
     * @param unvisited - List of possible group members - classmates that haven't been assigned yet
     * @param matchMap - hashmap of classmate ID to list of all other classmates in ranked order
     * @return List of IDs corresponding to one group
     */
    private List<String> createGroup(String nextID, int leftToFind, List<String> currGroup, List<String> unvisited, Map<String, List<String>> matchMap) {
        // base case -> group is full
        if (leftToFind == 0) {
            return currGroup;
        }

        // loop through matches,
        for (String potentialPartner : matchMap.get(nextID)) {
            //check if potential partner has been matched up already
            if (unvisited.contains(potentialPartner)) {
                // remove from list of available classmates
                unvisited.remove(potentialPartner);

                //clone current group (because I'm afraid of recursion)
                List<String> updatedGroup = new ArrayList<>(currGroup);
                //add new partner
                updatedGroup.add(potentialPartner);

                //make recursive call to find and add rest of group
                return createGroup(potentialPartner, leftToFind - 1, updatedGroup, unvisited, matchMap);
            }
        }
        //if somehow there is no one left to partner with because everyone has been visited already
        throw new RuntimeException("Error creating group: all matches have been taken already");
    }

    /**
     * Generates groups from list of Classmates to maximize compatibility
     * @param teamSize - How many members should be on each team (extras will be added to existing teams)
     * @return List of Groups, each in the form of a List of Classmate IDs which form a group
     */
    public List<List<String>> generateGroups(int teamSize) {
        Map<String,List<String>> classmateToBestMatches = new HashMap<>();
        for (String classmateID: classmatesMap.keySet()) {
            if (!classmatesMap.containsKey(classmateID)) {
                classmateToBestMatches.put(classmateID, getRecsFromStudentID(classmatesMap.size()-1, classmateID));
            }
        }
        // check unvisted instead

        List<String> unvisited = new ArrayList<>(classmatesMap.keySet());

        // split up the groups

        // find number of teams
        int numTeams = classmatesMap.size()/teamSize;
        Random rand = new Random();

        List<List<String>> listOfGroups= new ArrayList<>();

        // create team for each numTeams and add to listOfGroups
        for (int i = 0; i<numTeams; i++) {
            int teamSizeUpdated = teamSize;
            //check if group should have an extra person or not
            if (i>=classmatesMap.size()%teamSize) {
                teamSizeUpdated--;
            }
            String randomID = unvisited.get(rand.nextInt(unvisited.size()));
            unvisited.remove(randomID);
            // call to helper to create group for randomly selected classmate
            listOfGroups.add(createGroup(randomID, teamSizeUpdated, new ArrayList<>(List.of(randomID)), unvisited, classmateToBestMatches));
        }

        return listOfGroups;

    }
}
