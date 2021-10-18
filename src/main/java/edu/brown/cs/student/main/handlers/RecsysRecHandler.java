package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.PartnerRecommender;

import java.util.List;

/**
 * Class representing a CommandHandler for the recsys_rec command
 */
public class RecsysRecHandler implements ICommandHandler {

    /**
     * Handles the arguments inputted from the REPL and then outputs a list of compatible
     * IDs for the inputted User.
     *
     * @param handlerArguments - arguments from REPL (3 - recsys_rec <num_recs> <student_id>)
     * @throws NullPointerException - four things can go wrong: the user can try to use command
     * before data is loaded, the user could input an ID that doesn't exist, the user could input
     * a number of recommendations larger than the total number of users, or a user could enter
     * a negative value for the number of recommendations.
     */
    @Override
    public void handle(HandlerArguments handlerArguments) throws NullPointerException {
        try {
            PartnerRecommender recommender = handlerArguments.getRecommender();
            int numRecs = Integer.parseInt(handlerArguments.getArguments()[1]);
            List<String> ranks = recommender.getRecsFromStudentID(numRecs,
                handlerArguments.getArguments()[2]);

            // print out top k results by average rank
            System.out.println("Found top " + numRecs + " matches. Here are the IDs:");
            for (int i = 0; i < numRecs; i++) {
                String currRank = ranks.get(i);
                System.out.println(currRank);
            }
            System.out.println("Recommended successfully");
        } catch (NullPointerException e) {
            System.out.println("Error, run recsys_load first.");
        } catch (RuntimeException e) {
            System.out.println("inputted StudentID does not exist or number of recommendations" +
                "requested is invalid.");
        }
    }
}
