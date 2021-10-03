package handlers;

import edu.brown.cs.student.main.Database;

import java.sql.SQLException;

public class DatabaseHandler implements CommandHandler {

    @Override
    public void handle(Database db, String[] args) throws SQLException, ClassNotFoundException {
        try {
            db.changePath(args[1]);
        }
        catch (Exception e) {
            System.out.println("Invalid file path");
        }
    }
}
