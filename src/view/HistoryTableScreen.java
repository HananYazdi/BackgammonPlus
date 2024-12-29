package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HistoryTableScreen {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("History Table");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            // Panel with a custom background
            ImageIcon bgIcon = new ImageIcon(HistoryAndQuestionsMenu.class.getResource("/images/history.png"));
            Image backgroundImg = bgIcon.getImage();

            JPanel backgroundPanel = new JPanel(null) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
                }
            };

            frame.setContentPane(backgroundPanel);

            // Table data and columns
            String[] columns = {"Date", "Player 1", "Player 2", "Winner"};
            Object[][] data = {
                    {"28/11/2024", "Yuval", "Hanan", "Hanan"},
                    {"27/11/2024", "Or", "Oren", "Or"},
                    {"26/11/2024", "Hanan", "Oren", "Oren"},
                    {"25/11/2024", "Or", "Yuval", "Or"}
            };

            DefaultTableModel tableModel = new DefaultTableModel(data, columns);
            JTable table = new JTable(tableModel);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 16));

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBounds(50, 200, 1100, 400);
            backgroundPanel.add(scrollPane);

            // Buttons
            JButton backButton = new JButton("BACK");
            backButton.setBounds(50, 600, 100, 40);
            backButton.setFont(new Font("Arial", Font.BOLD, 14));
            backgroundPanel.add(backButton);

            backButton.addActionListener(e -> {
                frame.setVisible(false); // Hide the current frame
                HistoryAndQuestionsMenu.main(null); // Open the Questions and History menu
                frame.dispose(); // Dispose of the current frame
                // Return to the main menu or previous screen
                // MainMenu.main(null); // Uncomment and replace with your main menu class
            });

            // Set frame visibility
            frame.setVisible(true);
        });
    }
}
