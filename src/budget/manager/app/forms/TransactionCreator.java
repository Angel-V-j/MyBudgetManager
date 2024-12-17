package budget.manager.app.forms;

import budget.manager.app.models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import static budget.manager.app.controllers.TransactionController.*;
import static budget.manager.app.util.DateUtil.dateToString;
import static budget.manager.app.util.DateUtil.stringToDate;
import static javax.swing.JOptionPane.showMessageDialog;

public class TransactionCreator extends JFrame {
    private JPanel jPanelCreateTra;
    private JPanel jPanelInfo;
    private JLabel descriptionLabel;
    private JTextArea textAreaDescription;
    private JPanel jPanelDate;
    private JLabel dateLabel;
    private JTextField textFDate;
    private JPanel jPanelCategory;
    private JLabel categoryLabel;
    private JComboBox categoryPicker;
    private JPanel jPanelAmount;
    private JLabel amountLabel;
    private JTextField textFAmount;
    private JButton createTransactionButton;
    private JPanel jMainPanel;

    private LocalDate localDate = null;

    public TransactionCreator(User user, ArrayList<Category> categories, JTable table) {
        init(categories);
        createTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Category category = (Category) categoryPicker.getSelectedItem();
                localDate = getDate();
                try {
                    createTransactionButtonActionPerformed(addTransaction(user.getId(), category.getId(),
                                    Double.parseDouble(textFAmount.getText()), localDate,
                                    textAreaDescription.getText()), table, "created");
                } catch (NumberFormatException ex) {
                    showMessageDialog(null, "Error "+ex.getMessage() + "\nYou can enter only numbers", "Error! Not Numeric Amount", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public TransactionCreator(Transaction transaction, ArrayList<Category> categories, JTable table) {
        init(categories);
        categoryPicker.setSelectedIndex(transaction.getCategoryId());
        textFAmount.setText(String.valueOf(transaction.getAmount()));
        textFDate.setText(dateToString(transaction.getLocalDate()));
        textAreaDescription.setText(transaction.getDescription());
        createTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                localDate = getDate();
                Category category = (Category) categoryPicker.getSelectedItem();
                try {
                    createTransactionButtonActionPerformed(updateTransaction(transaction, new TransactionFactory().create(transaction.getId(), transaction.getUserId(),
                                    category.getId(),
                                    Double.parseDouble(textFAmount.getText()),
                                    localDate, textAreaDescription.getText())),
                                    table, "edited");
                } catch (NumberFormatException ex) {
                    showMessageDialog(null, "Error "+ex.getMessage() + "\nYou can enter only numbers", "Error! Not Numeric Amount", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    private void init(ArrayList<Category> category) {
        setContentPane(jMainPanel);
        setTitle("Create Transaction");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(350,450);
        setLocationRelativeTo(null);
        setVisible(true);

        categoryPicker.setModel(new DefaultComboBoxModel(category.toArray(new Category[0])));
        categoryPicker.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {

                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (component instanceof JLabel && value instanceof Category) {
                    JLabel label = (JLabel) component;
                    Category category = (Category) value;

                    label.setText(category.getId() + ". " + category.getName());
                    if (category.isIncome()) {
                        label.setForeground(Color.GREEN);
                    } else {
                        label.setForeground(Color.RED);
                    }
                }

                return component;
            }
        });

        createTransactionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                createTransactionButton.setBackground(new Color(130, 190, 155));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                createTransactionButton.setBackground(new Color(170,230, 195));
            }
        });

        textFDate.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textFDate.getText().equals("dd/MM/yyyy")) {
                    textFDate.setText("");
                    textFDate.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textFDate.getText().isEmpty()) {
                    textFDate.setText("dd/MM/yyyy");
                    textFDate.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void createTransactionButtonActionPerformed(boolean condition, JTable table, String type) {
        if (localDate.isBefore(LocalDate.now()) || localDate.isEqual(LocalDate.now())) {
            if (condition) {
                saveTransactions();
                table.setModel(new TransactionTableModel());
                showMessageDialog(null, "You have successfully " + type + " your transaction!","Transaction " + type, JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                showMessageDialog(null, "Error has occurred","Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            showMessageDialog(null, "Invalid Date!\nPlease try again!","Invalid Date", JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDate getDate() {
        try {
            return stringToDate(textFDate.getText());
        } catch (DateTimeParseException ex) {
            showMessageDialog(null, "Invalid Date Format!\nPlease try again!","Invalid Date", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(ex);
        }
    }
}
