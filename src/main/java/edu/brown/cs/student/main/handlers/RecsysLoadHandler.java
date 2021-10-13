package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.PartnerRecommender;

import java.sql.SQLException;

public class RecsysLoadHandler implements ICommandHandler{
    @Override
    public void handle(HandlerArguments handlerArguments) throws Exception {
        PartnerRecommender rec = handlerArguments.getRecommender();
        int k = rec.setData();
        System.out.println("Loaded Recommender with " + k + " students.");
    }
}
