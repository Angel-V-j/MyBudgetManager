package budget.manager.app.forms;

import budget.manager.app.services.csv.CSVReader;
import budget.manager.app.services.csv.CSVWriter;
import budget.manager.app.managers.SessionManager;
import budget.manager.app.models.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;

import static budget.manager.app.controllers.CategoryController.*;
import static budget.manager.app.controllers.TransactionController.*;
import static budget.manager.app.controllers.UserController.*;
import static budget.manager.app.services.csv.DataExporter.exportData;
import static budget.manager.app.services.csv.DataImporter.importData;
import static budget.manager.app.util.BalanceUtil.*;
import static budget.manager.app.util.DateUtil.stringToDate;
import static budget.manager.app.util.SqlUtil.CATEGORY_TABLE_NAME;
import static budget.manager.app.util.SqlUtil.TRANSACTION_TABLE_NAME;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

public class Main extends JFrame {
    private JPanel jPanelMenu;
    private JButton jButtonTransactions2;
    private JButton jButtonRnS3;
    private JButton jButtonAccount4;
    private JButton jButtonLogOut0;
    private JButton jButtonHome1;
    private JPanel jPanelContent;
    private JPanel jPanelHome1;
    private JPanel jMainPanel;
    private JPanel jPanelTable;
    private JTable jTableAllTransactions;
    private JLabel jTransactionLabel;
    private JPanel jPanelRnS3;
    private JPanel jPanelAccount4;
    private JTextField jTextFieldBalance;
    private JTextField jTextFieldIncome;
    private JTextField jTextFieldExpense;
    private JLabel jLabelWelcome;
    private JPanel jPanelBalance;
    private JPanel jPanelMIncome;
    private JPanel jPanelMExpense;
    private JTable jTableTransactions;
    private JPanel jPanelTransaction2;
    private JPanel jPanel;
    private JPanel jButtonsControls;
    private JButton createCategoryButton1;
    private JButton editButtonCat;
    private JButton deleteCatButton;
    private JButton editButtonTra;
    private JButton deleteTraButton;
    private JTextField jTextFieldEditTra;
    private JTextField jTextFieldEditCat;
    private JTextField jTextFieldDeleteTra;
    private JTextField jTextFieldDeleteCat;
    private JButton createTransactionButton;
    private JPasswordField jPasswordFieldChange1;
    private JPasswordField jPasswordFieldChange2;
    private JButton changePasswordButton;
    private JButton deleteAccountButton;
    private JComboBox jComboBoxCurrency;
    private JButton changeCurrencyButton;
    private JLabel jLabelTransactionsTra;
    private JLabel jLabelTransactionsCat;
    private JButton jButtonImportTra;
    private JButton jButtonExportTra;
    private JPanel jPanelRnS;
    private JTextField jTextFDateTo;
    private JButton jButtonClickToResult;
    private JTextField jTextFDateFrom;
    private JLabel dateFromLabel;
    private JPanel Balance;
    private JLabel BalanceLabel;
    private JTextField jTextFBalance;
    private JButton jButtonShowChart;
    private JPanel IncomeJPanel;
    private JTextField jTextFIncome;
    private JLabel IncomeLabel;
    private JPanel ExpenseJPanel;
    private JTextField jTextFExpenses;
    private JPasswordField jPasswordFieldCurrent;
    private JButton jButtonImportCat;
    private JButton jButtonExportCat;
    private JPanel jPanelAcc;
    private JPanel jPanelImpNExp;

    public Main() {
        init();
    }

