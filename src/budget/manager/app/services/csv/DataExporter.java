package budget.manager.app.services.csv;

import budget.manager.app.services.csv.interfaces.Exporter;

import javax.swing.*;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class DataExporter implements Exporter {
    public static void exportData(ArrayList<? extends Serializable> data, String defaultFileName) {
        if (data == null || data.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No data to export!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser directoryChooser = new JFileChooser();
        directoryChooser.setDialogTitle("Select a directory to save the file");
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = directoryChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = directoryChooser.getSelectedFile();

            String fileName = JOptionPane.showInputDialog(null,
                    "Enter the file name (without extension):", defaultFileName);
            if (fileName == null || fileName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "File name cannot be empty!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String filePath = new File(selectedDirectory, fileName + ".csv").getAbsolutePath();

            try {
                new CSVWriter().write(data, filePath);
                JOptionPane.showMessageDialog(null, "Data exported successfully to " + filePath,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to export data: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
