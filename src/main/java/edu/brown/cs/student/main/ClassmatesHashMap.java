package edu.brown.cs.student.main;
import edu.brown.cs.student.orm.table.ITable;
import edu.brown.cs.student.orm.table.Users;

import java.sql.SQLException;
import java.util.HashMap;

public class ClassmatesHashMap {
  /**
   * Class to store the Hashmap of aggregated Classmates from SQL Database and API call
   * Goes from Integer representing id to specific Classmate
   */

  private final HashMap<Integer, Classmate> map;

  /**
   * Constructor for map
   */
  public ClassmatesHashMap() throws SQLException {
    map = new HashMap<Integer, Classmate>();
  }

  /**
   * Gets Hashmap
   * @return Hashmap of Classmates
   */
  public HashMap<Integer, Classmate> getMap() {
    return map;
  }
}

