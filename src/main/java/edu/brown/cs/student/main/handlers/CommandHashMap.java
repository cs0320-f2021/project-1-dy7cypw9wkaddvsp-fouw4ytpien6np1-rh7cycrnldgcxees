package handlers;

import handlers.CommandHandler;
import handlers.InsertHandler;
import handlers.DeleteHandler;
import handlers.DatabaseHandler;
import handlers.RawQueryHandler;
import handlers.SelectHandler;
import handlers.UpdateHandler;
import java.util.HashMap;
import java.util.Map;

public class CommandHashMap {
  private final Map<String, CommandHandler<?>> commandHashMap = new HashMap<>();

  public <T extends CommandHandler<T>>
  Map<String, CommandHandler<?>> CommandHashMap() {
    Map<String, CommandHandler<?>> commandHashMap = new HashMap<>();
    commandHashMap.put("insert", new InsertHandler());
    commandHashMap.put("delete", new DeleteHandler());
    commandHashMap.put("database", new DatabaseHandler());
    commandHashMap.put("query", new RawQueryHandler());
    commandHashMap.put("where", new SelectHandler());
    commandHashMap.put("update", new UpdateHandler());
    return commandHashMap;
  }
}
