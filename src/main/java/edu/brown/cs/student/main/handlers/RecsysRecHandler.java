package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.PartnerRecommender;
import freemarker.log._NullLoggerFactory;

import java.sql.SQLException;

public class RecsysRecHandler implements ICommandHandler{
    @Override
    public void handle(HandlerArguments handlerArguments) throws SQLException, ClassNotFoundException, Exception {
        try {
            PartnerRecommender recommender = handlerArguments.getRecommender();
            int numrecs = Integer.parseInt(handlerArguments.getArguments()[1]);
            recommender.getRecsFromStudentID(numrecs, handlerArguments.getArguments()[2]);
            System.out.println("Recommended successfully");
        } catch (NullPointerException e) {
            System.out.println("Error, run recsys_load first.");
        }
    }
}
