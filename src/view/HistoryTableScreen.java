package view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import model.SysData;
import model.SysData.GameHistory;

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
//            String[] columns = {"Date", "Player 1", "Player 2", "Winner"};
//            Object[][] data = {
//                    {"28/11/2024", "Yuval", "Hanan", "Hanan"},
//                    {"27/11/2024", "Or", "Oren", "Or"},
//                    {"26/11/2024", "Hanan", "Oren", "Oren"},
//                    {"25/11/2024", "Or", "Yuval", "Or"}
//            };
			SysData sysData = new SysData();
			List<GameHistory> list = sysData.getGameHistories();
			String[] columns = { "Player 1", "player 1 Score", "Player 2", "player 2 Score", "Winner",
					"Difficulty Level", "Time" };
			Object[][] data = new Object[list.size()][7];
			for (int i = 0; i < list.size(); i++) {
				GameHistory history = list.get(i);
				data[i][0] = history.getPlayer1Name();
				data[i][1] = history.getPlayer1Score();
				data[i][2] = history.getPlayer2Name();
				data[i][3] = history.getPlayer2Score();
				data[i][4] = history.getWinner();
				data[i][5] = history.getDifficultyLevel();
				data[i][6] = history.getGameTime();
			}

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
