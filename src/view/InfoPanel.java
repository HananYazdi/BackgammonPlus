package view;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Game;

public class InfoPanel extends JPanel {
	private Game game;
	private JLabel rolls;
	private JLabel activePlayer;
	private JLabel scores;
	private JLabel time;

	public InfoPanel(Game game) {
		this.game = game;

		activePlayer = new JLabel();
		rolls = new JLabel();
		scores = new JLabel();
		time = new JLabel();

		add(scores);
		add(activePlayer);
		add(rolls);
		add(time);
	}

	public void updateInfo() {

		int[] r = game.getRolls();

		StringBuilder rollText = new StringBuilder("Moves: ");
		int[] s = new int[2];
		s[0] = game.getP1().getScore();
		s[1] = game.getP2().getScore();
		StringBuilder scoreText = new StringBuilder(
				"Score " + game.getP1().getName() + ", " + game.getP2().getName() + ": ");
		String t = game.updateTimerDisplay();
		StringBuilder timeText = new StringBuilder("Time: ");
		timeText.append(t);

		for (int score : s) {
			scoreText.append(score);
			scoreText.append(", ");
		}

		for (int roll : r) {
			rollText.append(roll);
			rollText.append(", ");
		}
		rollText.setLength(rollText.length() - 2);
		scoreText.setLength(scoreText.length() - 2);

		scores.setText(scoreText.toString());
		rolls.setText(rollText.toString());
		time.setText(timeText.toString());
		activePlayer.setText(game.getActivePlayer().getName());

		repaint();
	}
}
