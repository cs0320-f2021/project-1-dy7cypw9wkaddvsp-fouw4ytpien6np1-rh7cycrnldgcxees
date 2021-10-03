package edu.brown.cs.student.main;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * Class to interact with SQLite database file in Java with simple commands
 */
public class Database {
    /**
     * Connection to database
     */
    private static Connection conn = null;


    /**
     * Default constructor
     *
     */
    public Database() {
    }

    /**
     * Method to set/reset connection with database to given filepath
     * @param filename
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void changePath(String filename) throws ClassNotFoundException, SQLException {
        conn = null;

        Class.forName("org.sqlite.JDBC");
        String urlToDB = "jdbc:sqlite:" + filename;
        Connection conn = DriverManager.getConnection(urlToDB);
        Database.conn = conn;

        Statement stat = conn.createStatement();
        stat.executeUpdate("PRAGMA foreign_keys=ON;");
        stat.close();
    }
    /**
     * A method to insert generic object into Databas
     * @param object - Object to be inserted into Database
     */
    public void insert(Object object) throws RuntimeException, SQLException, IllegalAccessException {
        String tableName = object.getClass().getSimpleName().toLowerCase(); // use reflection api to get class name
        Field[] rawFields = object.getClass().getDeclaredFields();
        //String[] fieldNames = new String[](rawFields.length);
        //Object[] fieldContent = new Object[](rawFields.length);
        String tuple = "";
        for (int i = 0; i<rawFields.length; i++) {
            rawFields[i].setAccessible(true);

            //fieldNames[i] = rawFields[i].getName();  // name of field as string
            //fieldContent[i] = rawFields[i].get();  // object (content) of field
            Object currentContent = rawFields[i].get(object);


            // THIS vvv SEEMS WRONG BUT WE CAN FIX IT LATER
            if (rawFields[i].getGenericType().getTypeName().equals("int") || rawFields[i].getGenericType().getTypeName().equals("double")) {
                tuple += rawFields[i].get(object).toString();
            }
            else if (rawFields[i].getGenericType().getTypeName().equals("String")) {
                tuple += "'" + rawFields[i].get(object).toString() + "'";
            }
            else {
                throw new RuntimeException("Invalid Field Type");
            }
            if (i != rawFields.length - 1) {
                tuple += ", ";
            }
        }

        String writtenStatement = "INSERT INTO " + tableName + " VALUES (" + tuple + ");";
        // write SQL statement
        PreparedStatement prep = conn.prepareStatement(writtenStatement);
        prep.executeUpdate();
        prep.close();
    }

    /**
     * Deletes given object from Database
     * @param object - object to be deleted from Database
     */
    public void delete(Object object) throws SQLException, IllegalAccessException {
        String tableName = object.getClass().getSimpleName().toLowerCase();
        Field[] rawFields = object.getClass().getDeclaredFields();
        //String[] fieldNames = new String[](rawFields.length);

        Object[] fieldContent = new Object[rawFields.length];
        String tuple = "";
        String conditions = "";
        for (int i = 0; i<rawFields.length; i++) {
            rawFields[i].setAccessible(true);
            //fieldNames[i] = rawFields[i].getName();
            //fieldContent[i] = rawFields[i].get();  // object (content) of field


            Object currentContent = rawFields[i].get(object);
            String attrName = rawFields[i].getName();


            conditions += attrName + "=";
            // THIS vvv SEEMS WRONG BUT WE CAN FIX IT LATER
            if (rawFields[i].getGenericType().getTypeName().equals("int") || rawFields[i].getGenericType().getTypeName().equals("double")) {
                conditions += currentContent.toString();
            }
            else if (rawFields[i].getGenericType().getTypeName().equals("String")) {
                conditions += "'" + rawFields[i].get(object).toString() + "'";
            }
            else {
                throw new RuntimeException("Invalid Field Type");
            }
            if (i != rawFields.length - 1) {
                conditions += " AND ";
            }
        }
        String writtenStatement = "DELETE FROM " + tableName + " WHERE " + conditions + ";";
        PreparedStatement prep = conn.prepareStatement(writtenStatement);
        prep.executeUpdate();
        prep.close();
    }

    /**
     * Selects All rows from given table which match value for given attribute
     * @param attributeName - name of attribute to check
     * @param attributeContent - value of attribute to check against
     * @param tableName - name of table to retrieve from
     * @return Collection of objects containing all objects in table which match query
     * @throws SQLException - if issue with Database connection
     */
    public Collection<Object> where(String attributeName, String attributeContent, String tableName)
        throws SQLException {
        String writtenStatement = "SELECT * FROM " + tableName + " WHERE " + attributeName + "=" +
            attributeContent + ";";
        PreparedStatement prep = conn.prepareStatement(writtenStatement);
        prep.executeUpdate();
        prep.close();

        //PROBLEM -> how do we the list of rows from our prepared statement so that we can return them?
        return new ArrayList<Object>();
    }

    /**
     * Method to select all rows from a given table
     * @param attributes
     * @param tableName
     * @return
     * @throws SQLException
     */
    public Collection<Object> selectALl(List<String> attributes, String tableName)
            throws SQLException {
        String attrNames = "";
        for (int i = 0; i<attributes.size(); i++) {
            attrNames += attributes.get(i);
            if (i != attributes.size() - 1) {
                attrNames += ", ";
            }
        }
        String writtenStatement = "SELECT " + attrNames + " FROM " + tableName;
        PreparedStatement prep = conn.prepareStatement(writtenStatement);
        prep.executeUpdate();


        /* possible solution?

        ResultSet resultSet = prep.executeQuery();
        while (resultSet.next()) {

            int id = resultSet.getuser_id("ID");
            String name = resultSet.getString("NAME");
            BigDecimal salary = resultSet.getBigDecimal("SALARY");
            Timestamp createdDate = resultSet.getTimestamp("CREATED_DATE");

            Employee obj = new Employee();
            obj.setId(id);
            obj.setName(name);
            obj.setSalary(salary);
        }
         */

        prep.close();

        //PROBLEM -> how do we the list of rows from our prepared statement so that we can return them?
        return new ArrayList<Object>();
    }

    /**
     * Method to update given attribute of object in Database with new value
     * @param object - object to update data for
     * @param attributeName - attribute to update
     * @param newAttributeContent - new value for attribute
     * @throws SQLException - if issue with connecting to database
     */
    public void update(Object object, String attributeName, String newAttributeContent)
        throws SQLException {
        String tableName = object.getClass().getSimpleName().toLowerCase();
        // need to set where condition to only update object in table with
        // all of the attrs in that object
        String writtenStatement = "UPDATE " + tableName + " SET " + attributeName + "=" +
            newAttributeContent + ";";
        PreparedStatement prep = conn.prepareStatement(writtenStatement);
        prep.executeUpdate();
        prep.close();
    }

    // Help from TA: what should be the return type on this?
    public void rawQuery(String sqlStatement) throws SQLException {
        PreparedStatement prep = conn.prepareStatement(sqlStatement);
        prep.executeUpdate();
        prep.close();
    }

}