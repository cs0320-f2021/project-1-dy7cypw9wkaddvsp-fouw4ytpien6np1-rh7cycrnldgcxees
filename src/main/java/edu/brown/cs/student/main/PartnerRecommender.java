package edu.brown.cs.student.main;

import edu.brown.cs.student.api.main.ApiAggregator;
import edu.brown.cs.student.bloom.bloomfilter.AndSimilarityComparator;
import edu.brown.cs.student.bloom.bloomfilter.BloomFilter;
import edu.brown.cs.student.bloom.bloomfilter.BloomFilterRecommender;
import edu.brown.cs.student.kdtree.coordinates.Coordinate;
import edu.brown.cs.student.kdtree.coordinates.KdTree;
import edu.brown.cs.student.kdtree.searchAlgorithms.KdTreeSearch;
import edu.brown.cs.student.orm.Database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PartnerRecommender {
    private HashMap<String, Classmate> classmatesMap = new HashMap<>();

    //make list of classmates for kd tree loading:
    private List<Coordinate<String>> classmatesList = new ArrayList<>();

    private ApiAggregator api = new ApiAggregator();
    private Database db = new Database();
    private KdTree<String> kdTree;

    public PartnerRecommender() {}

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
        List<Map<String, String>> classmatesMaps = api.getAPIData("classmate");
        List<Classmate> classmateObjects = new ArrayList<>();
        for (Map<String, String> map: classmatesMaps) {
            try {
                classmateObjects.add(new Classmate(map));
            }
            catch (Exception e) {
                System.out.println("issue with hashmap parsing");
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
        // columns of aggregate are id (integer), positive (string), negative (string),
        interests (string), name (String), commenting (integer), testing (integer), OOP (integer),
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

    /**
     * Given a target student, returns List of student IDs in preference order based on partner compatibility
     * @param numRecs - number of students to return
     * @param studentID - student ID of target student
     * @return - list of Strings representing student IDs in order of partner compatibility
     */
    public List<String> getRecsFromStudentID(int numRecs, String studentID) {

        if (!classmatesMap.containsKey(studentID) || numRecs > classmatesMap.size() ||
        numRecs < 0) {
            throw new RuntimeException("Invalid StudentID");
        }
        Classmate target = classmatesMap.get(studentID);
        // run bloom filter on list of all students
        //================================================================================
        // TO-DO: fix this line vvv I just put random values in to see if everything else
        // was working by the filter
        // but this filter recommends the same top partners for everyone so something is broken
        BloomFilter<String> filter = new BloomFilter<>(0.01,
            classmatesMap.size());
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

        // since we have IDs from 1 to 61, we can loop through these to get the average rank
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

        List<String> rankedIDs = new ArrayList<>();
        for (int i=0;i<numRecs;i++) {
            rankedIDs.add(ranks.get(i).getId());
        }
        return rankedIDs;
    }

    /**
     * Helper for Generate Groups
     *
     * @param nextID - next id to find
     * @param leftToFind - list of ids left to find
     * @param currGroup - current group algorithm is on
     * @param unvisited - remaining IDs to process
     * @param matchMap - map of matches of compatibilities
     * @return List<String> representing the group
     */
    private List<String> createGroup(String nextID, int leftToFind, List<String> currGroup,
                                     List<String> unvisited, Map<String, List<String>> matchMap) {
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
                List<String> updatedGroup = new ArrayList<>();
                updatedGroup.addAll(currGroup);
                //add new partner
                updatedGroup.add(potentialPartner);

                //make recursive call to find and add rest of group
                return createGroup(potentialPartner, leftToFind - 1, updatedGroup,
                    unvisited, matchMap);
            }
        }
        //if somehow there is no one left to partner with because everyone has been visited already
        throw new RuntimeException("Error creating group: all matches have been taken already");
    }

    /**
     * Generates teams of the inputted size. If total classmates is not divisible by teamSize,
     * then add extra classmates to random group.
     *
     * @param teamSize - Int of desired team size
     * @return List<List<String>> representing the teams and the classmates ids for each team.
     */
    public List<List<String>> generateGroups(int teamSize) {
        if (teamSize < 1 || teamSize > classmatesMap.size()) {
            throw new RuntimeException("Team Size is negative or greater than number of classmates");
        }

        Map<String,List<String>> classmateToBestMatches = new HashMap<>();
        for (String classmateID: classmatesMap.keySet()) {
            if (!classmatesMap.containsKey(classmateID)) {
                classmateToBestMatches.put(classmateID,
                    getRecsFromStudentID(classmatesMap.size()-1, classmateID));
            }
        }
        // check unvisited instead
        List<String> unvisited = new ArrayList<>(classmatesMap.keySet());

        // figure out how to split up the groups

        // do classmatesmap.size()/teamsize - total number of teams
        // do classmatesmap.size() % teamsize - find the remainder
        //------------------> find number of groups which need an extra partner
        // pick random id from those whonhaven't been visited yet

        // find number of teams
        int numTeams = classmatesMap.size()/teamSize;
        Random rand = new Random();

        List<List<String>> listOfGroups= new ArrayList<>();

        // create team for each numTeams and add to listOfGroups
        for (int i = 0; i<numTeams; i++) {
            String randomID = unvisited.get(rand.nextInt(unvisited.size()));
            unvisited.remove(randomID);
            listOfGroups.add(createGroup(randomID, teamSize-1, new ArrayList<>(),
                unvisited, classmateToBestMatches));
        }
        // add remaining students to their top choice group
        for (int i=0;i<unvisited.size();i++) {
            //TO_DO
        }
        return listOfGroups;
    }
}
