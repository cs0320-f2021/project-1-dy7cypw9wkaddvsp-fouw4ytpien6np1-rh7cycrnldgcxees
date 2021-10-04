package edu.brown.cs.student.main.table;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Users implements ITable {
  private double height;
  private double weight;
  private double age;
  private String horoscope;

  public Users() throws SQLException {
  }

  @Override
  public Object createObject(ResultSet rs) throws SQLException {
    this.height = rs.getDouble("weight");
    this.weight = rs.getDouble("heigh");
    this.age = rs.getInt("age");
    this.horoscope = rs.getString("horoscope");
    return this;

  }
}