    private void init(){
        User currentUser = SessionManager.getInstance().getCurrentUser();
        setContentPane(jMainPanel);
        setTitle("Budget Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750,450);
        setLocationRelativeTo(null);
        initMenu();
        initSession(currentUser);

        jPanelTransaction2.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int newFontSize = Math.max(12, Math.min(jMainPanel.getWidth(), jMainPanel.getHeight()) / 22);
                Font currentFont = jLabelTransactionsTra.getFont();
                jLabelTransactionsTra.setFont(new Font(currentFont.getName(), currentFont.getStyle(), newFontSize));
                jLabelTransactionsCat.setFont(new Font(currentFont.getName(), currentFont.getStyle(), newFontSize));
                jLabelWelcome.setFont(new Font(currentFont.getName(), currentFont.getStyle(), newFontSize));
            }
        });

        setVisible(true);
    }

    private void initMenu() {
        jButtonHome1.setBackground(Color.white);
        jButtonHome1.setFocusable(Boolean.FALSE);
        jButtonRnS3.setText("<html>Reports &<br>Statistics</html>");

        MouseAdapter listener = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                JButton jButton = (JButton) e.getComponent();
                if (jButton.getBackground() != Color.white) {
                    jButton.setBackground(new Color(165,98,195));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (e.getComponent().getBackground() != Color.white) {
                    e.getComponent().setBackground(new Color(199,126,229));
                }
            }
        };

        JButton[] buttons = {jButtonHome1, jButtonTransactions2, jButtonRnS3, jButtonAccount4, jButtonLogOut0};
        JPanel[] panels = {jPanelHome1, jPanelTransaction2, jPanelRnS3, jPanelAccount4};

        for (JButton button : buttons) {
            button.addMouseListener(listener);
        }

        menuActionListener(buttons, panels);
    }

    private void menuActionListener(JButton[] buttons, JPanel[] panels) {
        for (JButton button : buttons) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(button.getName().endsWith("0")){
                        SessionManager.getInstance().logout();
                        new Login();
                        dispose();
                    }

                    button.setBackground(Color.white);
                    button.setEnabled(false);
                    button.setFocusable(Boolean.FALSE);
                    button.setForeground(new Color(165,98,195));
                    for (JPanel panel : panels) {
                        if(button.getName().endsWith(panel.getName()
                                .substring(panel.getName().length()-1))){
                            panel.setVisible(true);
                        } else {
                            panel.setVisible(false);
                        }
                    }

                    for (JButton buton : buttons) {
                        if (!buton.equals(button)) {
                            buton.setEnabled(true);
                            buton.setFocusable(Boolean.TRUE);
                            buton.setBackground(new Color(199,126,229));
                            buton.setForeground(Color.white);
                        }
                    }
                }
            });
        }

    }

    private void initSession(User currentUser) {
        ///Home Panel
        jLabelWelcome.setText("Welcome " + currentUser.getUsername());
        setHomeTextBoxes(SessionManager.getInstance().getUserTransactions());
        jPanelHome1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if (e.getComponent().isVisible())
                    setHomeTextBoxes(SessionManager.getInstance().getUserTransactions());
            }
        });
        jTableAllTransactions.setModel(new TransactionTableModel());

        ///Transaction Panel
        jTableTransactions.setModel(new TransactionTableModel());
        createTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TransactionCreator(currentUser, SessionManager.getInstance().getUserTransactions(),
                        SessionManager.getInstance().getUserCategories(),
                        jTableTransactions);
            }
        });

        editButtonTra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String traId = jTextFieldEditTra.getText();
                Transaction transaction = new Transaction();

                if ((transaction = checkIdTextBox(traId, transaction)) != null) {
                    new TransactionCreator(transaction,
                            SessionManager.getInstance().getUserCategories(),
                            jTableTransactions);
                    jTextFieldEditTra.setText("");
                } else {
                    showMessageDialog(null, "Transaction not found!\nPlease enter a valid transaction ID!");
                }
            }
        });

        deleteTraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String traId = jTextFieldDeleteTra.getText();
                Transaction transaction = new Transaction();

                if ((transaction = checkIdTextBox(traId, transaction)) != null) {
                    removeTransaction(transaction, SessionManager.getInstance().getUserTransactions());
                    jTableTransactions.setModel(new TransactionTableModel());
                    jTextFieldDeleteTra.setText("");
                } else {
                    showMessageDialog(null, "Transaction not found!\nPlease enter a valid transaction ID!");
                }
            }
        });

        createCategoryButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CategoryCreator(SessionManager.getInstance().getUserCategories());
            }
        });

        editButtonCat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String catId = jTextFieldEditCat.getText();
                Category category = new Category();

                if ((category = checkIdTextBox(catId, category)) != null) {
                    if (category.getUserId() == currentUser.getId()) {
                        new CategoryCreator(category);
                        jTextFieldEditCat.setText("");
                    }
                } else {
                    showMessageDialog(null, "Category not found!\nPlease enter a valid category ID!");
                }
            }
        });

        deleteCatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String catId = jTextFieldDeleteCat.getText();
                Category category = new Category();

                if ((category = checkIdTextBox(catId, category)) != null) {
                    if (category.getUserId() == currentUser.getId()) {
                        removeCategory(category, SessionManager.getInstance().getUserCategories());
                        jTextFieldDeleteCat.setText("");
                    }
                } else {
                    showMessageDialog(null, "Category not found!\nPlease enter a valid category ID!");
                }
            }
        });

        /// Statistics
        jButtonClickToResult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate lDateFrom;
                LocalDate lDateTo;
                if (!jTextFDateFrom.getText().isEmpty() && !jTextFDateTo.getText().isEmpty()) {
                    try {
                        lDateFrom = stringToDate(jTextFDateFrom.getText());
                        lDateTo = stringToDate(jTextFDateTo.getText());
                        jTextFBalance.setText(String.valueOf(balanceForPeriod(SessionManager.getInstance().getUserTransactions(), lDateFrom, lDateTo)));
                        jTextFIncome.setText(String.valueOf(incomeForPeriod(SessionManager.getInstance().getUserTransactions(), lDateFrom, lDateTo)));
                        jTextFExpenses.setText(String.valueOf(expensesForPeriod(SessionManager.getInstance().getUserTransactions(), lDateFrom, lDateTo)));
                    } catch (DateTimeParseException ex) {
                        showMessageDialog(null, "Invalid date format!\nPlease enter a valid date format!");
                    }
                } else {
                    System.out.println(jTextFDateFrom.getText());
                    System.out.println(jTextFDateTo.getText());
                    showMessageDialog(null, "You have to enter date in both fields!");
                }
            }
        });

        jButtonShowChart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PieChart(SessionManager.getInstance().getUserCategories());
            }
        });

        /// Account
        for(Currency currency : Currency.values()){
            jComboBoxCurrency.addItem(currency.name());
        }

        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char[] pass = jPasswordFieldCurrent.getPassword();
                char[] newPass1 = jPasswordFieldChange1.getPassword();
                char[] newPass2 = jPasswordFieldChange2.getPassword();
                if (pass != null && newPass1 != null && newPass2 != null) {
                    if (String.valueOf(pass).equals(currentUser.getPassword())) {
                        if(Arrays.equals(newPass1,newPass2)){
                            if(editUser(SessionManager.getInstance().getCurrentUser(), newPass1)) {
                                showMessageDialog(null, "You have successfully changed your password!");
                                SessionManager.getInstance().logout();
                                new Login();
                                dispose();
                            } else {
                                showMessageDialog(null, "Something went wrong!\nPlease try again!");
                            }
                        } else {
                            showMessageDialog(null, "Passwords don't match!");
                        }
                    } else {
                        showMessageDialog(null, "Wrong password! Try again!");
                    }
                } else {
                    showMessageDialog(null, "Some fields are empty!");
                }
            }
        });

        changeCurrencyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editUser(SessionManager.getInstance().getCurrentUser(),
                        Enum.valueOf(Currency.class, jComboBoxCurrency.getSelectedItem().toString()))) {
                    showMessageDialog(null, "Currencies have been changed!");
                } else {
                    showMessageDialog(null, "Currencies don't have been changed!");
                }
            }
        });

        deleteAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showConfirmDialog(null, "This will delete your account and all your data!\nDo you want to proceed?",
                        "Delete Account",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (removeUser(currentUser)) {
                        SessionManager.getInstance().logout();
                        dispose();
                        new Login();
                    } else {
                        showMessageDialog(null, "Something went wrong!\nPlease try again!");
                    }
                }
            }
        });

        ActionListener importFile = e -> importData(((JButton) e.getSource()).getName());

        jButtonImportTra.addActionListener(importFile);
        jButtonImportCat.addActionListener(importFile);

        jButtonExportTra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportData(SessionManager.getInstance().getUserTransactions(),
                        TRANSACTION_TABLE_NAME + currentUser.getUsername().toUpperCase());
            }
        });

        jButtonExportCat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportData(SessionManager.getInstance().getUserCategories(),
                        CATEGORY_TABLE_NAME + currentUser.getUsername().toUpperCase());
            }
        });
    }

    private <T extends Serializable> T checkIdTextBox(String stringId, Serializable object) {
        if (!stringId.isEmpty()) {
            try {
                int id = Integer.parseInt(stringId);
                if (object instanceof Transaction) {
                    return (T) searchTransactionById(id, SessionManager.getInstance().getUserTransactions());
                } else if (object instanceof Category) {
                    return (T) searchCategoryById(SessionManager.getInstance().getUserCategories(), id);
                }

            } catch (NumberFormatException e1) {
                showMessageDialog(null, "Enter only whole numbers!");
            }

        } else {
            showMessageDialog(null, "Please enter an ID!");
        }

        return null;
    }

    private void setHomeTextBoxes(ArrayList<Transaction> transactions) {
        jTextFieldExpense.setText(String.format("%.2f", calculateExpenses(transactions)));
        jTextFieldIncome.setText(String.format("%.2f", calculateIncome(transactions)));
        jTextFieldBalance.setText(String.format("%.2f", calculateBalance(transactions)));
    }
}
