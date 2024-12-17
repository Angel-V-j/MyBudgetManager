package budget.manager.app.controllers;

import budget.manager.app.managers.CSVReader;
import budget.manager.app.managers.CSVWriter;
import budget.manager.app.managers.SessionManager;
import budget.manager.app.models.Category;
import budget.manager.app.models.CategoryFactory;

import java.util.ArrayList;
import java.util.List;

import static budget.manager.app.util.FileUtil.*;

public class CategoryController {
    private static List<Category> categories = new CSVReader().read(getTextFilePath(CATEGORIES_FILE_NAME),CATEGORIES_FILE_NAME);

    public CategoryController() {
    }

    public static boolean addCategory(Category category) {
        if (categories.add(category)) {
            return SessionManager.getInstance().reloadUserCategories();
        }

        return false;
    }

    public static boolean addCategory(String categoryName, boolean isIncome) {
        if (categories.add(new CategoryFactory().create(getUniqueId(categories), SessionManager.
                getInstance().getCurrentUser().getId(), categoryName, isIncome))) {
            return SessionManager.getInstance().reloadUserCategories();
        }
        return false;
    }

    public static boolean editCategory(Category category, Category editedCategory) {
        if (category != null) {
            category.setName(editedCategory.getName());
            category.setIsIncome(editedCategory.isIncome());
            return SessionManager.getInstance().reloadUserCategories();
        }

        return false;
    }

    public static boolean removeCategory(Category category) {
        if(categories.remove(category)) {
            return SessionManager.getInstance().reloadUserCategories();
        }

        return false;
    }

    public static boolean removeCategoriesOfUser(int userId) {
        return categories.removeIf(category -> category.getUserId() == userId);
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

    public static ArrayList<Category> getCategories() {
        return (ArrayList<Category>) categories;
    }

    public static void saveCategories() {
        List<Category> categories = new ArrayList<>(getCategories().stream()
                .filter(c -> c.getUserId() >= 0)
                .toList());

        new CSVWriter().write(categories, getTextFilePath(CATEGORIES_FILE_NAME));
    }
}