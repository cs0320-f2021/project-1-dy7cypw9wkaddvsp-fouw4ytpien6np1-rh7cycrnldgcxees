package edu.brown.cs.student.main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class to interact with KdTree which stores users in tree according to Height, Weight, Age proximity
 */
public class KDTree {
    /**
     * The top of the tree, the node which connects to all other nodes
     */
    private INode root;

    /**
     * Constructor which takes in the Database that it will interact with
     * @param db Database to pull user data from
     */
    public KDTree(Database db) {
        /*
        For each row of the users in the database (for each user),
        we get the height, weight, age -> create a new node, and put it in
        the tree
         */
    }

    /**
     * Default constructor (for instantiation in the REPL)
     */
    public KDTree() { }

    /**
     * Method to fill tree using data from given Database
     * @param db - Database to get User Data from
     * @throws SQLException - if issue with selecting items from Database
     */
    public void fillTree(Database db) throws SQLException {
        List<String> attrs = new ArrayList<>();
        attrs.add("user_id");
        attrs.add("height");
        attrs.add("weight");
        attrs.add("age");
        attrs.add("horoscope");
        Collection<Object> allUsers = db.selectAll(attrs, "users");


        // how do we cast these objects as users????
        for (Object user: allUsers) {
           // user.getHeight
            // ^^^ can't run this unless we have a collection of users,
            // not a collection of objects
        }

    }
}

