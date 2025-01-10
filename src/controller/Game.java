package controller;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import model.Board;
import model.HardBoard;
import model.Level;
import model.MediumBoard;
import model.Move;
import model.Player;
import model.SysData;
import model.SysData.GameHistory;
import model.SysData.Question;
import view.InfoPanel;
import view.PlayerColor;
import view.RollResultPopup;

public class Game {
	protected static Player p1;
	protected static Player p2;
	protected static Player activePlayer; // משתנה חדש לשחקן הפעיל
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

	public Game(Level difficulty, String name1, String name2) {
		this.p1 = new Player(PlayerColor.WHITE, name1, this); // שחקן ראשון עם הצבע לבן
		this.p2 = new Player(PlayerColor.BLACK, name2, this); // שחקן שני עם הצבע שחור
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
		p1.setName(name1);
		p2.setName(name2);
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

	public void getPlayers() {
//		p1 = new Player(PlayerColor.WHITE, "Player 1", this);
//		p2 = new Player(PlayerColor.BLACK, "Player 2", this);
		score[0] = p1.getScore();
		score[1] = p2.getScore();
		activePlayer = p1; // הגדרת שחקן פעיל
	}

	public Player getP1() {
		return p1;
	}

	public Player getP2() {
		return p2;
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

	    RollResultPopup.showDiceRoll(p1.getName(), p2.getName(), rolls);

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
		//SysData sysData = new SysData();
		do {
			boolean flag = false;
			if (rolls.length == 0) {
				if (board.countPiecesOut(activePlayer.getColor()) > 8) {
					int[] rolls;
					do {
						rolls = p1.RollTurn(); // הטלת הקוביות
					} while (rolls[0] == rolls[1]); // בדיקה אם הערכים זהים (דאבל)

					setRolls(rolls);
					processQuestionTurn();
				} else {
					setRolls(p1.RollTurn());
					processQuestionTurn();
				}

				// setRolls(p1.RollTurn());
			} else {

				processQuestionTurn();
			}

			possibleTurns = Move.getPossibleTurns(board, getActivePlayer().getColor(), rolls[0], rolls[1]);
			if (possibleTurns.isEmpty()) {
				JOptionPane.showMessageDialog(null,
						"you cant move " + rolls[0] + " " + rolls[1] + getActivePlayer().getColor().toString(),
						"Popup Title", JOptionPane.INFORMATION_MESSAGE);
				switchActivePlayer();
				possibleTurns = Move.getPossibleTurns(board, getActivePlayer().getColor(), rolls[0], rolls[1]);

			}
			if (rolls[0] == rolls[1]) {
				setRolls(new int[] { rolls[0], rolls[0], rolls[0], rolls[0] });
			}

			while (possibleTurns.size() != 0 || rolls.length != 0) {
				info.updateInfo();
				p1.selectMove(possibleTurns);
				if (Won(active)) {
					flag = true;
					break;
				}

				// TURN
			}

			if (Won(active))
				break;
			if (flag)
				break;
			if (board.isHasSpecial() == true) {
				board.setHasSpecial(false);
				playMediumTurn(active, opponent);
			} else
				switchActivePlayer();
		} while (true);

		end();
		// findScore(active, opponent);
	}

	private void playHardTurn(Player active, Player opponent) {
		SysData sysData = new SysData();
		do {
			boolean flag = false;
			if (rolls.length == 0) {
				if (board.countPiecesOut(activePlayer.getColor()) > 8) {
					int[] rolls;
					do {
						rolls = p1.RollEnhancedDiceTurn(); // הטלת הקוביות
					} while (rolls[0] == rolls[1]); // בדיקה אם הערכים זהים (דאבל)

					setRolls(rolls);
					processQuestionTurn();

				} else {
					setRolls(p1.RollEnhancedDiceTurn());
					processQuestionTurn();
				}

				// setRolls(p1.RollTurn());
			} else {

				processQuestionTurn();
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
				if (Won(active)) {
					flag = true;
					break;
				}
				// TURN
			}

			if (Won(active))
				break;
			if (flag)
				break;
			if (board.isHasSpecial() == true) {
				board.setHasSpecial(false);
				playHardTurn(active, opponent);
			} else
				switchActivePlayer();
		} while (true);

		end();
		// findScore(active, opponent);
	}

	private void playEasyTurn(Player active, Player opponent) {
		do {
			boolean flag = false;
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
				if (Won(active)) {
					flag = true;
					break;
				}

				// TURN
			}

			if (Won(active))
				break;
			if (flag)
				break;
			if (board.isHasSpecial() == true) {
				board.setHasSpecial(false);
				playEasyTurn(active, opponent);
			} else
				switchActivePlayer();
		} while (true);

		end();
		// findScore(active, opponent);
	}

