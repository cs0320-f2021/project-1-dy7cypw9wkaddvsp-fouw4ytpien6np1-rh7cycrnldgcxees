package handlers;

import edu.brown.cs.student.main.Database;

import java.sql.SQLException;

/**
 * Interface for CommandHandlers
 */
public interface CommandHandler<T> {
    void handle(Database db, String[] args) throws SQLException, ClassNotFoundException;
}
