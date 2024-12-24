package budget.manager.app.forms;

import budget.manager.app.managers.SessionManager;
import budget.manager.app.models.Category;
import budget.manager.app.models.CategoryFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static budget.manager.app.controllers.CategoryController.*;
import static javax.swing.JOptionPane.showMessageDialog;

public class CategoryCreator extends JDialog {
    private JPanel jPanelCreateTra;
    private JPanel jPanelRButtonExp;
    private JPanel jPanelCategory;
    private JLabel categoryNameLabel;
    private JPanel jPanelRButtonInc;
    private JButton createCategoryButton;
    private JPanel jMainPanel;
    private JTextField textFieldCatName;
    private JRadioButton radioButtonIsInc;
    private JRadioButton radioButtonIsExp;

    public CategoryCreator(ArrayList<Category> categories) {
        setModalityType(ModalityType.APPLICATION_MODAL);
        init();
        createCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createCategoryButtonActionPerformed(addCategory(SessionManager.getInstance().getCurrentUser().getId(),
                        textFieldCatName.getText(), radioButtonIsInc.isSelected(), categories), "created");
            }
        });
        setVisible(true);
    }

    public CategoryCreator(Category category) {
        init();
        textFieldCatName.setText(category.getName());
        if (category.isIncome()){
            radioButtonIsInc.setSelected(true);
        } else {
            radioButtonIsExp.setSelected(true);
        }

        createCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createCategoryButtonActionPerformed(editCategory(category, new CategoryFactory().create(category.getId(),
                        category.getUserId(), textFieldCatName.getText(), radioButtonIsInc.isSelected())), "edited");
            }
        });
        setVisible(true);
    }

    private void init() {
        setContentPane(jMainPanel);
        setTitle("Create Transaction");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(320,400);
        setLocationRelativeTo(null);

        createCategoryButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                createCategoryButton.setBackground(new Color(190,167,130));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                createCategoryButton.setBackground(new Color(230, 207, 170));
            }
        });
    }

    private void createCategoryButtonActionPerformed(boolean condition, String string) {
        if (!textFieldCatName.getText().isEmpty()) {
            if (radioButtonIsInc.isSelected() || radioButtonIsExp.isSelected()) {
                if (condition) {
                    showMessageDialog(null, "Category " + string + "!");
                    dispose();
                } else {
                    showMessageDialog(null, "Something went wrong!");
                }
            } else {
                showMessageDialog(null, "Please select Income or Expense!");
            }

        } else {
            showMessageDialog(null, "Please enter category name!");
        }
    }
}
