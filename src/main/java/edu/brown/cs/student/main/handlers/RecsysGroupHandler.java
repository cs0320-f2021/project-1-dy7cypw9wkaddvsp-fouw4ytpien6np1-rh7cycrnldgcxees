package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.PartnerRecommender;

import java.sql.SQLException;
import java.util.List;

/**
 * Class representing a CommandHandler for the recsys_gen_group command
 */
public class RecsysGroupHandler implements ICommandHandler {
  /**
   * Handle method for recsys_gen_group command
   *
   * @param handlerArguments - arguments from REPL
   * @throws RuntimeException - occurs when teamsize argument is invalid, i.e. less than 1 or
   * greater than total number of students.
   */
  @Override
  public void handle(HandlerArguments handlerArguments)
      throws RuntimeException {
    try {
      PartnerRecommender recommender = handlerArguments.getRecommender();
      int teamSize = Integer.parseInt(handlerArguments.getArguments()[1]);
      List<List<String>> output = recommender.generateGroups(teamSize);
      for (List<String> list : output) {
        System.out.println(list.toString());
      }
    } catch (RuntimeException e) {
      System.out.println("Invalid teamSize argument.");
    }
  }
}
