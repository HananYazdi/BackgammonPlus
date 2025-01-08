package controller;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import model.Level;
import view.HistoryAndQuestionsMenu;

public class GameMenu {
    private JFrame mainFrame;
    private JTextField firstPlayerField;
    private JTextField secondPlayerField;
    private JComboBox<String> difficultyCombo;
    private String name1;
    private String name2;
    private Level level;
    
    public GameMenu() {
        initializeFrame();
        initializeComponents();
        setupLayout();
        addListeners();
        mainFrame.setVisible(true);
    }

    private void initializeFrame() {
        mainFrame = new JFrame("Backgammon Menu");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Set up background
        ImageIcon bgIcon = new ImageIcon(getClass().getResource("/images/backgammon_image.png"));
        Image backgroundImg = bgIcon.getImage();
        
        JPanel backgroundPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        
        mainFrame.setContentPane(backgroundPanel);
    }

    private void initializeComponents() {
        firstPlayerField = new JTextField();
        secondPlayerField = new JTextField();
        difficultyCombo = new JComboBox<>(new String[]{"EASY", "MEDIUM", "HARD"});
    }

    private void setupLayout() {
        JPanel contentPanel = (JPanel) mainFrame.getContentPane();
        
        // Add all components
        contentPanel.add(firstPlayerField);
        contentPanel.add(secondPlayerField);
        contentPanel.add(difficultyCombo);
        
        // Add buttons
        JButton startButton = createButton("Start", e -> handleStartGame());
        JButton historyButton = createButton("History & Questions", e -> openHistoryMenu());
        JButton rulesButton = createButton("Rules", e -> showRules());
        
        contentPanel.add(startButton);
        contentPanel.add(historyButton);
        contentPanel.add(rulesButton);
        
        // Initial positioning
        mainFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateComponentPositions(startButton, historyButton, rulesButton);
            }
        });
    }

    private JButton createButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        return button;
    }

    private void updateComponentPositions(JButton startButton, JButton historyButton, JButton rulesButton) {
        int width = mainFrame.getWidth();
        int height = mainFrame.getHeight();
        
        int fieldWidth = (int)(width * 0.1);
        int fieldHeight = 30;
        
        // Position text fields
        firstPlayerField.setBounds((int)(width * 0.2), (int)(height * 0.5), fieldWidth, fieldHeight);
        secondPlayerField.setBounds((int)(width * 0.2), (int)(height * 0.67), fieldWidth, fieldHeight);
        difficultyCombo.setBounds((int)(width * 0.7), (int)(height * 0.50), fieldWidth, fieldHeight);
        
        // Position buttons
        int buttonWidth = 160;
        int buttonHeight = 40;
        int buttonsY = (int)(height * 0.69);
        startButton.setBounds((width / 2 - buttonWidth + 69), buttonsY, buttonWidth, buttonHeight);
        historyButton.setBounds((width / 2 + 229), buttonsY, buttonWidth, buttonHeight);
        rulesButton.setBounds(10, 10, 100, 30);
    }

    private void handleStartGame() {
        if (!validateInputs()) {
            return;
        }
        
        name1 = firstPlayerField.getText();
        name2 = secondPlayerField.getText();
        level = Level.valueOf(difficultyCombo.getSelectedItem().toString());
        
        mainFrame.dispose();
        SwingUtilities.invokeLater(() -> {
            Game game = new Game(level, name1, name2);
            game.start();
        });
    }

    private boolean validateInputs() {
        if (firstPlayerField.getText().trim().isEmpty()) {
            showError("Please enter the name of the first player.");
            return false;
        }
        if (secondPlayerField.getText().trim().isEmpty()) {
            showError("Please enter the name of the second player.");
            return false;
        }
        if (firstPlayerField.getText().equals(secondPlayerField.getText())) {
            showError("Please enter different names.");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(mainFrame, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    private void openHistoryMenu() {
        mainFrame.dispose();
        SwingUtilities.invokeLater(() -> HistoryAndQuestionsMenu.main(new String[0]));
    }

    private void showRules() {
        String rules = "Backgammon Rules:\n\n" +
                "1. The game is played between two players, each with 15 pieces.\n" +
                "2. The board consists of 24 triangular stations.\n" +
                "3. Players move in opposite directions.\n" +
                "4. The goal is to move all pieces to your home quadrant.\n" +
                "5. Movement is determined by dice rolls.\n" +
                "6. Difficulty levels affect available dice types and rules.";
        
        JOptionPane.showMessageDialog(mainFrame, rules, "Game Rules", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addListeners() {
        mainFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    updateComponentPositions(
                        (JButton)((JPanel)mainFrame.getContentPane()).getComponent(3),
                        (JButton)((JPanel)mainFrame.getContentPane()).getComponent(4),
                        (JButton)((JPanel)mainFrame.getContentPane()).getComponent(5)
                    );
                });
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameMenu());
    }

    // Method to open menu from other classes
    public static void openMenu() {
        SwingUtilities.invokeLater(() -> new GameMenu());
    }
}