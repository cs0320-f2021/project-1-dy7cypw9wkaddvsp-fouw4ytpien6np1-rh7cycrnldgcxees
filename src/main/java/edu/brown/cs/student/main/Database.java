package edu.brown.cs.student.main;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;

public class Database {

    private static Connection conn = null;
    private static List<String> words = new ArrayList<>();


    /**
     * Instantiates the database, creating tables if necessary.
     * Automatically loads files.
     *
     * @param filename file name of SQLite3 database to open.
     * @throws SQLException if an error occurs in any SQL query.
     */
    public Database(String filename) throws SQLException, ClassNotFoundException {
        // this line loads the driver manager class, and must be
        // present for everything else to work properly
        Class.forName("org.sqlite.JDBC");
        String urlToDB = "jdbc:sqlite:" + filename;
        Connection conn = DriverManager.getConnection(urlToDB);
        Database.conn = conn;

        Statement stat = conn.createStatement();
        stat.executeUpdate("PRAGMA foreign_keys=ON;");
        stat.close();
    }
    
    public void changePath(String filename) throws ClassNotFoundException, SQLException {
        conn = null;
        words = new ArrayList<>();

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
    public static void insert(Object object) throws RuntimeException, SQLException, IllegalAccessException {
        String tableName = object.getClass().getSimpleName(); // use reflection api to get class name
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
    public static void delete(Object object) throws SQLException, IllegalAccessException {
        String tableName = object.getClass().getSimpleName();
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
        String writtenStatement = "DELETE FROM Customers WHERE " + conditions + ";";
        PreparedStatement prep= conn.prepareStatement(writtenStatement);
        prep.executeUpdate();
        prep.close();
    }

    public static Collection<Object> where(String attributeName, String attributeContent) {
        return new ArrayList<Object>();
    }

    public void update() {
    }

    public void rawQuery(String sqlStatement) {
    }

    /**
     * Retrieves all words that should be used.
     *
     * @return words.
     */
    public List<String> getWords() {
        return words;
    }
}