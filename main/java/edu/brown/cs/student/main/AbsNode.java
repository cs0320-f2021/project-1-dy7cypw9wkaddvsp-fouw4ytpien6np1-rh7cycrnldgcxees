package edu.brown.cs.student.main;

/**
 * Abstract Class for Nodes which encompasses shared characteristics of leaves and nodes
 */
public abstract class AbsNode implements INode{
    private final double height;
    private final double weight;
    private final double age;
    private final int level;

    /**
     * Indicates which attribute the item at this tier is splitting on
     */
    private final String attribute;

    public AbsNode(double height, double weight, double age, String type, int level) {
        this.height = height;
        this.weight=weight;
        this.age = age;
        this.level = level;
        if (!type.equals("h") && !type.equals("w") && !type.equals("a")) {
            throw new RuntimeException("invalid attribute tag");
        }
        else {
            this.attribute = type;
        }
    }

    public double getHeight() {
        return height;
    }
    public double getWeight() {
        return weight;
    }
    public double getAge() {
        return age;
    }

    public String getAttribute() {
        return attribute;
    }

    public int getLevel() {
        return level;
    }
}
