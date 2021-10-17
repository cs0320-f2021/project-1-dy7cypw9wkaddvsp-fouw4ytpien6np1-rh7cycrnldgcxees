package edu.brown.cs.student.main;

import edu.brown.cs.student.main.handlers.*;

import java.util.HashMap;

/**
 * Stores valid keywords for REPL in a hashmap mapping keywords to a Command Handlers
 * Goes from string representing keyword to specific Command Handler for keyword
 */
public class CommandHashmap {
    private static HashMap<String, ICommandHandler> map = new HashMap<>();
    /**
     * No-arg constructor
     */
    public CommandHashmap(){}

    public CommandHashmap(ICommandHandler[] handlers){
        for (ICommandHandler ch: handlers){map.put(ch.keyWord(), ch);}
    }

    public ICommandHandler getHandler(String keyword){
        return this.getMap().get(keyword);
    }

    // allows users to make their own handlers,
    public void addHandler(String handlerFor, ICommandHandler commandHandler) {
        if (!map.containsKey(handlerFor)) map.put(handlerFor, commandHandler);
        // else if (!map.get(handlerFor).equals(commandHandler)) <--- todo: error
    }

    /**
     * Gets Hashmap
     * @return Hashmap of keywords to command handlers
     */
    public HashMap<String, ICommandHandler> getMap() {return map;}
}
