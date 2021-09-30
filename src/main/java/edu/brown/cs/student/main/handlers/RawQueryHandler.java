package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.Database;

import java.util.Collection;

public class RawQueryHandler implements CommandHandler {

  @Override
  public void handle(Database db, String[] args) {
    try {
      db.rawQuery(args[0]);
    } catch (Exception e) {
      System.out.println("Query Error " + e.getMessage());
    }
  }
}
