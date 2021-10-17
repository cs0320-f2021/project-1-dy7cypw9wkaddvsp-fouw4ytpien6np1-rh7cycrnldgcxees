package edu.brown.cs.student.orm;
import edu.brown.cs.student.main.CommandHashmap;
import edu.brown.cs.student.main.handlers.ICommandHandler;
import edu.brown.cs.student.orm.table.ITable;
import edu.brown.cs.student.orm.table.TableHashMap;
import edu.brown.cs.student.orm.table.Users;

import java.lang.reflect.*;
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
    private HashMap<String, ICommandHandler> keyWordMap;
    // private HashMap<String, ArrayList<ITable>> tableNameToObjs; // NEW

    /**
     * Default constructor
     *
     */
    public Database() {}

    public Database(CommandHashmap keyWordMap) {
        this.keyWordMap = keyWordMap.getMap();
    }

    /**
     * Method to set/reset connection with database to given filepath
     * @param filename - path to database
     * @throws ClassNotFoundException - error if class is not found
     * @throws SQLException - sql error for urlToDB
     */
    public void changePath(String filename) throws ClassNotFoundException, SQLException {
        conn = null;
        Class.forName("org.sqlite.JDBC"); // to load a class dynamically...classes share names of tables
        String urlToDB = "jdbc:sqlite:" + filename;
        Connection conn = DriverManager.getConnection(urlToDB);
        Database.conn = conn;

        Statement stat = conn.createStatement();
        stat.executeUpdate("PRAGMA foreign_keys=ON;");
        stat.close();
    }

    /**
     * A method to insert generic object into Database
     * @param object - Object to be inserted into Database
     */
    public void insert(Object object) throws RuntimeException, SQLException, IllegalAccessException {
        String tableName = object.getClass().getSimpleName().toLowerCase(); // use reflection api to get class name
        Field[] rawFields = object.getClass().getDeclaredFields();
        String tuple = "";
        for (int i = 0; i < rawFields.length; i++) {
            rawFields[i].setAccessible(true);
            String currentField = rawFields[i].getGenericType().getTypeName();
            if (currentField.equals("int") || currentField.equals("double")) {
                tuple += rawFields[i].get(object).toString();
            }
            else if (currentField.equals("java.lang.String")) {
                tuple += "'" + rawFields[i].get(object).toString() + "'";
            }
            else {throw new RuntimeException("Invalid Field Type");}
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
        for (int i = 0; i < rawFields.length; i++) {
            rawFields[i].setAccessible(true);

            Object currentContent = rawFields[i].get(object);
            String attrName = rawFields[i].getName();

            String currentField = rawFields[i].getGenericType().getTypeName();
            conditions += attrName + "=";
            if (currentField.equals("int") || currentField.equals("double")) {
                conditions += currentContent.toString();
            }
            else if (currentField.equals("java.lang.String")) {
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
    public Collection<? super Object> where(String attributeName, String attributeContent,
                               String tableName)
        throws SQLException {
        TableHashMap map = new TableHashMap();
        HashMap<String, ITable> tableMap = map.getMap();

        ArrayList<Object> objects = new ArrayList<>();
        String writtenStatement = "SELECT * FROM " + tableName + " WHERE " + attributeName + "=" + "'" + attributeContent + "'" + ";";
        PreparedStatement prep = conn.prepareStatement(writtenStatement);
        ResultSet rs = prep.executeQuery();
        while (rs.next()) {
            ITable table = tableMap.get(tableName);
            objects.add(table.populateFields(rs));
        }
        prep.close();
        return objects;
    }

    /**
     * Method is designed to take in a query and return a ResultSet for 1 object that
     * matches the query (SELECT)
     * @param attributeName - name of attr
     * @param attributeContent - content to match
     * @return ResultSet - a set of (usually) 1 ResultSet result
     * @throws SQLException
     */
    public ResultSet returnRSFromQuery(String attributeName, String attributeContent,
                                       String tableName) throws SQLException {
        String writtenStatement = "SELECT * FROM " + tableName + " WHERE " +
            attributeName + "=" + "'" + attributeContent + "'" + ";";
        PreparedStatement prep = conn.prepareStatement(writtenStatement);
        return prep.executeQuery();
    }

    /**
     * Method to select all rows of specific columns from a table
     * @param attributes - columns to return
     * @param tableName - table to extract form
     * @return Collection<Object> - A collection of objects
     * @throws SQLException - throws error if bad input (i.e. col name doesn't exist)
     */
    public Collection<Object> selectAll(List<String> attributes, String tableName)
            throws SQLException {
        // get hashmap of table name to Objects that implement ITable
        TableHashMap map = new TableHashMap();
        HashMap<String, ITable> tableMap = map.getMap();
        // get the list of attributes as a string:
        StringBuilder attributesString = new StringBuilder();
        for (int i = 0; i < attributes.size(); i++) {
            attributesString.append(attributes.get(i));
            if (i != attributes.size() - 1) {
                attributesString.append(", ");
            }
        }

        ArrayList<Object> objects = new ArrayList<>(); // output collection

        // put together SQL:
        String writtenStatement = "SELECT " + attributesString.toString() + " FROM " + tableName;
        PreparedStatement prep = conn.prepareStatement(writtenStatement); // prep statement
        ResultSet rs = prep.executeQuery(); // returns a ResultSet of SQL query
        while (rs.next()) { // create a new object of each result
            ITable table = tableMap.get(tableName.toLowerCase());
            objects.add(table.populateFields(rs));
        }
        prep.close();
        return objects;
    }

    /**
     * Method to select all rows of specific columns from a table
     * @return Collection<Object> - A collection of objects
     * @throws SQLException - throws error if bad input (i.e. col name doesn't exist)
     */
    public List<Users> selectAllUsers()
            throws SQLException {

        ArrayList<Users> objects = new ArrayList<>(); // output collection

        // put together SQL:
        String writtenStatement = "SELECT height, weight, age, horoscope FROM users";
        PreparedStatement prep = conn.prepareStatement(writtenStatement); // prep statement
        ResultSet rs = prep.executeQuery(); // returns a ResultSet of SQL query
        while (rs.next()) { // create a new object of each result
            Users u = new Users();
            objects.add(u.createUserObject(rs));
        }
        prep.close();
        return objects;
    }

    /**
     * Method to update given attribute of object in Database with new value
     * @param object - object to update data for
     * @param attributeName - attribute to update
     * @param newAttributeContent - new value for attribute
     * @throws SQLException - if issue with connecting to database
     */
    public void update(Object object, String attributeName, String newAttributeContent)
        throws SQLException, NoSuchFieldException, IllegalAccessException {
        String tableName = object.getClass().getSimpleName().toLowerCase();
        // need to set where condition to only update object in table with
        // all of the attrs in that object
        String attrsMatch = "";


        Field[] rawFields = object.getClass().getDeclaredFields();
        for (int i = 0; i < rawFields.length; i++) {
            rawFields[i].setAccessible(true);
            Field field = rawFields[i];
            Object value = field.get(object);

            String[] correctArray = field.toString().split("\\.");


            attrsMatch += correctArray[correctArray.length - 1] + "=" + "'" + value + "'";
            if (i != rawFields.length - 1) {
                attrsMatch += "AND ";
            }
        }
        String writtenStatement = "UPDATE " + tableName + " SET " + attributeName + "=" +
            newAttributeContent + " WHERE " + attrsMatch + ";";
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