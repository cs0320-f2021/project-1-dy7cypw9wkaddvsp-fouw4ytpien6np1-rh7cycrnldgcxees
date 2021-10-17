package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.kdtree.coordinates.KdTree;
import edu.brown.cs.student.main.PartnerRecommender;
import edu.brown.cs.student.orm.Database;

/**
 * Class to hold references to KDTree and Database, as well as REPL arguments
 * To be passed into different types of Command Handlers
 */
public class HandlerArguments {

    private final KdTree<?> kdTree;
    private final Database db;
    private final PartnerRecommender recommender;
    private final String[] args;

    /**
     * Constructor
     * @param kd - KDTree from Main
     * @param db - Database from Main
     * @param rec - PartnerRecommender from Main
     * @param args - String[] of REPL arguments from user
     */
    public HandlerArguments(KdTree<?> kd, Database db, PartnerRecommender rec, String[] args) {
        this.kdTree = kd;
        this.db = db;
        this.recommender = rec;
        this.args = args;
    }


    /**
     * Gets KDTree
     * @return the KDTree reference
     */
    public KdTree<?> getKdTree() {
        return kdTree;
    }

    /**
     * Gets Database
     * @return The Database reference
     */
    public Database getDataBase() {
        return db;
    }
    /**
     * Gets PartnerRecommender
     * @return the PartnerRecommender reference
     */
    public PartnerRecommender getRecommender() {
        return recommender;
    }

    /**
     * Gets Arguments
     * @return REPL arguments from user
     */
    public String[] getArguments() {
        return args;
    }


}
