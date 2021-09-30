package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.Database;

public class DeleteHandler implements CommandHandler {

  @Override
  public void handle(Database db, Object object) {
      try {
        db.delete(object);
      } catch (Exception e) {
        System.out.println("Delete Error " + e.getMessage());
    }
  }
}