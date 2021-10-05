package edu.brown.cs.student.main.table;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
  Object createObject(ResultSet rs) throws SQLException;

}
