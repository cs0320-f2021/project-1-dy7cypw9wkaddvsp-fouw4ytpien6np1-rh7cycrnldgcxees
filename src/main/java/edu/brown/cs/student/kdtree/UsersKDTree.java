package edu.brown.cs.student.kdtree;

import edu.brown.cs.student.orm.Database;
import edu.brown.cs.student.orm.table.Users;

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
    private final HashMap<Integer, String> depthMap;

    /**
     * Default constructor (for instantiation in the REPL)
     */
    public UsersKDTree() {
        depthMap = new HashMap<>();
        depthMap.put(0, "height"); // levels % 3
        depthMap.put(1, "weight");
        depthMap.put(2, "age");
    }

    /**
     * Method to fill tree using data from given Database
     * @param db - Database to get User Data from
     * @throws SQLException - if issue with selecting items from Database
     */
    public void fillTree(Database db) throws SQLException {
        List<Users> allUsers = db.selectAllUsers();
        root = treeRecur(allUsers, 0, null);
    }

    /**
     * Helper function for fillTree - sorts given list of Users based on depth
     * @param userList - List of Users to be Sorted
     * @param depth - current level of tree; used to determine basis of sorting
     * @return Sorted list of Users
     */
    private List<Users> sortUsers(List<Users> userList, int depth) {
        if(depth % 3 == 0) {
            userList.sort(Comparator.comparing(Users::getHeight));
        }
        else if (depth % 3 == 1) {
            userList.sort(Comparator.comparing(Users::getWeight));
        }
        else {
            userList.sort(Comparator.comparing(Users::getAge));
        }
        return userList;
    }

    /**
     * Recursive helper function which places all Users into Nodes in Kd Tree
     * @param userList - list of Users to place in tree
     * @param depth - current depth - starts at 0
     * @return - current Node/root
     */
    private INode treeRecur(List<Users> userList, int depth, INode parent) {
        if(userList.size() > 1) {
            //sort list based on attribute
            List <Users> sortedList = sortUsers(userList, depth);

            // get median point (if even, this will take the second center point)
            int pivot = sortedList.size()/2;
            Users median = sortedList.get(pivot);

            // transform inputs to be able to compare
            String unformattedWeight = median.getWeight();
            double weight = Double.parseDouble(
                unformattedWeight.substring(0, unformattedWeight.length() - 3));
            String unformattedHeight = median.getHeight();
            String[] splitHeight = unformattedHeight.split(" ");
            Double feet =  Double.parseDouble(splitHeight[0].substring(0,
                splitHeight.length - 1)) * 12;
            Double inches = Double.parseDouble(splitHeight[1].substring(0,
                splitHeight.length - 2));
            double height = feet + inches;
            double age =  Double.parseDouble(median.getAge());

            Node medianNode = new Node(height, weight, age,
                    depthMap.get(depth % 3), depth, median, parent);
            INode left;
            INode right;
            if (userList.size() > 2) {
                left = treeRecur(sortedList.subList(0,pivot), depth+1, medianNode);
                right = treeRecur(sortedList.subList(pivot+1, sortedList.size()), depth+1, medianNode);
            }
            else {
                left = treeRecur(sortedList.subList(0,pivot), depth+1, medianNode);
                right = null;
            }

            medianNode.setLeft(left);
            medianNode.setRight(right);
            return medianNode;
        }
        else if (userList.size() == 1){
            Users current = userList.get(0);

            // transform inputs to be able to compare
            String unformattedWeight = current.getWeight();
            double weight = Double.parseDouble(
                unformattedWeight.substring(0, unformattedWeight.length() - 3));
            String unformattedHeight = current.getHeight();
            String[] splitHeight = unformattedHeight.split(" ");
            Double feet =  Double.parseDouble(splitHeight[0].substring(0,
                splitHeight.length - 1)) * 12;
            Double inches = Double.parseDouble(splitHeight[1].substring(0,
                splitHeight.length - 2));
            double height = feet + inches;
            double age =  Double.parseDouble(current.getAge());

            return new Leaf(
                    height, weight, age,
                    depthMap.get(depth % 3), depth, current, parent);
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

    private double calcDist(double nodeHeight, double nodeWeight, double nodeAge, Users user) {
        // transform inputs to be able to compare
        String unformattedWeight = user.getWeight();
        double weight = Double.parseDouble(
            unformattedWeight.substring(0, unformattedWeight.length() - 3));
        String unformattedHeight = user.getHeight();
        String[] splitHeight = unformattedHeight.split(" ");
        Double feet =  Double.parseDouble(splitHeight[0].substring(0,
            splitHeight.length - 1)) * 12;
        Double inches = Double.parseDouble(splitHeight[1].substring(0,
            splitHeight.length - 2));
        double height = feet + inches;
        double age =  Double.parseDouble(user.getAge());

        return Math.sqrt(Math.pow(nodeHeight - height, 2) + Math.pow(nodeWeight - weight, 2) +
            Math.pow(nodeAge - age, 2));
    }
    public List<Users> kNearestNeighbors(List<Users> currentList, INode currentNode,
                                         double height, double weight, double age) {
        Users currentUser = currentNode.getUser();
        double currDist = calcDist(height, weight, age, currentUser);
        for (Users nearUser: currentList) {

        }
        return currentList;
    }
}

