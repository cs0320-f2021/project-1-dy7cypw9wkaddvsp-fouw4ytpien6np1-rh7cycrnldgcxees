package edu.brown.cs.student.main.KDT;

import edu.brown.cs.student.main.Database;

import java.sql.SQLException;

/**
 * Interface for KDTrees
 */
public interface IKDTree {
    /**
     * Fill tree in a balanced way with Nodes that correspond with
     * tree type, based on values of attributes
     * @param db - database to pull data from
     * @throws SQLException - if issue pulling from database
     */
    void fillTree(Database db) throws SQLException;
}
