package edu.brown.cs.student.main.handlers;

import java.sql.SQLException;
import edu.brown.cs.student.orm.Database;
import edu.brown.cs.student.orm.table.ITable;
import edu.brown.cs.student.orm.table.Users;

/**
 * Command Handler for "Users" keyword in REPL -
 * instantiates Database through given filepath and fills in KDTree according to Database
 */
public class UsersHandler implements ICommandHandler {
    public static final Class<Users> handlesClass = Users.class;
    public static final String keyWord = "users";


    public UsersHandler(){}

    /**
     * Method to handle "users" keyword case in REPL
     * @param handlerArgs - Includes reference to database, kd tree, and user arguments
     * @throws SQLException - in case issue with database connection
     * @throws ClassNotFoundException - in case class cannot be found
     */
    @Override
    public void handle(HandlerArguments handlerArgs) throws SQLException, ClassNotFoundException {
        try {// assign filepath of Database to given filepath
            handlerArgs.getDataBase().changePath(handlerArgs.getArguments()[1]);
        }
        catch (Exception e) {
            System.out.println("Invalid file path");
        }
    }
    // public Users newInstance(){}

    @Override
    public String keyWord() {return keyWord;}
}
