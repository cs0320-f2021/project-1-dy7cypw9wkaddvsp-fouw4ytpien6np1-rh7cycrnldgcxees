package edu.brown.cs.student.main.KDT;

import edu.brown.cs.student.main.KDT.AbsNode;
import edu.brown.cs.student.main.KDT.INode;
import edu.brown.cs.student.main.table.Users;

/**
 * Class for Leaves, which are nodes in the KDTree that do not have children.
 */
public class Leaf extends AbsNode {

    public Leaf(Users user,INode parent, int level, String[] attrNames){
        super(user, parent, level, attrNames);
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
}
