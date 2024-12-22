package budget.manager.app.managers;

import java.sql.*;

import static budget.manager.app.services.db.DatabaseInitializer.initDb;

public class DatabaseManager {
    private static DatabaseManager instance;
    private static final String URL = "jdbc:mysql://localhost:3306/mybudgetmanager";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static Connection connection;

    private DatabaseManager() {
        try {
            connection = getConnection();
            initDb(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed())
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        return connection;
    }
}
