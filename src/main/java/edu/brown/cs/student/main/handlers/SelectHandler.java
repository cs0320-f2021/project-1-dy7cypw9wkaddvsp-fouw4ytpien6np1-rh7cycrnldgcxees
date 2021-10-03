package handlers;

import edu.brown.cs.student.main.Database;

import java.util.Collection;

public class SelectHandler implements CommandHandler {

  @Override
  public void handle(Database db, String[] args) {
    try {
      Collection<Object> queryResult = db.where();
    } catch (Exception e) {
      System.out.println("Select Error " + e.getMessage());
    }
  }
}
