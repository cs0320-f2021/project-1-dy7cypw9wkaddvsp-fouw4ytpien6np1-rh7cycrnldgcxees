package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.PartnerRecommender;

import java.sql.SQLException;

public class RecsysLoadHandler implements ICommandHandler{
    public static final String keyWord = "recsys_load";

    public RecsysLoadHandler(){}

    @Override
    public void handle(HandlerArguments handlerArguments) throws Exception {
        PartnerRecommender rec = handlerArguments.getRecommender();
        int k = Integer.parseInt(handlerArguments.getArguments()[1]);
        System.out.println("Loaded Recommender with " + k + " students.");
    }

    @Override
    public String keyWord(){return keyWord;}
}
