package edu.brown.cs.student.main.handlers;

import java.sql.SQLException;

/**
 * Command handler for "database" keyword in REPL
 * To pass database filepath into Database and create connection
 */
public class ChangePathHandler implements ICommandHandler {

    /**
     * Handles "database" keyword in REPL
     * @param handlerArgs - Includes reference to database, kd tree, and user arguments
     * @throws SQLException - in case issue with database connection
     * @throws ClassNotFoundException - in case class cannot be found
     */
    @Override
    public void handle(HandlerArguments handlerArgs) throws SQLException, ClassNotFoundException {
        try {
            handlerArgs.getDataBase().changePath(handlerArgs.getArguments()[1]);
        }
        catch (Exception e) {
            System.out.println("Invalid file path");
        }
    }
}
