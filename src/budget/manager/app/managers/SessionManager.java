package budget.manager.app.managers;
import budget.manager.app.models.Category;
import budget.manager.app.models.Transaction;
import budget.manager.app.models.User;

import java.util.ArrayList;

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
                userTransactions = loadTransactionsToList(currentUser.getId());
                userCategories = loadCategoriesToList(currentUser.getId());
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

    public ArrayList<Category> getUserCategories() {
        return userCategories;
    }
}

