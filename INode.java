package edu.brown.cs.student.main.KDT;

import edu.brown.cs.student.main.table.Users;

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

    int getLevel();

    INode createNode(Users user);
}
