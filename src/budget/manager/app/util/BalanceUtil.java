package budget.manager.app.util;

import budget.manager.app.managers.SessionManager;
import budget.manager.app.models.Transaction;

import java.time.LocalDate;
import java.util.List;

import static budget.manager.app.controllers.CategoryController.searchCategoryById;
import static budget.manager.app.controllers.TransactionController.searchTransactionFromToDate;


public class BalanceUtil {
    public static double calculateBalance(List<Transaction> transactionsBalance) {
	        return calculateIncome(transactionsBalance) - calculateExpenses(transactionsBalance);
    }

    public static double calculateIncome(List<Transaction> transactionsBalance) {
        return transactionsBalance.stream()
                .filter(t -> searchCategoryById(SessionManager.
                                getInstance().
                                getUserCategories(),
                        t.getCategoryId()).
                        isIncome())
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public static double calculateExpenses(List<Transaction> transactionsBalance) {
        return transactionsBalance.stream()
                .filter(t -> !searchCategoryById(SessionManager.
                                getInstance().
                                getUserCategories(),
                        t.getCategoryId()).
                        isIncome())
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public static double balanceForPeriod(List<Transaction> transactions, LocalDate startDate, LocalDate endDate) {
        return calculateBalance(
                searchTransactionFromToDate(transactions, startDate, endDate));
    }

    public static double incomeForPeriod(List<Transaction> transactions,LocalDate startDate, LocalDate endDate) {
        return calculateIncome(searchTransactionFromToDate(transactions, startDate, endDate));
    }

    public static double expensesForPeriod(List<Transaction> transactions,LocalDate startDate, LocalDate endDate) {
        return calculateExpenses(searchTransactionFromToDate(transactions, startDate, endDate));
    }
}