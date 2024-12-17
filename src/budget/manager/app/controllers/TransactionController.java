package budget.manager.app.controllers;

import budget.manager.app.managers.CSVReader;
import budget.manager.app.managers.CSVWriter;
import budget.manager.app.managers.SessionManager;
import budget.manager.app.models.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static budget.manager.app.controllers.CategoryController.searchCategoryByName;
import static budget.manager.app.util.FileUtil.*;

public class TransactionController {
    private static List<Transaction> transactions = new CSVReader().read(getTextFilePath(TRANSACTIONS_FILE_NAME),TRANSACTIONS_FILE_NAME);
    public TransactionController() {
    }

    public static boolean addTransaction(Transaction transaction) {
        if(transactions.add(transaction)) {
           return SessionManager.getInstance().reloadUserTransactions();
        }

        return false;
    }

    public static boolean addTransaction(int userId, int categoryId, double amount, LocalDate date, String description) {
        if(transactions.add(new TransactionFactory().create(getUniqueId(transactions), userId, categoryId, amount, date, description))) {
            return SessionManager.getInstance().reloadUserTransactions();
        }
        return false;
    }

    public static boolean updateTransaction(Transaction transaction, Transaction editedTransaction) {
        if (transaction != null) {
            transaction.setCategoryId(editedTransaction.getCategoryId());
            transaction.setAmount(editedTransaction.getAmount());
            transaction.setLocalDate(editedTransaction.getLocalDate());
            transaction.setDescription(editedTransaction.getDescription());
            return SessionManager.getInstance().reloadUserTransactions();
        }

        return false;
    }

    public static boolean removeTransaction(Transaction transaction) {
        if(transactions.remove(transaction)) {
            return SessionManager.getInstance().reloadUserTransactions();
        }

        return false;
    }

    public static boolean removeTransactionsOfUser(int userId) {
        return transactions.removeIf(transaction -> transaction.getUserId() == userId);
    }

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

    public static void saveTransactions() {
        new CSVWriter().write(transactions, getTextFilePath(TRANSACTIONS_FILE_NAME));
    }

    public static ArrayList<Transaction> getTransactions() {
        return (ArrayList<Transaction>) transactions;
    }
}