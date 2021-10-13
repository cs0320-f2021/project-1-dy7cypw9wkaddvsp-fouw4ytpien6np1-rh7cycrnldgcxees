package edu.brown.cs.student.main;

import edu.brown.cs.student.api.client.ApiClient;
import edu.brown.cs.student.api.main.ApiAggregator;
import edu.brown.cs.student.orm.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class PartnerRecommender {
    HashMap<Integer, Classmate> classmatesMap = new ClassmatesHashMap().getMap();
    private ApiAggregator api;
    private final Database db = new Database();

    public PartnerRecommender() throws SQLException, ClassNotFoundException {}

    public void setData () throws Exception {
        // call get getData on API to return list of classmates,
        // put each classmate from list into hashmap
        // have separate method to load in sql data for each classmate in hashmap
        List<Classmate> classmates = api.getAPIData("Classmate");

        // change path to integration.sqlite3:
        this.db.changePath("data/integration/integration.sqlite3");
        // run this raw query once to join all 4 tables together:

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

        // match classmate to the corresponding SQL entry in aggregate by id:
        for (Classmate classmate : classmates) {
            ResultSet rs = db.returnRSFromQuery("id",
                String.valueOf(classmate.getId()), "aggregate");
            classmate.setSQLData(rs);
            // for each classmate, add them to a HM of IDs -> Classmate
            classmatesMap.put(classmate.getId(), classmate);
        }
        // Write methods to load data from hashmap into kdtree and bloom filter(s) from
        // classmatesMap
    }
}
