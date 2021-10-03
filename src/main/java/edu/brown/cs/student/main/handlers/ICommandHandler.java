package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.Database;

import java.sql.SQLException;

/**
 * Interface for CommandHandlers - need to handle different cases according to keyword
 */
public interface ICommandHandler {
    void handle(HandlerArguments handlerArguments) throws SQLException, ClassNotFoundException;
}
