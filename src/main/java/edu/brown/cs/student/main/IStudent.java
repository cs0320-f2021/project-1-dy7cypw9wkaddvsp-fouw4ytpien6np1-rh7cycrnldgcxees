package edu.brown.cs.student.main;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class to represent a generic Object to collect data from ORM and API
 */
public interface IStudent {

  /**
   * Method that a generic Object would need to implement if Data changes
   * @param rs - resultSet from corresponding table
   * @throws SQLException - throws result if error occurs in SQL integration
   */
  void setSQLData(ResultSet rs) throws SQLException;
}
