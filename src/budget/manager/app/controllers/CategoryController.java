package budget.manager.app.controllers;

import budget.manager.app.managers.DatabaseManager;
import budget.manager.app.models.Transaction;
import budget.manager.app.models.TransactionFactory;
import budget.manager.app.services.csv.CSVReader;
import budget.manager.app.services.csv.CSVWriter;
import budget.manager.app.managers.SessionManager;
import budget.manager.app.models.Category;
import budget.manager.app.models.CategoryFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static budget.manager.app.util.FileUtil.*;

public class CategoryController {

    public CategoryController() {
    }

    public static boolean addCategory(Category category, List<Category> categories) {
        categories.add(category);
        return saveCategory(category);
    }

    public static boolean addCategory(String categoryName, boolean isIncome, List<Category> categories) {
        categories.add(new CategoryFactory().create(getUniqueId(categories), SessionManager.
                getInstance().getCurrentUser().getId(), categoryName, isIncome));
        return saveCategory(categories.getLast());
    }

    public static boolean editCategory(Category category, Category editedCategory) {
        category.setName(editedCategory.getName());
        category.setIsIncome(editedCategory.isIncome());

        String query = "UPDATE categories " +
                "SET name = ?, is_income = ? " +
                "WHERE id = ?";

        int id = category.getId();
        String name = category.getName();
        boolean isIncome = category.isIncome();

        try (PreparedStatement preparedStatement = DatabaseManager.getInstance().getConnection().prepareStatement(query)){
            preparedStatement.setString(1, name);
            preparedStatement.setBoolean(2, isIncome);
            preparedStatement.setInt(3, id);
            if (preparedStatement.executeUpdate() > 0)
                return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static boolean removeCategory(Category category, List<Category> categories) {
        return removeCategory(category.getId(), categories);
    }

    public static boolean removeCategory(int id, List<Category> categories) {
        categories.removeIf(category -> category.getId() == id);

        String query = "DELETE FROM categories " +
                "WHERE id = ?";

        try (PreparedStatement preparedStatement = DatabaseManager.getInstance().getConnection().prepareStatement(query)){
            preparedStatement.setInt(1, id);
            if (preparedStatement.executeUpdate() > 0)
                return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static boolean removeCategory(String categoryName, List<Category> categories) {
        categories.removeIf(category -> category.getName().equals(categoryName));

        String query = "DELETE FROM categories " +
                "WHERE name = ?";

        try (PreparedStatement preparedStatement = DatabaseManager.getInstance().getConnection().prepareStatement(query)){
            preparedStatement.setString(1, categoryName);
            if (preparedStatement.executeUpdate() > 0)
                return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

//    public static boolean removeCategoriesOfUser(int userId) {
//        return categories.removeIf(category -> category.getUserId() == userId);
//    }

    public static Category searchCategoryById(List<Category> categories, int id) {
//        String query = "SELECT username FROM categories WHERE id = ?";
//
//        try (PreparedStatement preparedStatement = DatabaseManager.getInstance().getConnection().prepareStatement(query)) {
//
//            preparedStatement.setInt(1, id);
//
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                if (resultSet.next()) {
//                    String username = resultSet.getObject("username").toString();
//                    System.out.println("Username: " + username);
//                } else {
//                    System.out.println("No user found with ID: " + userId);
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        for (Category category : categories) {
            if (category.getId() == id) {
                return category;
            }
        }

        return null;
    }

    public static Category searchCategoryByName(List<Category> categories, String categoryName) {
        for (Category category : categories) {
            if (category.getName().equals(categoryName)) {
                return category;
            }
        }

        return null;
    }

    public static ArrayList<Category> searchCategoryByType(List<Category> categories, boolean type) {
        ArrayList<Category> types = new ArrayList<>();
        for (Category category : categories) {
            if (category.isIncome() == type) {
                types.add(category);
            }
        }

        return types;
    }

    public static boolean saveCategory(Category category) {
        String query = "INSERT INTO categories (user_id, name, is_income) " +
                "VALUES (?, ?, ?)";
        int userId = category.getUserId();
        String name = category.getName();
        boolean isIncome = category.isIncome();

        try (PreparedStatement preparedStatement = DatabaseManager.getInstance().getConnection().prepareStatement(query)){
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, name);
            preparedStatement.setBoolean(3, isIncome);
            if (preparedStatement.executeUpdate() > 0)
                return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static ArrayList<Category> loadCategoriesToList() {
        ArrayList<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categories WHERE user_id = ?";

        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, SessionManager.getInstance().getCurrentUser().getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next())
                    categories.add(new CategoryFactory().createFromRSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }
}