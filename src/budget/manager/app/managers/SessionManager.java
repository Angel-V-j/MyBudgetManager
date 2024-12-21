package budget.manager.app.managers;
import budget.manager.app.models.Category;
import budget.manager.app.models.CategoryFactory;
import budget.manager.app.models.Transaction;
import budget.manager.app.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import static budget.manager.app.controllers.CategoryController.loadCategoriesToList;
import static budget.manager.app.controllers.TransactionController.loadTransactionsToList;
import static budget.manager.app.controllers.UserController.*;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private ArrayList<Transaction> userTransactions;
    private ArrayList<Category> userCategories;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }

        return instance;
    }

    public boolean login(String username, String password) {
        currentUser = searchUserByUsername(username);
        if (currentUser != null) {
            if(currentUser.getPassword().equals(password)){
                userTransactions = loadTransactionsToList();
                userCategories = loadCategoriesToList();
                return true;
            }
        }

        return false;
    }

    public void logout() {
        currentUser = null;
        userTransactions = null;
        userCategories = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public ArrayList<Transaction> getUserTransactions() {
        return userTransactions;
    }

    public List<Category> getUserCategories() {
        return userCategories;
    }

    private ArrayList<Category> setDefaultCategories() {
        CategoryFactory factory = new CategoryFactory();
        return new ArrayList<Category>(Arrays.asList(factory.create(0,-1,"Salary", true),
                factory.create(1,-1,"Bonus", true),
                factory.create(2,-1,"Investments", true),
                factory.create(3,-1,"Government Benefits", true),
                factory.create(4,-1,"Gifts", true),
                factory.create(5,-1,"Food & Groceries", false),
                factory.create(6,-1,"Rent/Mortgage", false),
                factory.create(7,-1,"Utilities", false),
                factory.create(8,-1,"Cloths", false),
                factory.create(9,-1,"Entertainment", false),
                factory.create(10,-1,"Insurance", false),
                factory.create(11,-1,"Debt Payment", false),
                factory.create(12,-1,"HealthCare", false)));
    }
}

