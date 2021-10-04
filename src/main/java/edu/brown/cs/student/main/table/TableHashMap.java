package edu.brown.cs.student.main.table;

import edu.brown.cs.student.main.handlers.ChangePathHandler;
import edu.brown.cs.student.main.handlers.ICommandHandler;
import edu.brown.cs.student.main.handlers.UsersHandler;

import java.sql.SQLException;
import java.util.HashMap;

public class TableHashMap {
  /**
   * Class to store the Hashmap of created Tables from SQL Database
   * Goes from string representing tableName to specific implementation of ITable
   */

  private final HashMap<String, ITable> map;

  /**
   * Constructor
   */
  public TableHashMap() throws SQLException {
    map = new HashMap<String, ITable>();
    this.map.put("users", new Users());
  }

  /**
   * Gets Hashmap
   * @return Hashmap of tableName to specific implementation of ITable
   */
  public HashMap<String, ITable> getMap() {
    return map;
  }
}

