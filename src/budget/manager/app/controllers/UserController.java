package budget.manager.app.controllers;

import budget.manager.app.managers.DatabaseManager;
import budget.manager.app.managers.SessionManager;
import budget.manager.app.models.Currency;
import budget.manager.app.models.User;
import budget.manager.app.models.UserFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static budget.manager.app.controllers.CategoryController.deleteUserCategories;
import static budget.manager.app.controllers.TransactionController.deleteUserTransactions;

public class UserController {

    public static boolean addUser(String username, String password, Currency currency) {
        String query = "INSERT INTO users (username, user_password, currency) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement =
                     DatabaseManager.getInstance().getConnection().prepareStatement(query)){
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, String.valueOf(currency));
            if (preparedStatement.executeUpdate() > 0)
                return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static boolean addUser(User user) {
        return addUser(user.getUsername(), user.getPassword(), user.getCurrency());
    }

    private static boolean editUser(User user, String column, String value) {
        String query = "UPDATE users " +
                "SET " + column + " = ? " +
                "WHERE id = ?";

        try (PreparedStatement preparedStatement =
                     DatabaseManager.getInstance().getConnection().prepareStatement(query)){
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, user.getId());
            if (preparedStatement.executeUpdate() > 0)
                return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static boolean editUser(User user, char[] password) {
        return editUser(user,"user_password", String.valueOf(password));
    }

    public static boolean editUser(User user, Currency currency) {
        return editUser(user,"currency", String.valueOf(currency));
    }

    public static boolean removeUser(User user) {
        String query = "DELETE FROM users " +
                "WHERE id = ?";

        int id = user.getId();
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            if (preparedStatement.executeUpdate() > 0) {
                deleteUserTransactions(SessionManager.getInstance().getUserTransactions());
                deleteUserCategories(SessionManager.getInstance().getUserCategories());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

//    public static User searchUserById(int id) {
//        String query = "SELECT * " +
//                "FROM users " +
//                "WHERE id = ?";
//
//        try (Connection connection = DatabaseManager.getInstance().getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setInt(1, SessionManager.getInstance().getCurrentUser().getId());
//
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                if (resultSet.next())
//                    return new UserFactory().createFromRSet(resultSet);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    public static User searchUserByUsername(String username) {
        String query = "SELECT * " +
                "FROM users " +
                "WHERE username = ?";

        try (PreparedStatement preparedStatement =
                     DatabaseManager.getInstance().getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next())
                    return new UserFactory().createFromRSet(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}