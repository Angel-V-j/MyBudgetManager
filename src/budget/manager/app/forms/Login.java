package budget.manager.app.forms;

import budget.manager.app.managers.SessionManager;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JFrame {
    private JPanel jMainPanel;
    private JTextField jTextFieldUsername;
    private JPasswordField jPasswordField;
    private JButton jLoginButton;
    private JPanel jPanelRight;
    private JLabel jLabelHeader;
    private JLabel jLabelUsername;
    private JLabel jLabelPassword;
    private JButton jButtonSignUp;
    private JPanel jPanelViolet;

    public Login(){
        init();
    }

    private void init(){
        setContentPane(jMainPanel);
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,400);
        setLocationRelativeTo(null);
        setVisible(true);

        jLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SessionManager.getInstance().login(jTextFieldUsername.getText(), String.valueOf(jPasswordField.getPassword()))){
                    new Main();
                    dispose();
                }
                else {
                    showMessageDialog(null,"Invalid username or password. Please try again!");
                }
            }
        });
        jButtonSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SingUp();
                dispose();
            }
        });
        jLoginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                jLoginButton.setBackground(new Color(165,98,195));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                jLoginButton.setBackground(new Color(199,126,229));
            }
        });

        jButtonSignUp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                jButtonSignUp.setForeground(new Color(199,126,229));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                jButtonSignUp.setForeground(new Color(165,98,195));
            }
        });
    }

    private void createUIComponents() {

    }
}
