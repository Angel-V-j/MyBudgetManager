package budget.manager.app.controllers;

import budget.manager.app.managers.DatabaseManager;
import budget.manager.app.managers.SessionManager;
import budget.manager.app.models.Category;
import budget.manager.app.models.CategoryFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static budget.manager.app.util.SqlUtil.CATEGORY_TABLE_NAME;
import static budget.manager.app.util.SqlUtil.getLastId;

public class CategoryController {

    public CategoryController() {
    }

    public static boolean addCategory(Category category, List<Category> categories) {
        return addCategory(category.getUserId(), category.getName(), category.isIncome(), categories);
    }

    public static boolean addCategory(int id, String categoryName, boolean isIncome, List<Category> categories) {
        String query = "INSERT INTO categories (user_id, name, is_income) " +
                "VALUES (?, ?, ?)";

        try (Connection connection = DatabaseManager.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, categoryName);
            preparedStatement.setBoolean(3, isIncome);
            if (preparedStatement.executeUpdate() > 0) {
                categories.add(new CategoryFactory().create(getLastId(connection,CATEGORY_TABLE_NAME),
                        id, categoryName, isIncome));
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
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
        categories.remove(category);
        return removeCategory(category.getId(), categories);
    }

    public static boolean removeCategory(int id, List<Category> categories) {
        categories.removeIf(category -> category.getId() == id);
        return removeCategory(id);
    }

    public static void deleteUserCategories(List<Category> categories) {
        for (Category category : categories) {
            removeCategory(category.getId());
        }
    }

    private static boolean removeCategory(int id) {
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

    public static Category searchCategoryById(List<Category> categories, int id) {
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

    public static ArrayList<Category> loadCategoriesToList(int userId) {
        ArrayList<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categories " +
                "WHERE user_id = ? OR user_id = -1";

        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next())
                    categories.add(new CategoryFactory().createFromRSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    public static Category getCategoryById(int id) {
        String query = "SELECT * FROM categories " +
                "WHERE id = ?";

        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next())
                    return new CategoryFactory().createFromRSet(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}