	public static String secondsToTimeFormat(int seconds) {
		int minutes = seconds / 60; // חישוב הדקות
		seconds = seconds % 60; // חישוב השניות שנותרו
		return String.format("%02d:%02d", minutes, seconds); // פורמט של MM:SS
	}
	

	public void end() {
		if (gameTimer != null) {
			gameTimer.stop(); // Stop the timer when the game ends
		}
		System.out.println("Game ended. Total time: " + elapsedTime + " seconds");
	//	JButton replay = new JButton("Replay?");
	//	replay.setFont(new Font("Arial", Font.PLAIN, 40));
	//	board.setVisible(false);
	//	frame.add(replay, BorderLayout.CENTER);
		//frame.repaint();
		String winner = getWinner(p1, p2);
		
		
	    // יצירת תווית שתציג את המנצח
	    JLabel winnerLabel = new JLabel("Winner: " + winner);
	    winnerLabel.setFont(new Font("Arial", Font.PLAIN, 40));
	    winnerLabel.setHorizontalAlignment(JLabel.CENTER);

	    // הסתרת הלוח והוספת תווית המנצח למסך
	    board.setVisible(false);
	    frame.add(winnerLabel, BorderLayout.CENTER);
	    frame.repaint();
	    
		GameHistory history = new GameHistory(p1.getName(), p2.getName(), winner, difficulty,
				secondsToTimeFormat(elapsedTime), p1.getScore(), p2.getScore());
		SysData sysData = new SysData();
		sysData.addGameHistory(history);

	//	replay.addActionListener(new ActionListener() {
	//		@Override
	//		public void actionPerformed(ActionEvent e) {
	//			setupFrame();
	//			start();
	//		}
	//	});
	}

	public String getWinner(Player firstPlayer, Player secondPlayer) {
		// בודק אם השחקן הראשון ניצח
		if (Won(firstPlayer)) {
			return firstPlayer.getName(); // מחזיר את שם השחקן הראשון
		}

		// בודק אם השחקן השני ניצח
		if (Won(secondPlayer)) {
			return secondPlayer.getName(); // מחזיר את שם השחקן השני
		}

		// אם אף שחקן לא ניצח
		return "No winner yet";
	}

	public void switchActivePlayer() {
		// מחליף את השחקן הפעיל
		activePlayer = (activePlayer == p1) ? p2 : p1;
	}

	protected boolean Won(Player p) {
		boolean f = board.getBearOff().getCount(activePlayer.getColor()) == 15;
		return board.getBearOff().getCount(activePlayer.getColor()) == 15;
		// return board.countPiecesAtHome(p.getColor());
		// return board.countPieces(p.getColor()) == 0;
	}

	public int[] getScores() {
		return score;
	}

	public void setScore(int[] score) {
		this.p1.setScore(score[0]);
		this.p2.setScore(score[1]);
		info.updateInfo();
	}

//	public void findScore(Player p, Player op) {
//		if (board.countPieces(op.getColor()) > 15 && !board.checkHome(op.getColor())) {
//			p.setScore(3);
//		} else {
//			if (board.countPieces(op.getColor()) > 15 && board.checkHome(op.getColor())) {
//				p.setScore(2);
//			} else {
//				p.setScore(1);
//			}
//		}
//	}

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

