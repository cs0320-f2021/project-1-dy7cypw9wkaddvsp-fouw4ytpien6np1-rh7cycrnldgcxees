package edu.brown.cs.student.main;

import edu.brown.cs.student.main.orm.Database;
import edu.brown.cs.student.main.orm.table.Users;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DatabaseTest {

  @Test
  public void testChangePath() throws SQLException, ClassNotFoundException {
    Database database = new Database();
    database.changePath("data/project-1/runwaySMALL.sqlite3");
  }

  @Test
  public void testInsert() throws SQLException, ClassNotFoundException, IllegalAccessException {
    Database database = new Database();
    database.changePath("data/project-1/runwaySMALL.sqlite3");
    Users user1 = new Users();
    user1.testUsers("55493", "145lbs", "32a", "5",
        "24", "slim", "Capricorn");
    database.insert(user1); // check to see if this doesn't fail
    Collection<Object> output1 = database.where("body_type", "slim",
        "users");
    Object[] output1Array = output1.toArray();
    Users user2 = (Users) output1Array[0];
    assertEquals("slim", user2.getBody_type());
    database.delete(user1);

  }

  @Test
  public void testDelete() throws SQLException, ClassNotFoundException, IllegalAccessException {
    Database database = new Database();
    database.changePath("data/project-1/runwaySMALL.sqlite3");
    Users user1 = new Users();
    user1.testUsers("55493", "145lbs", "32a", "5",
        "24", "slim", "Capricorn");
    database.insert(user1);
    database.delete(user1);
    Collection<Object> output1 = database.where("body_type", "slim",
        "users");
    Object[] output1Array = output1.toArray();
    assertEquals(output1Array.length, 0);
  }

  @Test
  public void testWhere() throws SQLException, ClassNotFoundException {
    Database database = new Database();
    database.changePath("data/project-1/runwaySMALL.sqlite3");
    Collection<Object> output1 = database.where("body_type", "athletic",
        "users");
    Object[] output1Array = output1.toArray();
    assertEquals(output1Array.length, 4); // 4 outputs expected

  }

  @Test
  public void testSelectAll() throws SQLException, ClassNotFoundException {
    Database database = new Database();
    database.changePath("data/project-1/runwaySMALL.sqlite3");
    ArrayList<String> list1 = new ArrayList<>();
    list1.add("*");
    Collection<Object> list2 = database.selectAll(list1, "users");
    Object[] output1Array = list2.toArray();
    assertEquals(output1Array.length, 15); // 4 outputs expected

  }

  @Test
  public void testSelectAllUsers() throws SQLException, ClassNotFoundException {
    Database database = new Database();
    database.changePath("data/project-1/runwaySMALL.sqlite3");
    List<Users> users = database.selectAllUsers();
    Object[] output1Array = users.toArray();
    assertEquals(output1Array.length, 15); // 15 outputs expected
  }

  @Test
  public void testUpdate()
      throws SQLException, ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
    Database database = new Database();
    database.changePath("data/project-1/runwaySMALL.sqlite3");
    Users user1 = new Users();
    user1.testUsers("55493", "145lbs", "32a", "5",
        "24", "slim", "Capricorn");
    database.insert(user1); // check to see if this doesn't fail
    database.update(user1, "age", "25");
    Collection<Object> output1 = database.where("body_type", "slim",
        "users");
    Object[] output1Array = output1.toArray();
    Users user2 = (Users) output1Array[0];
    assertEquals("25", user2.getAge());
    database.delete(user2);
  }

  @Test
  public void testRawQuery() {

  }

}
