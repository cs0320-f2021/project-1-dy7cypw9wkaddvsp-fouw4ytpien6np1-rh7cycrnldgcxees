package edu.brown.cs.student.main.KDT;

import edu.brown.cs.student.main.Database;
import edu.brown.cs.student.main.table.Users;

import java.sql.SQLException;
import java.util.*;

/**
 * Class to interact with KdTrees which store Users
 * which stores Users in tree according to Height, Weight, Age proximity
 */
public class UsersKDTree implements IKDTree {
    /**
     * The top of the tree, the node which connects to all other nodes
     */
    private INode root;
    // private String[] attributeNames;
    private final HashMap<Integer, String> dMap; // dimension int -> "attr name"
    private final String[] attrNames;

    /**
     * Default constructor (for instantiation in the REPL)
     */
    public UsersKDTree(Database db, String[] attributeNames) throws SQLException {
        this.attrNames = attributeNames;
        this.dMap = makeDimensionMap(attributeNames);
        List<Users> allUsers = db.selectAllUsers();
        fillTree(allUsers);
    }

    public HashMap<Integer, String> makeDimensionMap(String[] attributeNames){
        HashMap<Integer, String> dMap = new HashMap<>();
        int i = 0;
        for (String a : attributeNames) {
            dMap.put(i, a);
            i+=1;
        }
        return dMap;
    }

    /**
     * Method to fill tree using data from given Database
     * @param allUsers - list of all users
     */
    public void fillTree(List<Users> allUsers) throws SQLException {
        root = treeRecur(getRoot(), allUsers);
    }

    /**
     * Helper function for fillTree - sorts given list of Users based on depth
     * @param userList - List of Users to be Sorted
     * @param depth - current level of tree; used to determine basis of sorting
     * @return Sorted list of Users
     */
    private List<Users> sortUsers(List<Users> userList, int depth) {
        if(depth % 3 == 0) { userList.sort(Comparator.comparing(Users::getHeight));}
        else if (depth % 3 == 1) {userList.sort(Comparator.comparing(Users::getWeight));}
        else {userList.sort(Comparator.comparing(Users::getAge));}
        return userList;
    }

    /**
     * Recursive helper function which places all Users into Nodes in Kd Tree
     * @param currNode - current node
     * @param userList - list of Users to place in tree
     * @return - current Node/root
     */
    private INode treeRecur(INode currNode, List<Users> userList) {
        // todo: if root == null, sort by dimension 0, send back thru
        if (root == null){
            List <Users> sortedList = sortUsers(userList, 0);
            Users medianUser = sortedList.get(sortedList.size()/2);
            root = new Node(medianUser, null, 0, attrNames);
            treeRecur(root, userList);
        }
        if (userList.size() > 1) {
            List <Users> sortedList = sortUsers(userList, currNode.getLevel()); // sort list on dimension
            int pivot = sortedList.size()/2; // get median point (or 2nd to center)
            Users medianUser = sortedList.get(pivot);
            INode medianNode = currNode.createNode(medianUser);

            currNode.setLeft(); = treeRecur(medianNode, sortedList.subList(0,pivot));;
            INode right;

            if (userList.size() > 2) { treeRecur(medianNode, sortedList.subList(pivot+1, sortedList.size()));}
            else right = null;


            medianNode.setLeft(left);
            medianNode.setRight(right);
            return medianNode;
        }
        else if (userList.size() == 1){
            Users user = userList.get(0);
            return new Leaf(user, currNode, currNode.getLevel(), attrNames);
        }
        else {
            throw new RuntimeException("User List is Empty");
        }
    }

    /**
     * Gets root/top Node of tree
     * @return the root of the tree
     */
    public INode getRoot() {
        return root;
    }

    /*
    Given: a node and a target point to search around...
Get the straight-line (Euclidean) distance from your target point to the current node.
If the current node is closer to your target point than one of your k-nearest neighbors,
or if your collection of neighbors is not full, update the list accordingly

Find the relevant axis (x, y, z) according to the depth.

If the Euclidean distance between the target point and the farthest of the current
“best neighbors” you have so far is greater than the relevant axis distance*
between the current node and target point, recur on both children.
*The axis distance is the straight line distance from your target point to the plane,
so the difference between the two points on the relevant axis
(target’s i’th coordinate - current node’s i’th coordinate)
Searching the other child is important, because if there is some chance that there
is a closer point on the other side of the plane, then you must check that branch of the k-d tree.

**Note: that if you have found less than k neighbors, your maximum search distance
is the entire space. That is, recur on both subtrees of the current node.

If the previous if-statement is false and you do not need to recur down both children, then:
If the current node's coordinate on the relevant axis is less than the target's coordinate on
the relevant axis, recur on the right child.

Else if the current node's coordinate on the relevant axis is greater than the target's
coordinate on the relevant axis, recur on the left child.
     */
}

