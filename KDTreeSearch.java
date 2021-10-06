package edu.brown.cs.student.main.KDT;

import edu.brown.cs.student.main.table.Users;

import java.util.HashMap;
import java.util.List;

public class KDTreeSearch {
    UsersKDTree kdTree; // root, depth map <dimension, "attribute">
    HashMap<Integer, Double> targetValues; // <dimension, target value on dimension>
    HashMap<Double, INode> bestDistances;  // <distance from target, INode>


    public KDTreeSearch(UsersKDTree kdt, HashMap<Integer, Double> target){
        this.kdTree = kdt;
        this.bestDistances = new HashMap<>();
        this.targetValues = target;
        // targetValues.put(0, height);
        // targetValues.put(1, weight);
        // targetValues.put(2, age);
    }

    private double calcDist(INode node, double targetHeight, double targetWeight, double targetAge) {
        Users user = node.getUser();
        return Math.sqrt(Math.pow(targetHeight-user.getHeight(), 2)
                + Math.pow(targetWeight-user.getWeight(), 2)
                + Math.pow(targetAge-user.getAge(), 2));
    }

    // a band-aid, will change
    private double valOnDimension(INode node, int dimension){
        if (dimension == 0) return node.getUser().getHeight();
        if (dimension == 1) return node.getUser().getWeight();
        else return node.getUser().getAge();
    }

    private boolean shouldRecur(INode subtree) {
        // WHEN TO RECUR FOR SUBTREE, for dimension d
        // target value on dimension - currNode on dimension <= calcDist(target value, best distance so far)
        //                                                "" <= calcDist(target value, sorted bestDistances.keys)
        Users user = subtree.getUser();
        int dimension = subtree.getLevel()%3;
        return (targetValues.get(dimension) - valOnDimension(subtree, dimension)
                <= calcDist(subtree, user.getHeight(), user.getWeight(), user.getAge()));
    }

    // nnSearch(fromNode, target)
    // if (!node.isLeaf) {
    //      choose subtree, find dimension
    //      calculate distance, check, store, and remove if needed
    //      if productive space exists on one subtree, recur
    //      else if productive space exists on other subtree, recur
    //      else return sorted nodes? neighbors? on keys in bestDistance
    // }

    public List<Users> kNearestNeighbors(INode currNode,
                                         double targetHeight, double targetWeight, double targetAge) {
        Users currUser = currNode.getUser();
        double currDist = calcDist(targetHeight, targetWeight, targetAge, currUser);

        // choose left or right subtree
        //     check if there is a productive space (whether you should recur)
        //     check value against best distance values
        }
        // return currList;
    }
}
