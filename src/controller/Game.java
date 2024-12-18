package controller;
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

import model.Board;
import model.HardBoard;
import model.Level;
import model.MediumBoard;
import model.Move;
import model.Player;
import view.InfoPanel;
import view.PlayerColor;

public class Game {
	protected Player p1;
	protected Player p2;
	protected Player activePlayer; // משתנה חדש לשחקן הפעיל
	protected Board board;
	protected int[] rolls;
	protected ArrayList<Turn> possibleTurns;
	protected int[] score = new int[2];
	protected Timer gameTimer; // Timer to track the game duration
	protected int elapsedTime = 0; // Elapsed time in seconds
	protected JFrame frame;
	protected InfoPanel info;
	private Level difficulty;
	protected int QuestionRoll;
	// protected int[] rollsEnhancedDice;

	public Game(Level difficulty) {
		this.difficulty = difficulty;
		if (difficulty == Level.EASY) {
			board = new Board(this);
		}
		if (difficulty == Level.MEDIUM) {
			board = new MediumBoard(this);
		}
		if (difficulty == Level.HARD) {
			board = new HardBoard(this);
		}

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

	public Level getDifficulty() {
		return difficulty;
	}

	public int[] getRolls() {
		return rolls;
	}

	public void setRolls(int[] rolls) {
		this.rolls = rolls;
		info.updateInfo();
	}

	public void setQuestionRoll(int QuestionRoll) {
		this.QuestionRoll = QuestionRoll;
	}

	public int getQuestionRoll() {
		return QuestionRoll;
	}

//	public void setEnhancedDiceRoll(int[] rollsEnhancedDice) {
//		this.rollsEnhancedDice = rollsEnhancedDice;
//		info.updateInfo();
//	}
//
//	public int[] getEnhancedDiceRoll() {
//		return rollsEnhancedDice;
//	}

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

		if (rolls[0] < rolls[1]) {
			switchActivePlayer();
		}

		setQuestionRoll(getActivePlayer().RollQuestionTurn());

		info.updateInfo();
		Turn(p1, p2);
	}

//	public void Turn(Player active, Player opponent) {
//		do {
//			if (rolls.length == 0) {
//				if (board.countPiecesOut(activePlayer.getColor()) > 8) {
//					int[] rolls;
//					do {
//						rolls = p1.RollTurn(); // הטלת הקוביות
//					} while (rolls[0] == rolls[1]); // בדיקה אם הערכים זהים (דאבל)
//
//					setRolls(rolls);
//				} else {
//					setRolls(p1.RollTurn());
//				}
//
//				// setRolls(p1.RollTurn());
//			}
//
//			possibleTurns = Move.getPossibleTurns(board, getActivePlayer().getColor(), rolls[0], rolls[1]);
//			if (possibleTurns.isEmpty()) {
//				JOptionPane.showMessageDialog(null,
//						"you cant move" + rolls[0] + rolls[1] + getActivePlayer().getColor().toString(), "Popup Title",
//						JOptionPane.INFORMATION_MESSAGE);
//				switchActivePlayer();
//				possibleTurns = Move.getPossibleTurns(board, getActivePlayer().getColor(), rolls[0], rolls[1]);
//
//			}
//			if (rolls[0] == rolls[1]) {
//				setRolls(new int[] { rolls[0], rolls[0], rolls[0], rolls[0] });
//			}
//
//			while (possibleTurns.size() != 0 || rolls.length != 0) {
//				info.updateInfo();
//				p1.selectMove(possibleTurns);
//				// TURN
//			}
//
//			if (Won(active))
//				break;
//			switchActivePlayer();
//		} while (true);
//
//		end();
//		findScore(active, opponent);
//	}
	public void Turn(Player active, Player opponent) {
		switch (difficulty) {
		case EASY:
			playEasyTurn(active, opponent);
			break;
		case MEDIUM:
			playMediumTurn(active, opponent);
			break;
		case HARD:
			playHardTurn(active, opponent);
			break;
		}
	}

	private void playMediumTurn(Player active, Player opponent) {
		do {
			if (rolls.length == 0) {
				if (board.countPiecesOut(activePlayer.getColor()) > 8) {
					int[] rolls;
					do {
						rolls = p1.RollTurn(); // הטלת הקוביות
					} while (rolls[0] == rolls[1]); // בדיקה אם הערכים זהים (דאבל)

					setRolls(rolls);
					setQuestionRoll(p1.RollQuestionTurn());

				} else {
					setRolls(p1.RollTurn());
					setQuestionRoll(p1.RollQuestionTurn());
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
			if(board.isHasSpecial()==true)
			{
				board.setHasSpecial(false);
				playEasyTurn(active,opponent);
			}
			else
				switchActivePlayer();
			switchActivePlayer();
		} while (true);

		end();
		findScore(active, opponent);
	}

	private void playHardTurn(Player active, Player opponent) {
		do {
			if (rolls.length == 0) {
				if (board.countPiecesOut(activePlayer.getColor()) > 8) {
					int[] rolls;
					do {
						rolls = p1.RollEnhancedDiceTurn(); // הטלת הקוביות
					} while (rolls[0] == rolls[1]); // בדיקה אם הערכים זהים (דאבל)

					setRolls(rolls);
					setQuestionRoll(p1.RollQuestionTurn());

				} else {
					setRolls(p1.RollEnhancedDiceTurn());
					setQuestionRoll(p1.RollQuestionTurn());
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
			if(board.isHasSpecial()==true)
			{
				board.setHasSpecial(false);
				playEasyTurn(active,opponent);
			}
			else
				switchActivePlayer();
			switchActivePlayer();
		} while (true);

		end();
		findScore(active, opponent);
	}

	private void playEasyTurn(Player active, Player opponent) {
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
			if(board.isHasSpecial()==true)
			{
				board.setHasSpecial(false);
				playEasyTurn(active,opponent);
			}
			else
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

	protected void switchActivePlayer() {
		// מחליף את השחקן הפעיל
		activePlayer = (activePlayer == p1) ? p2 : p1;
	}

	protected boolean Won(Player p) {
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
