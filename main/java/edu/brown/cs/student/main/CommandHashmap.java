package edu.brown.cs.student.main;

import edu.brown.cs.student.main.handlers.ICommandHandler;
import edu.brown.cs.student.main.handlers.ChangePathHandler;
import edu.brown.cs.student.main.handlers.UsersHandler;

import java.util.HashMap;

/**
 * Class to store the Hashmap of valid keywords for REPL
 * Goes from string representing keyword to specific Command Handler for keyword
 */
public class CommandHashmap {
    private final HashMap<String, ICommandHandler> map;

    /**
     * Constructor
     */
    public CommandHashmap() {
        map = new HashMap<String, ICommandHandler>();
        this.map.put("database", new ChangePathHandler());
        this.map.put("users", new UsersHandler());
    }

    /**
     * Gets Hashmap
     * @return Hashmap of keywords to command handlers
     */
    public HashMap<String, ICommandHandler> getMap() {
        return map;
    }
}
