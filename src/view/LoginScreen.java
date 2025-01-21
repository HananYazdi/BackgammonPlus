package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginScreen {
    private static final String USERNAME = "admin"; 
    private static final String PASSWORD = "1234"; 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame loginFrame = new JFrame("Login");
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setSize(400, 250);
            loginFrame.setLayout(null);
            loginFrame.setResizable(false);

            // כותרת
            JLabel titleLabel = new JLabel("Login to Questions Table");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            titleLabel.setBounds(100, 20, 250, 30);
            loginFrame.add(titleLabel);

            // שם משתמש
            JLabel usernameLabel = new JLabel("Username:");
            usernameLabel.setBounds(50, 70, 100, 25);
            loginFrame.add(usernameLabel);

            JTextField usernameField = new JTextField();
            usernameField.setBounds(150, 70, 180, 25);
            loginFrame.add(usernameField);

            // סיסמה
            JLabel passwordLabel = new JLabel("Password:");
            passwordLabel.setBounds(50, 110, 100, 25);
            loginFrame.add(passwordLabel);

            JPasswordField passwordField = new JPasswordField();
            passwordField.setBounds(150, 110, 180, 25);
            loginFrame.add(passwordField);

            // כפתור התחברות
            JButton loginButton = new JButton("Login");
            loginButton.setBounds(150, 160, 100, 30);
            loginFrame.add(loginButton);

            // מאזין לחיצה לכפתור התחברות
            loginButton.addActionListener((ActionEvent e) -> {
                String enteredUsername = usernameField.getText();
                String enteredPassword = new String(passwordField.getPassword());

                if (USERNAME.equals(enteredUsername) && PASSWORD.equals(enteredPassword)) {
                    JOptionPane.showMessageDialog(loginFrame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loginFrame.dispose(); // סגירת חלון ההתחברות
                    QuestionsTableScreen.main(null); // מעבר לעמוד הראשי
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Incorrect Username or Password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            });

            loginFrame.setLocationRelativeTo(null); // למרכז את החלון על המסך
            loginFrame.setVisible(true);
        });
    }
}
