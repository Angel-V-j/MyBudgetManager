package budget.manager.app.models;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class UserFactory extends AbstractFactory<User> {

    public User create(int id, String username, String password, Currency currency) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setCurrency(currency);

        return user;
    }

    public User createFromCsv(String csvLine) {
        if (csvLine == null || csvLine.trim().isEmpty()) {
            return null;
        }
        try {
            String[] fields = csvLine.trim().split("\\s*,\\s*");
            int id = Integer.parseInt(fields[0]);
            String username = fields[1];
            String password = fields[2];
            Currency currency = Currency.valueOf(fields[3]);

            return create(id, username, password, currency);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Transaction create(int id, int userId, int categoryId, double amount, LocalDate date, String info) {
        return null;
    }

    @Override
    public Category create(int id, int userId, String name, boolean isIncome) {
        return null;
    }
}
