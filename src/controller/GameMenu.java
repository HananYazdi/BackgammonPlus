package controller;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Level;
import view.HistoryAndQuestionsMenu;

public class GameMenu {
	public static boolean flag;
	public static Level level;

	public static void main(String[] args) {
		JFrame main_frame = new JFrame("Backgammon Menu");
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main_frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		// Load the background image
		ImageIcon bgIcon = new ImageIcon("src/images/backgammon_image.png");
		Image backgroundImg = bgIcon.getImage();

		// Create a panel with null layout to place components absolutely
		JPanel backgroundPanel = new JPanel(null) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				int w = getWidth();
				int h = getHeight();
				// Draw the image to fill the entire panel
				g.drawImage(backgroundImg, 0, 0, w, h, this);
			}
		};

		main_frame.setContentPane(backgroundPanel);

		// Create components
		JTextField first_player_field = new JTextField();
		JTextField second_player_field = new JTextField();
		String[] difficulties = { "EASY", "MEDIUM", "HARD" };
		JComboBox<String> difficulty_combo = new JComboBox<>(difficulties);

		JButton start_button = new JButton("Start");
		JButton history_button = new JButton("History & Questions");

		// Add components to the background panel
		backgroundPanel.add(first_player_field);
		backgroundPanel.add(second_player_field);
		backgroundPanel.add(difficulty_combo);
		backgroundPanel.add(start_button);
		backgroundPanel.add(history_button);

		history_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Action for History & Questions button
				System.out.println("History & Questions button clicked! Showing history...");
				main_frame.dispose();
				// Open HistoryAndQuestionsMenu
				HistoryAndQuestionsMenu.main(new String[0]);
			}
		});

		// Adjust component positions when resized
		main_frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int width = main_frame.getWidth();
				int height = main_frame.getHeight();

				int fieldWidth = (int) (width * 0.1);
				int fieldHeight = 30;

				// Adjusted positions:
				int leftX = (int) (width * 0.2);
				int firstPlayerY = (int) (height * 0.5);
				first_player_field.setBounds(leftX, firstPlayerY, fieldWidth, fieldHeight);

				int secondPlayerY = (int) (height * 0.67);
				second_player_field.setBounds(leftX, secondPlayerY, fieldWidth, fieldHeight);

				int rightX = (int) (width * 0.7);
				int difficultyY = (int) (height * 0.50);
				difficulty_combo.setBounds(rightX, difficultyY, fieldWidth, fieldHeight);

				int buttonWidth = 160;
				int buttonHeight = 40;
				int buttonsY = (int) (height * 0.69);
				start_button.setBounds((width / 2 - buttonWidth + 69), buttonsY, buttonWidth, buttonHeight);
				history_button.setBounds((width / 2 + 229), buttonsY, buttonWidth, buttonHeight);
			}
		});
		// Add ActionListeners to the buttons
		start_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String firstPlayerName = first_player_field.getText().trim();
				String secondPlayerName = second_player_field.getText().trim();
				String selectedDifficulty = (String) difficulty_combo.getSelectedItem();
				level = Level.valueOf(selectedDifficulty.toUpperCase());

				flag = true;

				// Validations
				if (firstPlayerName.isEmpty()) {
					JOptionPane.showMessageDialog(main_frame, "Please enter the name of the first player.",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (secondPlayerName.isEmpty()) {
					JOptionPane.showMessageDialog(main_frame, "Please enter the name of the second player.",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (selectedDifficulty == null || selectedDifficulty.isEmpty()) {
					JOptionPane.showMessageDialog(main_frame, "Please select a difficulty level.", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				main_frame.dispose();

				// If all inputs are valid, start the game
				System.out.println("Starting game with:");
				System.out.println("First Player: " + firstPlayerName);
				System.out.println("Second Player: " + secondPlayerName);
				System.out.println("Difficulty: " + selectedDifficulty);

			}
		});
		main_frame.setVisible(true);
		while (true) {
			// בדוק כאן אם קרה משהו
			if (flag) {
				Game game = new Game(level);
				game.start();
				System.out.println("Something happened!");
				break; // או בצע פעולה אחרת
			}

			try {
				Thread.sleep(100); // המתן 100ms כדי לא להעמיס על המעבד
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
