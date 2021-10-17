package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.PartnerRecommender;

import java.util.List;

public class RecsysRecHandler implements ICommandHandler{
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
        }
    }
}
