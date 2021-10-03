package edu.brown.cs.student.main;

public class Node extends AbsNode{
    private INode left;
    private INode right;


    public Node(double height, double weight, double age, String attr) {
        super(height, weight,age, attr);
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
