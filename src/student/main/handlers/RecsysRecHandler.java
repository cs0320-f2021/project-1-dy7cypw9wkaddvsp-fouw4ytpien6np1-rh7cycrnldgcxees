package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.PartnerRecommender;

import java.sql.SQLException;

public class RecsysRecHandler implements ICommandHandler{
    public static final String keyWord = "recsys_rec";
    public final PartnerRecommender pr;

    public RecsysRecHandler(){
        this.pr = new PartnerRecommender();
    }

    public RecsysRecHandler(PartnerRecommender pr){
        this.pr = pr;
    }

    @Override
    public void handle(HandlerArguments handlerArguments) throws SQLException, ClassNotFoundException, Exception {
        PartnerRecommender recommender = handlerArguments.getRecommender();
        int numrecs = Integer.parseInt(handlerArguments.getArguments()[1]);
        recommender.getRecsFromStudentID(numrecs, handlerArguments.getArguments()[2]);
        System.out.println("Recommended successfully");
    }

    @Override
    public String keyWord() {return keyWord;}
}
