package budget.manager.app.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserFactory extends AbstractFactory<User> {

    public User create(int id, String username, String password, String currency) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setCurrency(Currency.valueOf(currency));

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
            String currency = fields[3];

            return create(id, username, password, currency);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User createFromRSet(ResultSet resultSet) throws SQLException {
        return create(resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("user_password"),
                resultSet.getString("currency"));
    }

    @Override
    public Transaction create(int id, int userId, int categoryId, double amount, String currency, LocalDate date, String info) {
        return null;
    }

    @Override
    public Category create(int id, int userId, String name, boolean isIncome) {
        return null;
    }
}
