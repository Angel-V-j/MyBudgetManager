package budget.manager.app.forms;

import budget.manager.app.models.Currency;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

import static javax.swing.JOptionPane.showMessageDialog;
import static budget.manager.app.controllers.UserController.*;

public class SingUp extends JFrame {
    private JPanel jMainPanel;
    private JPanel jPanelRight;
    private JLabel jLabelHeader;
    private JLabel jLabelUsername;
    private JLabel jLabelPassword;
    private JTextField jTextFieldUsername;
    private JPasswordField jPasswordField;
    private JButton jLoginButton;
    private JButton jButtonSignUp;
    private JPanel jPanelViolet;
    private JPasswordField jPasswordField2;
    private JComboBox jComboBoxCurrency;

    public SingUp() {
        init();
        jButtonSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = jTextFieldUsername.getText();
                char[] password = jPasswordField2.getPassword();
                if (!username.isEmpty()) {
                    if (password.length > 0 && jPasswordField.getPassword().length > 0) {
                        if (searchUserByUsername(username) == null) {
                            if (Arrays.equals(password, jPasswordField.getPassword())) {
                                if (addUser(username, String.valueOf(password), Currency.valueOf(String.
                                        valueOf(jComboBoxCurrency.getSelectedItem())))){
                                    new Login();
                                    showMessageDialog(null,"You have successfully sign upped!","SignUp",JOptionPane.INFORMATION_MESSAGE);
                                    dispose();
                                } else {
                                    showMessageDialog(null,"Something went wrong! Please try again!","Error",JOptionPane.ERROR_MESSAGE);
                                }

                            } else {
                                showMessageDialog(null, "The two passwords do not match!\n"+
                                        "Please try again!","Passwords do not match",JOptionPane.ERROR_MESSAGE);
                            }

                        } else {
                            showMessageDialog(null,"There is already a user with this username!" +
                                    "\nPlease try another username!","Taken username",JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        showMessageDialog(null,"You have not entered a password" +
                                "\nPlease enter a password!","Password empty",JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    showMessageDialog(null,"You have not entered a username" +
                            "\nPlease enter a username!","Username empty",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        jButtonSignUp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                jButtonSignUp.setBackground(new Color(165,98,195));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                jButtonSignUp.setBackground(new Color(199,126,229));
            }
        });

        jLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login();
                dispose();
            }
        });

        jLoginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                jLoginButton.setForeground(new Color(199,126,229));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                jLoginButton.setForeground(new Color(165,98,195));
            }
        });
    }

    private void init(){
        setContentPane(jMainPanel);
        setTitle("Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        setLocationRelativeTo(null);
        for(Currency currency : Currency.values()){
            jComboBoxCurrency.addItem(currency.name());
        }
        jComboBoxCurrency.setSelectedIndex(0);
        setVisible(true);
    }
}
