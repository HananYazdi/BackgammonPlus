package view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class RollResultPopup {

    public static void showDiceRoll(String player1Name, String player2Name, int[] rolls) {
        // Determine the winner
        String winner = (rolls[0] > rolls[1]) ? player1Name : player2Name;

        // Create a JFrame to display the results
        JFrame frame = new JFrame("Dice Roll Results");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Create a JPanel to display the results
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(50, 50, 50));
        frame.setContentPane(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add a rolling dice GIF to simulate animation
        JLabel rollingGif = new JLabel(new ImageIcon(RollResultPopup.class.getResource("/images/DiceGif.gif")));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(rollingGif, gbc);

        // Refresh the panel to show the GIF
        panel.revalidate();
        panel.repaint();

        // Display the results after a 3-second animation
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // Remove the GIF
                panel.remove(rollingGif);

                // Array of dice image paths
                URL[] diceImages = {
                	RollResultPopup.class.getResource("/images/1.png"),
                	RollResultPopup.class.getResource("/images/2.png"),
                	RollResultPopup.class.getResource("/images/3.png"),
                	RollResultPopup.class.getResource("/images/4.png"),
                	RollResultPopup.class.getResource("/images/5.png"),
                	RollResultPopup.class.getResource("/images/6.png")
                };

                // Create scaled icons for dice images
                ImageIcon diceIcon1 = getScaledIcon(diceImages[rolls[0] - 1], 80, 80);
                ImageIcon diceIcon2 = getScaledIcon(diceImages[rolls[1] - 1], 80, 80);

                // Add the first dice result
                JLabel dice1 = new JLabel(diceIcon1);
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 1;
                panel.add(dice1, gbc);

                // Add the second dice result
                JLabel dice2 = new JLabel(diceIcon2);
                gbc.gridx = 1;
                gbc.gridy = 0;
                panel.add(dice2, gbc);

                // Add player names and rolls
                JLabel player1Label = new JLabel(player1Name + ": " + rolls[0]);
                player1Label.setFont(new Font("Arial", Font.BOLD, 16));
                player1Label.setForeground(Color.WHITE);
                gbc.gridx = 0;
                gbc.gridy = 1;
                panel.add(player1Label, gbc);

                JLabel player2Label = new JLabel(player2Name + ": " + rolls[1]);
                player2Label.setFont(new Font("Arial", Font.BOLD, 16));
                player2Label.setForeground(Color.WHITE);
                gbc.gridx = 1;
                gbc.gridy = 1;
                panel.add(player2Label, gbc);

                // Add winner label
                JLabel winnerLabel = new JLabel("The Beginner Is: " + winner);
                winnerLabel.setFont(new Font("Arial", Font.BOLD, 18));
                winnerLabel.setForeground(Color.YELLOW);
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.gridwidth = 2;
                panel.add(winnerLabel, gbc);

                // Add a button to close the popup
                JButton closeButton = new JButton("Close");
                closeButton.addActionListener(e -> frame.dispose());
                gbc.gridx = 0;
                gbc.gridy = 3;
                gbc.gridwidth = 2;
                panel.add(closeButton, gbc);

                // Refresh the panel to display updates
                panel.revalidate();
                panel.repaint();
            }
        }, 3000); // Wait for 3 seconds to show results after GIF

        frame.setVisible(true);
    }

    // Helper method to scale dice images
    private static ImageIcon getScaledIcon(URL imagePath, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
