package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.PartnerRecommender;

import java.sql.SQLException;

public class RecsysRecHandler implements ICommandHandler {
    public static final String keyWord = "recsys_rec";
    public PartnerRecommender pr;

    public RecsysRecHandler(){}

    @Override
    public void handle(HandlerArguments handlerArguments) throws SQLException, ClassNotFoundException, Exception {
        try {
            this.pr = handlerArguments.getRecommender();
            int numrecs = Integer.parseInt(handlerArguments.getArguments()[1]);
            this.pr.getRecsFromStudentID(numrecs, handlerArguments.getArguments()[2]);
            System.out.println("Recommended successfully");
        }
        catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public String keyWord() {return keyWord;}
}
