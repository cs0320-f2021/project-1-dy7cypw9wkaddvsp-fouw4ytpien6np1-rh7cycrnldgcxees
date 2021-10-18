package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.PartnerRecommender;

import java.sql.SQLException;
import java.util.List;

/**
 * Class representing a CommandHandler for the recsys_gen_group command
 */
public class RecsysGroupHandler implements ICommandHandler {
  public static final String keyWord = "recsys_gen_groups";
  /**
   * Handle method for recsys_gen_group command
   *
   * @param handlerArguments - arguments from REPL
   * @throws RuntimeException - occurs when teamsize argument is invalid, i.e. less than 1 or
   * greater than total number of students.
   */
  @Override
  public void handle(HandlerArguments handlerArguments)
      throws IllegalArgumentException {
    try {
      PartnerRecommender recommender = handlerArguments.getRecommender();
      int teamSize = Integer.parseInt(handlerArguments.getArguments()[1]);
      List<List<String>> groups = recommender.generateGroups(teamSize);
      int i = 1;
      for (List<String> group: groups) {
        System.out.println("Group " + i + ": "+ group.toString());
        i++;
      }


    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public String keyWord() {
    return keyWord;
  }
}
