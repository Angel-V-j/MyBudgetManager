package budget.manager.app.managers;

import java.sql.*;

public class DatabaseManager {
    private static DatabaseManager instance;
    private static final String URL = "jdbc:mysql://localhost:3306/mybudgetmanager";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private Connection connection;

    private DatabaseManager() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.createStatement().execute(createUserTableQuery());
            connection.createStatement().execute(createCategoryTableQuery());
            connection.createStatement().execute(createTransactionTableQuery());
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

    private String createUserTableQuery() {
        return """
                CREATE TABLE IF NOT EXISTS users (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    username CHAR(36) UNIQUE NOT NULL,
                                    user_password TEXT NOT NULL,
                                    currency CHAR(3) NOT NULL
                                );
                """;
    }

    private String createCategoryTableQuery() {
        return """
                CREATE TABLE IF NOT EXISTS categories (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    user_id INT NOT NULL,
                                    name CHAR(60) NOT NULL,
                                    is_income BOOLEAN NOT NULL,
                                    FOREIGN KEY (user_id)
                                        REFERENCES users (id)
                                );
               """;
    }

    private String createTransactionTableQuery() {
        return """
                CREATE TABLE IF NOT EXISTS transactions (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    user_id INT NOT NULL,
                                    category_id INT NOT NULL,
                                    amount DECIMAL NOT NULL,
                                    currency CHAR(2) NOT NULL,
                                    date DATE NOT NULL,
                                    description TEXT,
                                    FOREIGN KEY (user_id)
                                        REFERENCES users (id),
                                    FOREIGN KEY (category_id)
                                        REFERENCES categories (id)
                                );
                """;
    }

    public static void main(String[] args) {
        String query = "SELECT username FROM users WHERE id = ?";

        // ID of the user to fetch
        int userId = 1; // Replace with your desired ID
        DatabaseManager databaseManager = DatabaseManager.getInstance();

        try (Connection connection = databaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameter for the query
            preparedStatement.setInt(1, userId);

            // Execute the query and process the result
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getObject("username").toString();
                    System.out.println("Username: " + username);
                } else {
                    System.out.println("No user found with ID: " + userId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
