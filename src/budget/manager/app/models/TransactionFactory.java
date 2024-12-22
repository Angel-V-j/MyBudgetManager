package budget.manager.app.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static budget.manager.app.util.DateUtil.stringToDate;

public class TransactionFactory extends AbstractFactory<Transaction> {

    @Override
    public Transaction create(int id, int userId, int categoryId, double amount, String currency, LocalDate date, String info){
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setUserId(userId);
        transaction.setCategoryId(categoryId);
        transaction.setAmount(amount);
        transaction.setCurrency(Currency.valueOf(currency));
        transaction.setLocalDate(date);
        transaction.setDescription(info);
        return transaction;
    }

    @Override
    public Transaction createFromCsv(String csvLine) {
        if (csvLine == null || csvLine.trim().isEmpty()) {
            return null;
        }
        try {
            String[] fields = csvLine.trim().split("\\s*,\\s*");
            int id = Integer.parseInt(fields[0]);
            int userId = Integer.parseInt(fields[1]);
            int categoryId = Integer.parseInt(fields[2]);
            double amount = Double.parseDouble(fields[3]);
            String currency = fields[4];
            LocalDate date = stringToDate(fields[5]);
            String info = "";
            if (!csvLine.endsWith(",")) {
                info = fields[6];
            }

            return create(id, userId, categoryId, amount, currency, date, info);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Transaction createFromRSet(ResultSet resultSet) throws SQLException {
        return create(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getInt("category_id"),
                resultSet.getDouble("amount"),
                resultSet.getString("currency"),
                resultSet.getDate("date").toLocalDate(),
                resultSet.getString("description"));
    }

    @Override
    public User create(int id, String username, String password, String currency) {
        return null;
    }

    @Override
    public Category create(int id, int userId, String name, boolean isIncome) {
        return null;
    }
}
