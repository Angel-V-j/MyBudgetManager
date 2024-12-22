package budget.manager.app.models;

import budget.manager.app.managers.SessionManager;

import javax.swing.table.AbstractTableModel;

import static budget.manager.app.controllers.CategoryController.searchCategoryById;
import static budget.manager.app.util.DateUtil.dateToString;

public class TransactionTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Id", "Category", "Amount", "Currency", "Date", "Description"};

    public TransactionTableModel() {}

    @Override
    public int getRowCount() {
        return SessionManager.getInstance().getUserTransactions().size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Transaction transaction = SessionManager.getInstance().getUserTransactions().get(rowIndex);
        return switch (columnIndex) {
            case 0 -> transaction.getId();
            case 1 -> transaction.getCategoryId() + "-" +
                    searchCategoryById(SessionManager.getInstance().getUserCategories(), transaction.getCategoryId()).getName();
            case 2 -> transaction.getAmount();
            case 3 -> transaction.getCurrency().name();
            case 4 -> dateToString(transaction.getLocalDate());
            case 5 -> transaction.getDescription();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
