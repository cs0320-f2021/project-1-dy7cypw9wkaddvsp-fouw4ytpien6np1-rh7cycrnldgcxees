package edu.brown.cs.student.orm.table;

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

