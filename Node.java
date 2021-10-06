package edu.brown.cs.student.main.KDT;
import edu.brown.cs.student.main.KDT.AbsNode;
import edu.brown.cs.student.main.KDT.INode;
import edu.brown.cs.student.main.table.Users;

/**
 * Class for Nodes, which are nodes in the KDTree that have children.
 */
public class Node extends AbsNode {
    private INode left;
    private INode right;

    public Node(Users user, INode parent, int level, String[] attrNames) {
        super(user, parent, level, attrNames);
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public void setLeft(INode leftSubtree) {

    }


    public INode getLeft() {
        return left;
    }

    public INode getRight() {
        return right;
    }

    @Override
    public void setLeft(Users user) {
        this.left = createNode(user);
    }

    @Override
    public void setRight(Users user) {
        this.right = createNode(user);
    }
}
