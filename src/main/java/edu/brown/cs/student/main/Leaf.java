package edu.brown.cs.student.main;

public class Leaf extends AbsNode{

    public Leaf(double height, double weight, double age, String attr) {
        super(height, weight, age, attr);
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
}
