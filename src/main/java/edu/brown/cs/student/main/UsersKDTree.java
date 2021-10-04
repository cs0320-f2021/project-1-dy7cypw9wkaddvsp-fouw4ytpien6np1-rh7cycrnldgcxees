package edu.brown.cs.student.main;

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
    private final HashMap<Integer, String> depthMap;

    /**
     * Default constructor (for instantiation in the REPL)
     */
    public UsersKDTree() {
        depthMap = new HashMap<>();
        depthMap.put(0, "height");
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
        root = treeRecur(allUsers, 0);
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
    private INode treeRecur(List<Users> userList, int depth) {
        if(userList.size() > 1) {
            //sort list based on attribute
            List <Users> sortedList = sortUsers(userList, depth);

            // get median point (if even, this will take the second center point)
            int pivot = sortedList.size()/2;
            Users median = sortedList.get(pivot);

            INode left;
            INode right;
            if (userList.size() > 2) {
                left = treeRecur(sortedList.subList(0,pivot), depth+1);
                right = treeRecur(sortedList.subList(pivot+1, sortedList.size()), depth+1);
            }
            else {
                left = treeRecur(sortedList.subList(0,pivot), depth+1);
                right = null;
            }

            return new Node(
                    median.getHeight(), median.getWeight(), median.getAge(),
                    depthMap.get(depth % 3), depth, left, right);
        }
        else if (userList.size() == 1){
            Users current = userList.get(0);

            return new Leaf(
                    current.getHeight(), current.getWeight(), current.getAge(),
                    depthMap.get(depth % 3), depth);
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
}

