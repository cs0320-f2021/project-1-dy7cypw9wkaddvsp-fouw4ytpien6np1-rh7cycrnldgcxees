package edu.brown.cs.student.main.table;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Class to create User Objects from columns of Database
 */
public class Users implements ITable {
  private double height;
  private double weight;
  private double age;
  private String horoscope;

  /**
   * Default constructor
   */
  public Users() {
  }

  /**
   * Create instance of Object from columns of Result Set
   * @param rs - result set from Database
   * @return Object which is a User
   * @throws SQLException - if there is an issue reading the Database
   */
  @Override
  public Object createObject(ResultSet rs) throws SQLException {
    this.height = rs.getDouble("height");
    this.weight = rs.getDouble("weight");
    this.age = rs.getInt("age");
    this.horoscope = rs.getString("horoscope");
    return this;
  }

  /**
   * Creates instance of User from columns of Result Set
   * @param rs - Result Set to get attributes from
   * @return - User object with appropriate fields
   * @throws SQLException - if issue with Database
   */
  public Users createUserObject(ResultSet rs) throws SQLException {
    this.height = rs.getDouble("height");
    this.weight = rs.getDouble("weight");
    this.age = rs.getInt("age");
    this.horoscope = rs.getString("horoscope");
    return this;
  }

  /**
   * Gets height of User
   * @return double representing height
   */
  public double getHeight() {
    return height;
  }

  /**
   * Gets weight of User
   * @return - double representing weight
   */
  public double getWeight() {
    return weight;
  }

  /**
   * Gets Age of User
   * @return - double representing age
   */
  public double getAge() {
    return age;
  }

  /**
   * Gets horoscope of User
   * @return - String representing horoscope sign
   */
  public String getHoroscope() {
    return horoscope;
  }

  /**
   * @param attr
   * @return
   */
  public double attrFromName(String attr) {
    switch (attr) {
      case "height":
        return this.height;
      case "weight":
        return this.weight;
      case "age":
        return this.age;
      default:
        throw new RuntimeException("Invalid Attribute");
    }
  }

  public String attrName (int onDimension){
    if (onDimension == 0) return "height";
    else if (onDimension == 1) return "weight";
    else return "age";
  }

  public HashMap<Integer, Double> dimensionToAttr(List<String> attributeNames){
    HashMap<Integer, Double> dToA = new HashMap<>();
    int i = 0;
    for (String attr : attributeNames) {
      dToA.put(i, attrFromName(attr));
    }
    return dToA;
  }
}
