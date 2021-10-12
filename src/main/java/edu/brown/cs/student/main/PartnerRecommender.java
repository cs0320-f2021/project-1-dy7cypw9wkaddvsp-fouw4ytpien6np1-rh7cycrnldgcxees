package edu.brown.cs.student.main;

import edu.brown.cs.student.api.main.ApiAggregator;
import edu.brown.cs.student.orm.Database;

import java.util.HashMap;

public class PartnerRecommender {
    HashMap<Integer, Classmate> classmates;
    ApiAggregator api;
    Database db;

    public PartnerRecommender() {}

    public void setApidata (){
        // call get getData on API to return list of classmates,
        // put each classmate from list into hashmap
        // have separate method to load in sql data for each classmate in hashmap

        //OR

        // for each classmate in list, get information directly from sql to
        // fully instantiate each classmate,
        // then add all classmates to hashmap
    }

    //THEN write methods to load data from hashmap into kdtree and bloom filter(s)
}