	private static void displayPopup(List<Question> listOfQuestions, List<String> filteredQuestions) {
	    Random random = new Random();
	    int randomIndex = random.nextInt(filteredQuestions.size());  // Choose a random question from filtered list
	    String questionText = filteredQuestions.get(randomIndex);

	    // Find the Question object that matches the selected question text
	    Question selectedQuestion = null;
	    for (Question q : listOfQuestions) {
	        if (q.getQuestion().equals(questionText)) {
	            selectedQuestion = q;
	            break;
	        }
	    }

	    if (selectedQuestion == null) {
	        System.out.println("Selected question not found!");
	        return;
	    }

	    // Extract answers and the correct answer index from the selected question
	    List<String> answers = selectedQuestion.getAnswers();
	    int correctAnsIndex = selectedQuestion.getCorrectAns(); // Assuming it's a 1-based index

	    StringBuilder message = new StringBuilder("Question: " + questionText + "\n\n");

	    // Prepare the answer options for the user
	    String[] options = new String[answers.size()];
	    for (int i = 0; i < answers.size(); i++) {
	        options[i] = String.valueOf(i + 1);
	        message.append((i + 1) + ". " + answers.get(i) + "\n");
	    }

	    // Show the question in a popup with options
	    int userChoice = JOptionPane.showOptionDialog(null, message.toString(),
	            "Filtered Questions", JOptionPane.DEFAULT_OPTION,
	            JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

	    // Check if the user selected the correct answer
	    if (userChoice == correctAnsIndex - 1) {
	        if (activePlayer.equals(p1)) {
	        	p1.setScore(p1.getScore() + 1);
	        } else {
	        	p2.setScore(p2.getScore() + 1);
	        }
	        JOptionPane.showMessageDialog(null, "Correct answer!", "Result", JOptionPane.INFORMATION_MESSAGE);
	        Board.playSound("sound\\success.wav");
	    } else {
	        if (activePlayer.equals(p1)) {
	        	p1.setScore(p1.getScore() -1);
	        } else {
	        	p2.setScore(p2.getScore() - 1);
	        }
	        JOptionPane.showMessageDialog(null,
	                "Incorrect answer! The correct answer was: " + options[correctAnsIndex - 1], "Result",
	                JOptionPane.ERROR_MESSAGE);
	        Board.playSound("sound\\fail.wav");
	    }
	}


	// Method to find the question object in the JSON by the question text
	private static JsonObject findQuestionObject(JsonArray questionsArray, String questionText) {
		for (JsonElement element : questionsArray) {
			JsonObject questionObject = element.getAsJsonObject();
			String question = questionObject.get("question").getAsString();
			if (question.equals(questionText)) {
				return questionObject;
			}
		}
		return null; // Should never happen if the data is correct
	}
	//function for the processQuestionTurn
	public void processQuestionTurn() {
		SysData sysData = new SysData();
	    try {
	        // Roll the question turn to get the difficulty level
	        int difficulty = p1.RollQuestionTurn();
	        
	        // Set the question roll (this might involve some side-effect like UI or game state update)
	        setQuestionRoll(difficulty);
	        
	        // Retrieve the list of questions filtered by the selected difficulty
	        List<Question> listOfQuestions = sysData.getQuestionsByDifficulty(difficulty);

	        // If no questions were found, throw an exception (optional, for robust error handling)
	        if (listOfQuestions == null || listOfQuestions.isEmpty()) {
	            throw new Exception("No questions available for this difficulty.");
	        }
	        
	        // Create a list of the filtered question texts
	        List<String> filteredQuestions = new ArrayList<>();
	        for (Question q : listOfQuestions) {
	            if (q.getQuestion() == null) {
	                throw new Exception("Question text is missing for one or more questions.");
	            }
	            filteredQuestions.add(q.getQuestion()); // Assuming getQuestion() is available
	        }

	        // Display the popup for the questions
	        displayPopup(listOfQuestions, filteredQuestions);

	    } catch (NullPointerException e) {
	        // Handle specific NullPointerExceptions (e.g., null data in sysData or Question objects)
	        JOptionPane.showMessageDialog(null, "Error: A required value was missing. Please check the data.", 
	                                      "Error", JOptionPane.ERROR_MESSAGE);
	        e.printStackTrace();
	    } catch (Exception e) {
	        // Catch all other exceptions and provide a generic error message
	        JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage(), 
	                                      "Error", JOptionPane.ERROR_MESSAGE);
	        e.printStackTrace();
	    }
	}

}
