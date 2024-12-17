package budget.manager.app.controllers;

import budget.manager.app.managers.CSVReader;
import budget.manager.app.managers.CSVWriter;
import budget.manager.app.models.Currency;
import budget.manager.app.models.User;
import budget.manager.app.models.UserFactory;

import java.util.List;

import static budget.manager.app.util.FileUtil.*;

public class UserController {
    private static List<User> users = new CSVReader().read(getTextFilePath(USERS_FILE_NAME),USERS_FILE_NAME);

    public static List<User> getUsers() {
        return users;
    }

    public static boolean addUser(User user) {
        return users.add(user);
    }

    public static boolean addUser(String username, String password, Currency currency) {
        return users.add(new UserFactory().create(getUniqueId(users), username, password, currency));
    }

    public static boolean editUser(User user, char[] password) {
        if (user != null) {
            user.setPassword(String.valueOf(password));
            return true;
        }

        return false;
    }

    public static boolean editUser(User user, Currency currency) {
        if (user != null) {
            user.setCurrency(currency);
            return true;
        }

        return false;
    }

    public static boolean removeUser(User user) {
        return users.remove(user);
    }

    public static void saveUsers() {
        new CSVWriter().write(getUsers(), getTextFilePath(USERS_FILE_NAME));
    }

    public static User searchUserById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }

        return null;
    }

    public static User searchUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }
}