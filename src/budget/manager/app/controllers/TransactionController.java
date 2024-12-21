package budget.manager.app.controllers;

import budget.manager.app.managers.DatabaseManager;
import budget.manager.app.managers.SessionManager;
import budget.manager.app.models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static budget.manager.app.controllers.CategoryController.searchCategoryByName;
import static budget.manager.app.util.SqlUtil.getLastTransactionId;

public class TransactionController {
    public TransactionController() {
    }

    public static boolean addTransaction(int userId, int categoryId, double amount, Currency currency,
                                         LocalDate date, String description, List<Transaction> transactions) {
        String query = "INSERT INTO transactions (user_id, category_id, amount, currency, date, description) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, categoryId);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setString(4, String.valueOf(currency));
            preparedStatement.setDate(5, java.sql.Date.valueOf(date));
            preparedStatement.setString(6, description);
            if (preparedStatement.executeUpdate() > 0) {
                transactions.add(new TransactionFactory().create(getLastTransactionId(connection), userId,
                        categoryId, amount,
                        String.valueOf(currency), date,
                        description));
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static boolean addTransaction(Transaction transaction, List<Transaction> transactions) {
        return addTransaction(transaction.getUserId(), transaction.getCategoryId(),
                transaction.getAmount(), transaction.getCurrency(),
                transaction.getLocalDate(), transaction.getDescription(),
                transactions);
    }

    public static boolean editTransaction(Transaction transaction, Transaction editedTransaction) {
        transaction.setCategoryId(editedTransaction.getCategoryId());
        transaction.setAmount(editedTransaction.getAmount());
        transaction.setLocalDate(editedTransaction.getLocalDate());
        transaction.setDescription(editedTransaction.getDescription());

        String query = "UPDATE transactions " +
                "SET category_id = ?, amount = ?, date = ?, description = ? " +
                "WHERE id = ?";

        try (PreparedStatement preparedStatement =
                     DatabaseManager.getInstance().getConnection().prepareStatement(query)){
            preparedStatement.setInt(1, transaction.getCategoryId());
            preparedStatement.setDouble(2, transaction.getAmount());
            preparedStatement.setDate(3, java.sql.Date.valueOf(transaction.getLocalDate()));
            preparedStatement.setString(4, transaction.getDescription());
            preparedStatement.setInt(5, transaction.getId());
            if (preparedStatement.executeUpdate() > 0)
                return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static boolean removeTransaction(int id, List<Transaction> transactions) {
        transactions.removeIf(transaction -> transaction.getId() == id);

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

    public static boolean removeTransaction(Transaction transaction, List<Transaction> transactions) {
        return removeTransaction(transaction.getId(), transactions);
    }

//    public static boolean removeTransactionsOfUser(int userId, List<Transaction> transactions) {
//        return transactions.removeIf(transaction -> transaction.getUserId() == userId);
//    }

    public static Transaction searchTransactionById(int id, List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (transaction.getId() == id) {
                return transaction;
            }
        }

        return null;
    }

    public static ArrayList<Transaction> searchTransactionByCategory(String categoryName, List<Transaction> transactions, List<Category> categories) {
        ArrayList<Transaction> transactionsByCategory = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getCategoryId() == searchCategoryByName(categories, categoryName).getId()) {
                transactionsByCategory.add(transaction);
            }
        }

        return transactionsByCategory;
    }

    public static ArrayList<Transaction> searchTransactionByDate(List<Transaction> transactions, LocalDate date) {
        ArrayList<Transaction> transactionsFromDate = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getLocalDate().isEqual(date)) {
                transactionsFromDate.add(transaction);
            }
        }

        return transactionsFromDate;
    }

    public static ArrayList<Transaction> searchTransactionByMonth(List<Transaction> transactions, int month) {
        ArrayList<Transaction> transactionsFromMonth = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getLocalDate().getMonthValue() == month) {
                transactionsFromMonth.add(transaction);
            }
        }

        return transactionsFromMonth;
    }

    public static ArrayList<Transaction> searchTransactionByMonth(List<Transaction> transactions, Month month) {
        ArrayList<Transaction> transactionsFromMonth = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getLocalDate().getMonth() == month) {
                transactionsFromMonth.add(transaction);
            }
        }

        return transactionsFromMonth;
    }

    public static ArrayList<Transaction> searchTransactionFromToDate(List<Transaction> transactions, LocalDate fromDate, LocalDate toDate) {
        ArrayList<Transaction> transactionsFromToDate = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if ((transaction.getLocalDate().equals(fromDate) || transaction.getLocalDate().isAfter(fromDate)) &&
                    (transaction.getLocalDate().equals(toDate) || transaction.getLocalDate().isBefore(toDate))) {
                transactionsFromToDate.add(transaction);
            }
        }

        return transactionsFromToDate;
    }

    public static ArrayList<Transaction> loadTransactionsToList() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE user_id = ?";

        try (Connection connection = DatabaseManager.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, SessionManager.getInstance().getCurrentUser().getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next())
                    transactions.add(new TransactionFactory().createFromRSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }
}