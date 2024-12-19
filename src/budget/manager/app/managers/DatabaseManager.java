package budget.manager.app.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static DatabaseManager instance;
    private static final String URL = "jdbc:mysql://localhost:3306/mybudgetmanager";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private Connection connection;

    private DatabaseManager() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
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

    public Connection getConnection() {
        return connection;
    }

}
