package edu.brown.cs.student.main;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DatabaseTest {

  @Test
  public void testChangePath() {
    Database database = new Database();
    database.changePath("data/project-1/justusersSMALL.sqlite3") // test if this does not error
  }

  @Test
  public void testInsert() {
    Database database = new Database();
    Users user1 = new Users();
  }

  @Test
  public void testDelete() {

  }

  @Test
  public void testWhere() {

  }

  @Test
  public void testSelectAll() {

  }

  @Test
  public void testUpdate() {

  }

  @Test
  public void testRawQuery() {

  }

}
