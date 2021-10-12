package edu.brown.cs.student.kdtree;

import edu.brown.cs.student.main.orm.table.Users;

/**
 * Class for Leaves, which are nodes in the KDTree that do not have children.
 */
public class Leaf extends AbsNode{

    public Leaf(double height, double weight, double age, String attr, int depth, Users user, INode parent) {
        super(height, weight, age, attr, depth, user, parent);
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
}
