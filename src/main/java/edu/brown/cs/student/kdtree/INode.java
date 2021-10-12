package edu.brown.cs.student.kdtree;

import edu.brown.cs.student.main.orm.table.Users;

/**
 * Interface for Nodes and Leaves
 */
public interface INode {
    /**
     * Returns true if has no children, else false
     * @return boolean
     */
    boolean isLeaf();

    /**
     * Gets user field of Node
     * @return users
     */
    Users getUser();
} 
