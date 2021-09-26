package edu.brown.cs.student.main;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * A method to insert generic object into Databas
     * @param object - Object to be inserted into Database
     */
    public void insert(Object object) throws RuntimeException {
        String tableName = object.getSimpleName(); // use reflection api to get class name
        Field[] rawFields = object.getDeclaredFields();
        //String[] fieldNames = new String[](rawFields.length);
        //Object[] fieldContent = new Object[](rawFields.length);
        String tuple = "";
        for (int i = 0; i<rawFields.length; i++) {
            rawFields[i].setAccessible(true);

            //fieldNames[i] = rawFields[i].getName();  // name of field as string
            //fieldContent[i] = rawFields[i].get();  // object (content) of field
            Object currentContent = rawFields[i].get();


            // THIS vvv SEEMS WRONG BUT WE CAN FIX IT LATER
            if (rawFields[i].getGenericType() == int || rawFields[i].getGenericType() == double) {
                tuple += rawFields[i].get().toString;
            }
            else if (rawFields[i].getGenericType() == String) {
                tuple += "\'" + rawFields[i].get().toString + "\'";
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
    public void delete(Object object) {
        String tableName = object.getSimpleName();
        Field[] rawFields = object.getDeclaredFields();
        //String[] fieldNames = new String[](rawFields.length);
        Object[] fieldContent = new Object[](rawFields.length);
        String tuple = "";
        for (int i = 0; i<rawFields.length; i++) {
            rawFields[i].setAccessible(true);
            //fieldNames[i] = rawFields[i].getName();
            //fieldContent[i] = rawFields[i].get();  // object (content) of field


            Object currentContent = rawFields[i].get();
            String attrName = rawFields[i].getName();


            conditions += attrName + "=";
            // THIS vvv SEEMS WRONG BUT WE CAN FIX IT LATER
            if (rawFields[i].getGenericType() == int || rawFields[i].getGenericType() == double) {
                conditions += currentContent.toString;
            }
            else if (rawFields[i].getGenericType() == String) {
                conditions += "\'" + rawFields[i].get().toString + "\'";
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

    public Collection where(String attributeName, String attributeContent) {

    }

    public void update() {
    }

    public void rawQuery(String sqlStatement) {
    }




//    Map<String, Integer> getInstanceMap() throws SQLException {
//        Map<String, Integer> instMap = new HashMap<>();
//        //TODO: select the five most common words from the entire database, and how many times they appear
//        PreparedStatement prep = conn.prepareStatement(
//                "SELECT word, COUNT(*) FROM word GROUP BY word.word ORDER BY COUNT(*) DESC LIMIT 5;"); //Your SQL Here!
//        ResultSet rs = prep.executeQuery();
//        while (rs.next()) {
//            instMap.put(rs.getString(1), rs.getInt(2));
//        }
//
//        prep.close();
//        rs.close();
//        return instMap;
//    }

    /**
     * Retrieves all words that should be used.
     *
     * @return words.
     */
    public List<String> getWords() {
        return words;
    }
}