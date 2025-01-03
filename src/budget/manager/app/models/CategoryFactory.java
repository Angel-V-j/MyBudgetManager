package budget.manager.app.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class CategoryFactory extends AbstractFactory<Category>{

    @Override
    public Category create(int id, int userId, String name, boolean isIncome) {
        Category category = new Category();
        category.setId(id);
        category.setUserId(userId);
        category.setName(name);
        category.setIsIncome(isIncome);
        return category;
    }

    @Override
    public Category createFromCsv(String csvLine) {
        if (csvLine == null || csvLine.trim().isEmpty()) {
            return null;
        }
        try {
            String[] fields = csvLine.trim().split("\\s*,\\s*");
            int id = Integer.parseInt(fields[0]);
            int userId = Integer.parseInt(fields[1]);
            if (userId < 0) {
                return null;
            }

            String name = fields[2];
            boolean isIncome = Boolean.parseBoolean(fields[3]);
            return create(id, userId, name, isIncome);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Category createFromRSet(ResultSet resultSet) throws SQLException {
        return create(resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("name"),
                resultSet.getBoolean("is_income"));
    }


    @Override
    public User create(int id, String username, String password, String currency) {
        return null;
    }

    @Override
    public Transaction create(int id, int userId, int categoryId, double amount, String currency, LocalDate date, String info) {
        return null;
    }
}
