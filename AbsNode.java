package edu.brown.cs.student.main.KDT;
import edu.brown.cs.student.main.table.Users;

import java.util.HashMap;
import java.util.List;

/**
 * Abstract Class for Nodes which encompasses shared characteristics of leaves and nodes
 */
public abstract class AbsNode implements INode {
    private final Users user;
    final INode parent;
    int level = 0;
    private final int dimension;
    final String[] attrNames;

    /**
     * Indicates which attribute the item at this tier is splitting on
     */
    // private final String attribute;
    // public String attrOnDimension(){return attrNames[dimension];}

    public AbsNode(Users user, INode parent, int level, String[] attrNames) {
        this.user = user;
        this.parent = parent;
        if (parent != null) this.level = parent.getLevel()+1;
        this.attrNames = attrNames;
        this.dimension = level%3;
    }

    public double valOnDimension(){
        String attrName = user.attrName(dimension);
        return user.attrFromName(attrName);
    }

    // note- createNode overwritten in leaf
    public INode createNode(Users user){
        return new Node(user, parent, level, attrNames);
    }
    public int getLevel() {
        return level;
    }
    public Users getUser() {
        return user;
    }
    public INode getParent() {
        return parent;
    }
}
