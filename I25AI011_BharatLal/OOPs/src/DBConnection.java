package src.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class for MySQL database connection.
 * Update DB_URL, DB_USER, DB_PASSWORD before running.
 */
public class DBConnection {

    private static final String DB_URL  = "jdbc:mysql://localhost:3306/banking_system";
    private static final String DB_USER = "root";       // Change to your MySQL user
    private static final String DB_PASSWORD = "Bharat@121";       // Change to your MySQL password

    private static Connection connection = null;

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found. Add mysql-connector-j.jar to classpath.", e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
