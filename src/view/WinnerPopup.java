package view;

import javax.swing.*;  // For JFrame, JLabel, ImageIcon
import java.awt.*;     // For BorderLayout, Font

public class WinnerPopup {
    // Method to display the winner popup
    public static void showWinnerPopup(String playerName) {
        // Create the frame
        JFrame frame = new JFrame("Game Over");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 600); // Adjust size to fit both GIFs
        frame.setLayout(new BorderLayout());

        // Add winner name label
        JLabel winnerLabel = new JLabel(playerName + " is the Winner!", SwingConstants.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(winnerLabel, BorderLayout.CENTER);

        // Add top GIF
        ImageIcon topGifIcon = new ImageIcon(WinnerPopup.class.getResource("/images/prize.gif")); // Path to top GIF
        JLabel topGifLabel = new JLabel(topGifIcon, SwingConstants.CENTER);
        frame.add(topGifLabel, BorderLayout.NORTH);

        // Add bottom GIF
        ImageIcon bottomGifIcon = new ImageIcon(WinnerPopup.class.getResource("/images/winnerRespect.gif")); // Path to bottom GIF
        JLabel bottomGifLabel = new JLabel(bottomGifIcon, SwingConstants.CENTER);
        frame.add(bottomGifLabel, BorderLayout.SOUTH);

        // Show the frame
        frame.setVisible(true);
    }

}
