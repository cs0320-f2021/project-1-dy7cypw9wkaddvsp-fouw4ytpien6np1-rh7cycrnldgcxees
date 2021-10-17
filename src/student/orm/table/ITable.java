package edu.brown.cs.student.orm.table;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface to get objects from databases
 */
public interface ITable {

  /**
   * Method takes in an Result Set (row) from the database and creates an object from the columns
   * @param rs - Result Set to create object from
   * @return - Object from Result Set
   * @throws SQLException - If issue getting data from database
   */
  Object populateFields(ResultSet rs) throws SQLException;

  // ITableObj newInstance();

  String iTableName();

}
