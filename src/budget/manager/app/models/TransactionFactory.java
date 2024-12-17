package budget.manager.app.models;

import budget.manager.app.managers.SessionManager;

import java.time.LocalDate;
import java.util.Arrays;

import static budget.manager.app.util.DateUtil.stringToDate;

public class TransactionFactory extends AbstractFactory<Transaction> {

    @Override
    public Transaction create(int id, int userId, int categoryId, double amount, LocalDate date, String info){
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setUserId(userId);
        transaction.setCategoryId(categoryId);
        transaction.setAmount(amount);
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
            LocalDate date = stringToDate(fields[4]);
            String info = "";
            if (!csvLine.endsWith(",")) {
                info = fields[5];
            }

            return create(id, userId, categoryId, amount, date, info);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User create(int id, String username, String password, Currency currency) {
        return null;
    }

    @Override
    public Category create(int id, int userId, String name, boolean isIncome) {
        return null;
    }
}
