package budget.manager.app.util;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;

import static javax.swing.JOptionPane.showMessageDialog;

public class FileUtil {

    public static final String USERS_FILE_NAME = "users.csv";
    public static final String TRANSACTIONS_FILE_NAME = "transactions.csv";
    public static final String CATEGORIES_FILE_NAME = "categories.csv";

    private FileUtil(){}

    public static String getTextFilePath(String file) {
        String directory = System.getProperty("user.dir");
        return new File("src/budget/manager/app/database/" + file).getAbsolutePath();
    }

    public static int getUniqueId(List<? extends Serializable> list) {
        int id = 0;
        try {
            id = Integer.parseInt(list.getLast().toString().split(",")[0]);
            return 1 + id;
        } catch (NumberFormatException e) {
            showMessageDialog(null, "Id must be an integer");
        } catch (NoSuchElementException ex) {
            return 0;
        }

        return id;
    }
}
