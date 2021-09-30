package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.Database;

import java.sql.SQLException;

public class InsertHandler implements CommandHandler {

    @Override
    public void handle(Database db, Object object) throws SQLException, IllegalAccessException {
        try {
            db.insert(object);
        } catch (Exception e) {
             System.out.println("Insert Error " + e.getMessage());
        }
    }
}
