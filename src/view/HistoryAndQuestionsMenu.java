package view;

import javax.swing.*;

import controller.GameMenu;

import java.awt.*;
import java.awt.event.*;

public class HistoryAndQuestionsMenu {
    public static void main(String[] args) {
        JFrame main_frame = new JFrame("Backgammon Menu");
        main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main_frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Load the background image from src folder
        ImageIcon bgIcon = new ImageIcon(HistoryAndQuestionsMenu.class.getResource("/images/backgammon_History_Questions.png"));
        Image backgroundImg = bgIcon.getImage();

        // Create a custom panel with a null layout and a background image
        JPanel backgroundPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = getWidth();
                int h = getHeight();
                g.drawImage(backgroundImg, 0, 0, w, h, this);
            }
        };

        main_frame.setContentPane(backgroundPanel);

        // Create buttons
        JButton history_button = new JButton("History");
        JButton questions_button = new JButton("Questions");
        JButton back_button = new JButton("Back to Main Menu");

        // Add buttons to the background panel
        backgroundPanel.add(history_button);
        backgroundPanel.add(questions_button);
        backgroundPanel.add(back_button);

        // Add ActionListeners:
        // These listeners open a popup (JOptionPane) where the user can edit a question.
        history_button.addActionListener(e -> {
            // Hide the current frame
            main_frame.setVisible(false);

            // Open the new HistoryTableScreen
            HistoryTableScreen.main(null);

            // Optionally dispose of the current frame to free resources
            main_frame.dispose();
        });

        questions_button.addActionListener(e -> {
            // Hide current frame
            main_frame.setVisible(false);

            // Open the new QuestionsTableScreen
            QuestionsTableScreen.main(null);

            // Optionally dispose of the current frame to free resources
            main_frame.dispose();
        });

        back_button.addActionListener(e -> {
            main_frame.dispose();

            System.out.println("Back to Main Menu clicked");
        });

        // Adjust positions when the window is resized
        main_frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = main_frame.getWidth();
                int height = main_frame.getHeight();

                int buttonWidth = 150;
                int buttonHeight = 30;

                // History button (left side)
                int historyX = (int)(width * 0.10);
                int historyY = (int)(height * 0.51);
                history_button.setBounds(historyX, historyY, buttonWidth, buttonHeight);

                // Questions button (right side)
                int questionsX = (int)(width * 0.77);
                int questionsY = (int)(height * 0.51);
                questions_button.setBounds(questionsX, questionsY, buttonWidth, buttonHeight);

                // Back to Main Menu button (bottom left)
                int backX = (int)(width * 0.1);
                int backY = (int)(height * 0.8);
                back_button.setBounds(backX, backY, buttonWidth, buttonHeight);
            }
        });

        main_frame.setVisible(true);
    }
}