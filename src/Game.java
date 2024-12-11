import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game {
	private Player p1;
	private Player p2;
	private Player activePlayer; // משתנה חדש לשחקן הפעיל
	private Board board;
	private int[] rolls;
	private ArrayList<Turn> possibleTurns;
	private int[] score = new int[2];
	private Timer gameTimer; // Timer to track the game duration
	private int elapsedTime = 0; // Elapsed time in seconds

	private JFrame frame;
	private InfoPanel info;

	public Game() {
		board = new Board(this);
		setupFrame();
	}

	public void setupFrame() {
		frame = new javax.swing.JFrame("Backgammon");
		frame.setSize(860, 690);
		frame.setBackground(new java.awt.Color(12, 64, 8));
		frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

		info = new InfoPanel(this);

		frame.add(info, BorderLayout.SOUTH);
		frame.add(board, BorderLayout.CENTER);
		frame.setVisible(true);
	}

	private void getPlayers() {
		p1 = new Player(PlayerColor.WHITE, "Player 1", this);
		p2 = new Player(PlayerColor.BLACK, "Player 2", this);
		score[0] = p1.getScore();
		score[1] = p2.getScore();
		activePlayer = p1; // הגדרת שחקן פעיל
	}

	public Player getActivePlayer() {
		return activePlayer; // מחזיר את השחקן הפעיל
	}

	public int getelapsedTime() {
		return elapsedTime;
	}

	public Board getBoard() {
		return board;
	}

	public InfoPanel getInfoPanel() {
		return info;
	}

	public int[] getRolls() {
		return rolls;
	}

	public void setRolls(int[] rolls) {
		this.rolls = rolls;
		info.updateInfo();
	}

	public ArrayList<Turn> getPossibleTurns() {
		return possibleTurns;
	}

	public void setPossibleTurns(ArrayList<Turn> turns) {
		possibleTurns = turns;
	}

	private void startGameTimer() {
		gameTimer = new Timer(1000, new ActionListener() { // Fires every second
			@Override
			public void actionPerformed(ActionEvent e) {
				elapsedTime++;
				updateTimerDisplay(); // Update the timer display
			}
		});
		gameTimer.start();
	}

	public String updateTimerDisplay() {
		int minutes = elapsedTime / 60;
		int seconds = elapsedTime % 60;
		String timeString = String.format("Game Time: %02d:%02d", minutes, seconds);
		return timeString;

	}

	public void start() {
		getPlayers();
		board.setInitialBoard();
		startGameTimer();

		do {
			setRolls(new int[] { p1.firstRoll(), p2.firstRoll() });
		} while (rolls[0] == rolls[1]);

		if (rolls[0] < rolls[1])
			switchActivePlayer();

		info.updateInfo();
		Turn(p1, p2);
	}

	public void Turn(Player active, Player opponent) {
		do {
			if (rolls.length == 0) {
				if (board.countPiecesOut(activePlayer.getColor()) > 8) {
					int[] rolls;
					do {
						rolls = p1.RollTurn(); // הטלת הקוביות
					} while (rolls[0] == rolls[1]); // בדיקה אם הערכים זהים (דאבל)

					setRolls(rolls);
				} else {
					setRolls(p1.RollTurn());
				}

				// setRolls(p1.RollTurn());
			}

			possibleTurns = Move.getPossibleTurns(board, getActivePlayer().getColor(), rolls[0], rolls[1]);
			if (possibleTurns.isEmpty()) {
				JOptionPane.showMessageDialog(null,
						"you cant move" + rolls[0] + rolls[1] + getActivePlayer().getColor().toString(), "Popup Title",
						JOptionPane.INFORMATION_MESSAGE);
				switchActivePlayer();
				possibleTurns = Move.getPossibleTurns(board, getActivePlayer().getColor(), rolls[0], rolls[1]);

			}
			if (rolls[0] == rolls[1]) {
				setRolls(new int[] { rolls[0], rolls[0], rolls[0], rolls[0] });
			}

			while (possibleTurns.size() != 0 || rolls.length != 0) {
				info.updateInfo();
				p1.selectMove(possibleTurns);
				// TURN
			}

			if (Won(active))
				break;
			switchActivePlayer();
		} while (true);

		end();
		findScore(active, opponent);
	}

	public void end() {
		if (gameTimer != null) {
			gameTimer.stop(); // Stop the timer when the game ends
		}
		System.out.println("Game ended. Total time: " + elapsedTime + " seconds");

		JButton replay = new JButton("Replay?");
		replay.setFont(new Font("Arial", Font.PLAIN, 40));
		board.setVisible(false);
		frame.add(replay, BorderLayout.CENTER);
		frame.repaint();
		replay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setupFrame();
				start();
			}
		});
	}

	private void switchActivePlayer() {
		// מחליף את השחקן הפעיל
		activePlayer = (activePlayer == p1) ? p2 : p1;
	}

	private boolean Won(Player p) {
		return board.countPiecesAtHome(p.getColor());
		// return board.countPieces(p.getColor()) == 0;
	}

	public int[] getScores() {
		return score;
	}

	public void findScore(Player p, Player op) {
		if (board.countPieces(op.getColor()) > 15 && !board.checkHome(op.getColor())) {
			p.setScore(3);
		} else {
			if (board.countPieces(op.getColor()) > 15 && board.checkHome(op.getColor())) {
				p.setScore(2);
			} else {
				p.setScore(1);
			}
		}
	}

	public void choosePlayer() { // work in progress chooseplayer code
		JFrame c = new JFrame("Choose Player ");
		c.setSize(800, 700);
		JPanel p = new JPanel();
		JButton choosePlayer = new JButton("Two Players");
		JButton chooseComputer = new JButton("Computer Player");
		p.add(choosePlayer, BorderLayout.CENTER);
		p.add(chooseComputer, BorderLayout.CENTER);
		c.add(p, BorderLayout.CENTER);
		choosePlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getPlayers();
				c.dispose();
//               start();
			}
		});
		chooseComputer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//            getComputerPlayers();
				c.dispose();
//               start();
			}
		});
		c.setVisible(true);
	}
}
