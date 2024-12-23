package budget.manager.app.services.csv;

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
                        if (transaction.getCategoryId() > 13) {
                            Category category = getCategoryById(categoryId);
                            category.setUserId(userId);
                            addCategory(category, SessionManager.getInstance().getUserCategories());
                            transaction.setCategoryId(SessionManager.getInstance().getUserCategories().getLast().getId());
                        }
                        addTransaction(transaction, SessionManager.getInstance().getUserTransactions());
                    } else if (serializable instanceof Category category) {
                        category.setUserId(userId);
                        addCategory(category, SessionManager.getInstance().getUserCategories());
                    }
                }

                JOptionPane.showMessageDialog(null, "CSV file imported successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to import CSV file: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
