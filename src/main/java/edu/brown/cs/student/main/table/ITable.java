package edu.brown.cs.student.main.table;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ITable {

  /**
   * Method takes in an array of strings and creates an object from those strings
   */
  Object createObject(ResultSet rs) throws SQLException;

}
