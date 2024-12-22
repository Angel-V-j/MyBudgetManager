package budget.manager.app.services.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static budget.manager.app.util.SqlUtil.*;

public class DatabaseInitializer {
    private static final String USER_TABLE_QUERY = """
                CREATE TABLE IF NOT EXISTS users (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    username CHAR(36) UNIQUE NOT NULL,
                                    user_password TEXT NOT NULL,
                                    currency CHAR(3) NOT NULL
                                );
                """;
    
    private static final String  CATEGORY_TABLE_QUERY = """
                CREATE TABLE IF NOT EXISTS categories (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    user_id INT NOT NULL,
                                    name CHAR(60) NOT NULL,
                                    is_income BOOLEAN NOT NULL,
                                    FOREIGN KEY (user_id)
                                        REFERENCES users (id)
                                );
               """;

    private static final String TRANSACTION_TABLE_QUERY = """
                CREATE TABLE IF NOT EXISTS transactions (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    user_id INT NOT NULL,
                                    category_id INT NOT NULL,
                                    amount DECIMAL NOT NULL,
                                    currency CHAR(3) NOT NULL,
                                    date DATE NOT NULL,
                                    description TEXT,
                                    FOREIGN KEY (user_id)
                                        REFERENCES users (id),
                                    FOREIGN KEY (category_id)
                                        REFERENCES categories (id)
                                );
                """;

    private static final String LOAD_DEFAULT_USER_QUERY = """
                INSERT INTO users (id, username, user_password, currency)
                        VALUES (-1, 'default', 'default', 'NaN');
                """;
    private static final String LOAD_DEFAULT_CATEGORIES_QUERY = """
                INSERT INTO categories (user_id, name, is_income)
                        VALUES
                            (-1, 'Salary', 1),
                            (-1, 'Bonus', 1),
                            (-1, 'Investments', 1),
                            (-1, 'Government Benefits', 1),
                            (-1, 'Gifts', 1),
                            (-1, 'Food & Groceries', 0),
                            (-1, 'Rent/Mortgage', 0),
                            (-1, 'Utilities', 0),
                            (-1, 'Cloths', 0),
                            (-1, 'Entertainment', 0),
                            (-1, 'Insurance', 0),
                            (-1, 'Debt Payment', 0),
                            (-1, 'HealthCare', 0);
                """;

    public static void initDb(Connection connection) throws SQLException {
        connection.createStatement().execute(USER_TABLE_QUERY);
        connection.createStatement().execute(CATEGORY_TABLE_QUERY);
        connection.createStatement().execute(TRANSACTION_TABLE_QUERY);
        if (getLastId(connection, CATEGORY_TABLE_NAME) == 0) {
            connection.createStatement().executeLargeUpdate(LOAD_DEFAULT_USER_QUERY);
            connection.createStatement().executeLargeUpdate(LOAD_DEFAULT_CATEGORIES_QUERY);
        }
    }
}
