package edu.brown.cs.student.main.table;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class to create User Objects from columns of Database
 */
public class Users implements ITable {
  private String user_id;
  private String weight;
  private String bust_size;
  private String height;
  private String age;
  private String body_type;
  private String horoscope;

  /**
   * Default constructor
   */
  public Users() {
  }

  /**
   * Default constructor
   */
  public Users testUsers(String user_id, String weight, String bust_size, String height,
                         String age, String body_type, String horoscope) {
    this.user_id = user_id;
    this.weight = weight;
    this.bust_size = bust_size;
    this.height = height;
    this.age = age;
    this.body_type = body_type;
    this.horoscope = horoscope;

    return this;
  }



  /**
   * Create instance of Object from columns of Result Set
   * @param rs - result set from Database
   * @return Object which is a User
   * @throws SQLException - if there is an issue reading the Database
   */
  @Override
  public Object createObject(ResultSet rs) throws SQLException {
    this.user_id = rs.getString("user_id");
    this.weight = rs.getString("weight");
    this.bust_size = rs.getString("bust_size");
    this.height = rs.getString("height");
    this.age = rs.getString("age");
    this.body_type = rs.getString("body_type");
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
    this.weight = rs.getString("weight");
    this.height = rs.getString("height");
    this.age = rs.getString("age");
    this.horoscope = rs.getString("horoscope");
    return this;
  }

  /**
   * Gets height of User
   * @return String representing height, i.e. "5' 11''"
   */
  public String getHeight() {
    return height;
  }

  /**
   * Gets weight of User
   * @return - String representing weight, i.e. "145lbs"
   */
  public String getWeight() {
    return weight;
  }

  /**
   * Gets Age of User
   * @return - String representing age
   */
  public String getAge() {
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
   *
   * @param attr
   * @return
   */
  public String getAttrByName(String attr) {
    if (attr.equals("height")) {
      return this.height;
    }
    else if (attr.equals("weight")) {
      return this.weight;
    }
    else if (attr.equals("age")) {
      return this.age;
    }
    else {
      throw new RuntimeException("Invalid Attribute");
    }
  }

  /**
   * Gets id of Users
   * @return - int representing user_id
   */
  public String getUser_id() {
    return user_id;
  }

  /**
   * Gets bust size of Users
   * @return - String representing bust_size
   */
  public String getBust_size() {
    return bust_size;
  }

  /**
   * Gets body type of Users
   * @return - String representing body type
   */
  public String getBody_type() {
    return body_type;
  }
}
