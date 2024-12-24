package budget.manager.app.services.csv;

import budget.manager.app.forms.CategoryCreator;
import budget.manager.app.managers.SessionManager;
import budget.manager.app.models.Category;
import budget.manager.app.models.Transaction;
import budget.manager.app.services.csv.interfaces.Importer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import static budget.manager.app.controllers.CategoryController.*;
import static budget.manager.app.controllers.TransactionController.addTransaction;
import static budget.manager.app.util.SqlUtil.DEFAULT_USER_ID;
import static javax.swing.JOptionPane.*;

public class DataImporter implements Importer {
    public static void importData(String importType) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a CSV file to import");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                ArrayList<Serializable> list = (ArrayList<Serializable>) new CSVReader().read(selectedFile.
                        getAbsolutePath(), importType);
                int userId = SessionManager.getInstance().getCurrentUser().getId();
                for (Serializable serializable : list) {
                    if (serializable instanceof Transaction transaction) {
                        int categoryId = transaction.getCategoryId();
                        transaction.setUserId(userId);
                        if (SessionManager.getInstance().getUserCategories().stream().anyMatch(item -> item.getId() == categoryId)) {
                            addTransaction(transaction, SessionManager.getInstance().getUserTransactions());
                        } else {
                            if (showConfirmDialog(null, "You do not have category associated with transaction:\n"
                                            + transaction.toString() + "\nDo you want to add new category for this transaction?\n" +
                                            "If you choose NO this transaction will be skipped!",
                                    "Success", JOptionPane.YES_NO_OPTION) == YES_OPTION) {
                                int size = SessionManager.getInstance().getUserCategories().size();
                                new CategoryCreator(SessionManager.getInstance().getUserCategories());
                                if (size < SessionManager.getInstance().getUserCategories().size()) {
                                    transaction.setCategoryId(SessionManager.getInstance().getUserCategories().getLast().getId());
                                    addTransaction(transaction, SessionManager.getInstance().getUserTransactions());
                                }
                            }
                        }
                    } else if (serializable instanceof Category category) {
                        if (category.getUserId() != DEFAULT_USER_ID) {
                            category.setUserId(userId);
                            addCategory(category, SessionManager.getInstance().getUserCategories());
                        }
                    }
                }

                showMessageDialog(null, "CSV file imported successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                showMessageDialog(null, "Failed to import CSV file: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
