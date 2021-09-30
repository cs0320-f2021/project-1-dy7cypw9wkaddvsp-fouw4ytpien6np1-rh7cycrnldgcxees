package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.Database;

public class UpdateHandler implements CommandHandler{

  @Override
  public void handle(Database db, String[] args) {
    try {

    } catch (Exception e) {
      System.out.println("Update Error " + e.getMessage());
    }
  }
}