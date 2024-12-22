package budget.manager.app.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlUtil {
    public static final int NOT_VALID_ID = -2;
    public static final String TRANSACTION_TABLE_NAME = "transactions";
    public static final String CATEGORY_TABLE_NAME = "categories";
    public static final String USER_TABLE_NAME = "users";

    public static int getLastId(Connection connection, String tableName) {
        int lastId = NOT_VALID_ID; // Initialize with a default value (e.g., -1 for 'no transactions')
        String query = "SELECT MAX(id) AS last_id " +
                "FROM " + tableName;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                lastId = resultSet.getInt("last_id"); // Retrieve the last ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastId;
    }
}
