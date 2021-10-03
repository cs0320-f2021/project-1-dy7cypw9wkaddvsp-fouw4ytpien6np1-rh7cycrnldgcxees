package edu.brown.cs.student.main;

/**
 * Class for Leaves, which are nodes in the KDTree that do not have children.
 */
public class Leaf extends AbsNode{

    public Leaf(double height, double weight, double age, String attr, int depth) {
        super(height, weight, age, attr, depth);
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
}
