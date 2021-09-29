package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.Database;

import java.sql.SQLException;

/**
 * Interface for CommandHandlers
 */
public interface CommandHandler {
    void handle(Database db, String[] args) throws SQLException, ClassNotFoundException;
}
