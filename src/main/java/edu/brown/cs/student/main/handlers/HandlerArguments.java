package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.orm.Database;
import edu.brown.cs.student.kdtree.UsersKDTree;

/**
 * Class to hold references to KDTree and Database, as well as REPL arguments
 * To be passed into different types of Command Handlers
 */
public class HandlerArguments {

    private final UsersKDTree kdTree;
    private final Database db;
    private final String[] args;

    /**
     * Constructor
     * @param kd - KDTree from Main
     * @param db - Database from Main
     * @param args - String[] of REPL arguments from user
     */
    public HandlerArguments(UsersKDTree kd, Database db, String[] args) {
        this.kdTree = kd;
        this.db = db;
        this.args = args;
    }

    /**
     * Gets KDTree
     * @return the KDTree reference
     */
    public UsersKDTree getKdTree() {
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
     * Gets Arguments
     * @return REPL arguments from user
     */
    public String[] getArguments() {
        return args;
    }
}
