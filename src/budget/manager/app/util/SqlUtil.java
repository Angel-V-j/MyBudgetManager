package budget.manager.app.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlUtil {
    public static int getLastTransactionId(Connection connection) {
        int lastId = -1; // Initialize with a default value (e.g., -1 for 'no transactions')
        String query = "SELECT MAX(id) AS last_id " +
                "FROM transactions";

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

    public static int getLastCategoryId(Connection connection) {
        int lastId = -2;
        String query = "SELECT MAX(id) AS last_id " +
                "FROM transactions";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                lastId = resultSet.getInt("last_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastId;
    }
}
