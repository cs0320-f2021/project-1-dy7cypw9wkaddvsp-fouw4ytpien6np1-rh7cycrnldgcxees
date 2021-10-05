package edu.brown.cs.student.main;

import edu.brown.cs.student.main.table.Users;

/**
 * Class for Nodes, which are nodes in the KDTree that have children.
 */
public class Node extends AbsNode{
    private INode left;
    private INode right;

    public Node(double height, double weight, double age, String attr, int depth, Users user, INode parent) {
        super(height, weight,age, attr, depth, user, parent);
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    public INode getLeft() {
        return left;
    }

    public INode getRight() {
        return right;
    }

    public void setLeft(INode newLeft) {
        this.left = newLeft;
    }

    public void setRight(INode newRight) {
        this.right = newRight;
    }
}
