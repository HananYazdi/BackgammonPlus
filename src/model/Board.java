package model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.sound.sampled.*;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import controller.Game;
import controller.Turn;
import model.SysData.Question;
import view.Palette;
import view.PlayerColor;

public class Board extends JPanel {
	protected static Game game = null;
	private final Triangle[] points;
	private final Bar bar;
	private final Bar bearOff;
	private Position selectedPosition;
	protected static ArrayList<Integer> placesQuestion;// for places questions
	protected static Integer placeSurprise;// for surprise place
	protected boolean hasSpecial = false;

	public boolean isHasSpecial() {
		return hasSpecial;
	}

	public void setHasSpecial(boolean hasSpecial) {
		this.hasSpecial = hasSpecial;
	}

	public Triangle[] getPoints() {
		return points;
	}

	public Board(Game game) {
		super(null, true);
		this.game = game;
		points = new Triangle[24];
		bar = new Bar(this, 1);
		bearOff = new Bar(this, 0);
		add(bar);
		add(bearOff);

		setSize(getGeometry().getBoardWidth(), getGeometry().getBoardHeight());
		setMaximumSize(new Dimension(getGeometry().getBoardWidth(), getGeometry().getBoardHeight()));
		setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.BLACK));
		setBackground(Palette.getBoardBackgroundColour());

		for (int i = 0; i < points.length; i++) {
			points[i] = new Triangle(0, null, i + 1, this);
		}

		placesQuestion = new ArrayList<Integer>();
		placeSurprise = -1;
	}

	public static BoardGeometry getGeometry() {
		return BoardGeometry.getBoardGeometry();
	}

	public Game getGame() {
		return game;
	}

	public Bar getBar() {
		return bar;
	}

	public Bar getBearOff() {
		return bearOff;
	}

	public Triangle getPoint(int num) {
		return points[num - 1];
	}

	public void setSelectedPosition(Position p) {
		selectedPosition = p;
	}

	public Position getSelectedPosition() {
		return selectedPosition;
	}

	public void setInitialBoard() {
		for (int i = 0; i < points.length; i++) {
			switch (i) {
			case 0:
				points[i].setCountAndColor(5, PlayerColor.BLACK);
				break;
			case 4:
				points[i].setCountAndColor(3, PlayerColor.WHITE);
				break;
			case 12:
				points[i].setCountAndColor(2, PlayerColor.WHITE);
				break;
			case 6:
				points[i].setCountAndColor(5, PlayerColor.WHITE);
				break;
			case 11:
				points[i].setCountAndColor(2, PlayerColor.BLACK);
				break;
			case 17:
				points[i].setCountAndColor(5, PlayerColor.BLACK);
				break;
			case 19:
				points[i].setCountAndColor(3, PlayerColor.BLACK);
				break;
			case 23:
				points[i].setCountAndColor(5, PlayerColor.WHITE);
				break;
			default:
				points[i].setCountAndColor(0, null);
			}
		}

		int[] numbers = IsEmptyPlace(points);
		setPlacesToQuestion(numbers);// places for question
		int[] QuestionBars = toIntArray(placesQuestion);
		paintQuestion(QuestionBars);
		int[] result = subtractArrays(numbers, QuestionBars);// for special bar only places that are still available
		setPlacesToSurprise(result);// place for surprise
		points[placeSurprise].setHasSurpriseMark(true);// for suprise mark
		points[placeSurprise].repaint();// to add the paint

	}

	public static int[] toIntArray(ArrayList<Integer> list) {
		// Initialize an int[] with the size of the list
		int[] result = new int[list.size()];

		// Copy each element from the ArrayList to the int array
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}

		return result;
	}

	public static void setPlacesToQuestion(int[] numbers) {
		Random random = new Random();
		Set<Integer> chosenNumbers = new HashSet<>();
		placesQuestion = new ArrayList<Integer>();
		// Randomly pick 3 distinct numbers
		while (chosenNumbers.size() < 3) {
			int randomIndex = random.nextInt(numbers.length);
			chosenNumbers.add(numbers[randomIndex]); // Add to the set
			System.out.println(numbers[randomIndex]);
			placesQuestion.add(numbers[randomIndex]);
		}
	}

	public static void setPlacesToSurprise(int[] numbers) {
		Random random = new Random();

		int randomIndex = random.nextInt(numbers.length);
		placeSurprise = (numbers[randomIndex]); // Add to the set
	}

	// function for question mark paint when it needed
	public void paintQuestion(int[] QuestionBars) {
		System.out.println("entered");
		for (int i = 0; i < QuestionBars.length; i++) {
			System.out.println("i=" + i);
			points[QuestionBars[i]].setHasQuestionMark(true);
			points[QuestionBars[i]].repaint();
		}
	}

	// to know where i can put it
	public int[] IsEmptyPlace(Triangle[] places) {
		ArrayList<Integer> emptyIndices = new ArrayList<>();

		// Iterate through the array to check for emptiness
		for (int i = 0; i < places.length; i++) {
			if (places[i].getCount() == 0) { // when the bar is empty
				emptyIndices.add(i);
			}
		}

		// Convert the list to an array and return
		return emptyIndices.stream().mapToInt(Integer::intValue).toArray();
	}

	// to know where i can put the special bar
	public static int[] subtractArrays(int[] array1, int[] array2) {
		// Convert array1 to a list for easier manipulation
		List<Integer> list = new ArrayList<>();
		for (int num : array1) {
			list.add(num);
		}

		// Remove all elements from array2
		for (int num : array2) {
			list.remove(Integer.valueOf(num));
		}

		// Convert the list back to an array
		int[] result = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}

		return result;
	}

	public boolean checkHome(PlayerColor c) { // from liams score increment code

		if (c == PlayerColor.BLACK) {
			for (int x = 0; x <= 11; x++) {

				if (points[x].getCount() > 0 && points[x].getPieceColor() == PlayerColor.BLACK) {

					return false;
				}
			}
			for (int x = 18; x <= 23; x++) {

				if (points[x].getCount() > 0 && points[x].getPieceColor() == PlayerColor.BLACK) {

					return false;
				}
			}

		}

		if (c == PlayerColor.WHITE) {
			for (int x = 0; x <= 5; x++) {

				if (points[x].getCount() > 0 && points[x].getPieceColor() == PlayerColor.WHITE) {

					return false;
				}
			}
			for (int x = 12; x <= 23; x++) {

				if (points[x].getCount() > 0 && points[x].getPieceColor() == PlayerColor.WHITE) {

					return false;
				}
			}
		}
		return true;
	}

	public int countPieces(PlayerColor c) {
		int count = bar.getCount(c);

		for (Triangle point : points) {
			if (point.getPieceColor() == c)
				count += point.getCount();
		}

		return count;
	}

	public int countPiecesOut(PlayerColor c) {
		int count = bearOff.getCount(c);
		return count;
	}

	public boolean countPiecesAtHome(PlayerColor c) {
		if (c == PlayerColor.WHITE) {
			for (int x = 0; x <= 23; x++) {

				if (points[x].getCount() != 0 && points[x].getPieceColor() == PlayerColor.WHITE) {
					return false;
				}
			}
		}
		if (c == PlayerColor.BLACK) {
			for (int x = 0; x <= 23; x++) {

				if (points[x].getCount() != 0 && points[x].getPieceColor() == PlayerColor.BLACK) {
					return false;
				}
			}
		}
		return true;
	}

	public void makeMove(Position from, Position to) {
		ArrayList<Turn> turns = game.getPossibleTurns();

		boolean flag = true;

		for (Turn turn : turns) {
			if (turn.getMoves()[0].getFrom() == from && turn.getMoves()[0].getTo() == to)
				flag = false;
		}
		if (flag)
			return;

		int[] rolls = getGame().getRolls();

		ArrayList<Integer> temp = new ArrayList<>();

		boolean moveFound = false;
		for (int i = 0; i < rolls.length; i++) {

			int rollDifference = Math.abs(from.getPointNumber() - to.getPointNumber());

			if (getGame().getActivePlayer().getColor().equals(PlayerColor.WHITE)) {

				if (rollDifference > 6) {

					rollDifference = to.getPointNumber() - from.getPointNumber() + 24;

				}
				if (from.getPointNumber() == 25 || from.getPointNumber() == 0) {
					rollDifference = to.getPointNumber() - 12;

				}
				if (to.getPointNumber() == 25 || to.getPointNumber() == 0) {
					// rollDifference = rolls[i];
					if (from.getPointNumber() == 12) {
						if (points[6].getCount() == 0 && points[7].getCount() == 0 && points[8].getCount() == 0
								&& points[9].getCount() == 0 && points[10].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 1) {
									rollDifference = 1;
								} else {
									rollDifference = Math.max(rolls[i + 1], rolls[i]);
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 1;
						}

					}
					if (from.getPointNumber() == 11) {
						if (points[6].getCount() == 0 && points[7].getCount() == 0 && points[8].getCount() == 0
								&& points[9].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 2) {
									rollDifference = 2;
								} else {
									rollDifference = Math.max(rolls[i + 1], rolls[i]);
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 2;
						}
					}
					if (from.getPointNumber() == 10) {
						if (points[6].getCount() == 0 && points[7].getCount() == 0 && points[8].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 3) {
									rollDifference = 3;
								} else {
									rollDifference = Math.max(rolls[i + 1], rolls[i]);
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 3;
						}
					}
					if (from.getPointNumber() == 9) {
						if (points[6].getCount() == 0 && points[7].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 4) {
									rollDifference = 4;
								} else {
									rollDifference = Math.max(rolls[i + 1], rolls[i]);
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 4;
						}
					}
					if (from.getPointNumber() == 8) {
						if (points[6].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 5) {
									rollDifference = 5;
								} else {
									rollDifference = Math.max(rolls[i + 1], rolls[i]);
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 5;
						}
					}
					if (from.getPointNumber() == 7) {
						rollDifference = 6;
					}
				}
				if (from.getPointNumber() > to.getPointNumber()
						&& Math.abs(to.getPointNumber() - from.getPointNumber()) <= 6) {
					rollDifference = rollDifference * (-1);
				}
				if (from.getPointNumber() < to.getPointNumber()
						&& Math.abs(to.getPointNumber() - from.getPointNumber()) > 6) {
					rollDifference = 24 - to.getPointNumber() + from.getPointNumber();
					rollDifference = rollDifference * (-1);
				}

			} else {
				if (rollDifference > 6) {
					rollDifference = 24 - (to.getPointNumber() - from.getPointNumber());

				}
				if (from.getPointNumber() == 0 || from.getPointNumber() == 25) {
					rollDifference = 13 - to.getPointNumber();

				}
				if (to.getPointNumber() == 25 || to.getPointNumber() == 0) {
					// rollDifference = rolls[i];

					if (from.getPointNumber() == 13) {
						if (points[17].getCount() == 0 && points[16].getCount() == 0 && points[15].getCount() == 0
								&& points[14].getCount() == 0 && points[13].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 1) {
									rollDifference = 1;
								} else {
									rollDifference = Math.max(rolls[i + 1], rolls[i]);
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 1;
						}
					}
					if (from.getPointNumber() == 14) {
						if (points[17].getCount() == 0 && points[16].getCount() == 0 && points[15].getCount() == 0
								&& points[14].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 2) {
									rollDifference = 2;
								} else {
									rollDifference = Math.max(rolls[i + 1], rolls[i]);
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 2;
						}
					}
					if (from.getPointNumber() == 15) {
						if (points[17].getCount() == 0 && points[16].getCount() == 0 && points[15].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 3) {
									rollDifference = 3;
								} else {
									rollDifference = Math.max(rolls[i + 1], rolls[i]);
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 3;
						}
					}
					if (from.getPointNumber() == 16) {
						if (points[17].getCount() == 0 && points[16].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 4) {
									rollDifference = 4;
								} else {
									rollDifference = Math.max(rolls[i + 1], rolls[i]);
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 4;
						}
					}
					if (from.getPointNumber() == 17) {
						if (points[17].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 5) {
									rollDifference = 5;
								} else {
									rollDifference = Math.max(rolls[i + 1], rolls[i]);
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 5;
						}
					}
					if (from.getPointNumber() == 18) {
						rollDifference = 6;
					}
				}
				if (to.getPointNumber() > from.getPointNumber()
						&& Math.abs(to.getPointNumber() - from.getPointNumber()) <= 6) {
					rollDifference = rollDifference * (-1);
				}

			}

			if (rolls[i] == rollDifference && !moveFound) {
				moveFound = true; // אחרי שמצאנו את הצעד הראשון, נשאיר את שאר הצעדים
			} else {
				temp.add(rolls[i]);
			}
		}

		// המרת temp חזרה למערך אם יש צורך
		int[] updatedRolls = new int[temp.size()];
		for (int i = 0; i < temp.size(); i++) {
			updatedRolls[i] = temp.get(i);
		}

		System.out.println("Updated rolls: " + Arrays.toString(updatedRolls));
		// System.out.println("Updated board: " + game.getBoard().toString());

		// עדכון הגלגלים החדשים
		game.setRolls(updatedRolls);

		// עדכון הלוח (הזזת החלקים)
		from.removePiece(getGame().getActivePlayer().getColor());
		to.addPiece(getGame().getActivePlayer().getColor());

		// עדכון הצעדים האפשריים
		if (to.getPointNumber() != 0 && to.getPointNumber() != 25) {
			if (points[to.getPointNumber() - 1].isHasSurpriseMark())// has special turn
			{
				hasSpecial = true;
				points[to.getPointNumber() - 1].setHasSurpriseMark(false);
				points[to.getPointNumber() - 1].repaint();
			}
		}
		if (to.getPointNumber() != 0 && to.getPointNumber() != 25) {
			if (points[to.getPointNumber() - 1].isHasQuestionMark()) {
				QuestionDice d = new QuestionDice();
				int difficulty = d.roll();
				  try {
				        // Create the SysData object, which may throw exceptions if there is an issue during initialization
				        SysData sysData = new SysData();
				        
				        // Retrieve the list of questions by difficulty, this could throw an exception if the data is unavailable
				        List<Question> listOfQuestions = sysData.getQuestionsByDifficulty(difficulty);

				        // Check if no questions were found
				        if (listOfQuestions == null || listOfQuestions.isEmpty()) {
				            throw new Exception("No questions found for the given difficulty.");
				        }

				        // Create a list of filtered question texts
				        List<String> filteredQuestions = new ArrayList<>();
				        for (Question q : listOfQuestions) {
				            // Ensure each question text is available (not null)
				            if (q.getQuestion() == null) {
				                throw new Exception("A question text is missing for one of the questions.");
				            }
				            filteredQuestions.add(q.getQuestion()); // Add the question text to the list
				        }

				        // Proceed to display the popup with the list of questions and their texts
				        displayPopup(listOfQuestions, filteredQuestions);

				    } catch (NullPointerException e) {
				        // Handle specific cases like null data or objects
				        JOptionPane.showMessageDialog(null, "Error: A null value was encountered. Please check the data.",
				                                      "Error", JOptionPane.ERROR_MESSAGE);
				        e.printStackTrace();
				    } catch (Exception e) {
				        // Handle general exceptions (like no questions found, or missing question text)
				        JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage(),
				                                      "Error", JOptionPane.ERROR_MESSAGE);
				        e.printStackTrace();
				    }
				/*try {
					// Define the path to your JSON file in the project (e.g.,
					// src/questions_scheme.json)
					FileReader jsonFile = new FileReader("questions_scheme.json");

					// Parse the JSON file using Gson
					Gson gson = new Gson();
					JsonObject rootObject = gson.fromJson(jsonFile, JsonObject.class);

					// Get the "questions" array from the root object
					JsonArray questionsArray = rootObject.getAsJsonArray("questions");

					// Define the target difficulty level (as an integer)
					// int targetDifficulty = 1; // Example: difficulty = 1

					// Get filtered questions based on difficulty
					List<String> filteredQuestions = getQuestionsByDifficulty(questionsArray, difficulty);

					// Output the filtered questions in a popup
					displayPopup(questionsArray, filteredQuestions, difficulty);

				} catch (IOException e) {
					e.printStackTrace();
				}*/
			}
		}
		game.setPossibleTurns(Move.reducePossibleTurns(this, turns, from, to));

		// הצגת הלוח מחדש
		repaint();
	}

	// Method to filter questions based on difficulty
	/*private static List<String> getQuestionsByDifficulty(JsonArray questionsArray, int targetDifficulty) {
		List<String> filteredQuestions = new ArrayList<>();

		// Loop through each question object in the array
		for (JsonElement questionElement : questionsArray) {
			JsonObject questionObject = questionElement.getAsJsonObject();
			int difficulty = questionObject.get("difficulty").getAsInt(); // Get the difficulty

			// If the difficulty matches the target difficulty, add the question to the list
			if (difficulty == targetDifficulty) {
				String question = questionObject.get("question").getAsString(); // Get the question text
				filteredQuestions.add(question);
			}
		}
		return filteredQuestions;
	}*/
	
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
	        if (game.getActivePlayer().equals(game.getP1())) {
	            game.getP1().setScore(game.getP1().getScore() + 1);
	        } else {
	            game.getP2().setScore(game.getP2().getScore() + 1);
	        }
	        JOptionPane.showMessageDialog(null, "Correct answer!", "Result", JOptionPane.INFORMATION_MESSAGE);
	        playSound("sound\\success.wav");
	    } else {
	        if (game.getActivePlayer().equals(game.getP1())) {
	            game.getP1().setScore(game.getP1().getScore() - 1);
	        } else {
	            game.getP2().setScore(game.getP2().getScore() - 1);
	        }
	        playSound("sound\\fail.wav");
	        JOptionPane.showMessageDialog(null,
	                "Incorrect answer! The correct answer was: " + options[correctAnsIndex - 1], "Result",
	                JOptionPane.ERROR_MESSAGE);
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
	
	
	public static void playSound(String soundFile) {
        try {
            // Get the current working directory
            String baseDir = System.getProperty("user.dir");
           
            
            // Show the full path for debugging (optional)
            // Check if the file exists before attempting to play
            File file = new File(baseDir + File.separator + soundFile);
            if (!file.exists()) {
                file = new File(baseDir + File.separator + "src\\" + File.separator + soundFile);
            }
            if (!file.exists()) {
                throw new IllegalArgumentException("Sound file not found: " + baseDir);
            }

            // Wrap FileInputStream in BufferedInputStream
            try (InputStream audioSrc = new BufferedInputStream(new FileInputStream(file))) {
                // Convert to AudioInputStream (supports WAV, AIFF, etc.)
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioSrc);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                
                // Optionally, wait until the clip is finished playing
                clip.drain();
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            // Display error message in case of failure
            JOptionPane.showMessageDialog(null, "Error playing sound: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
	
	// **************************************************
	// GRAPHICS
	// **************************************************

	public void highlightMoves(Color color) {
		ArrayList<Turn> turns = getGame().getPossibleTurns();
		for (Turn turn : turns) {
			if (turn.getMoves()[0].getFrom().getPointNumber() == selectedPosition.getPointNumber())

				turn.getMoves()[0].getTo().addHighlight(color);
		}
	}

	public void clearHighlights() {
		for (Triangle t : points)
			t.removeHighlight();
		bar.removeHighlight();
		bearOff.removeHighlight();
		repaint();
	}

	protected void drawDice(Graphics g, int value, int x, int y) {
		// Draw die background (white)
		g.setColor(Color.WHITE);
		g.fillRect(x, y, 40, 40);
		// Draw die outline
		g.setColor(Color.BLACK);
		g.drawRect(x, y, 40, 40);

		// Draw dots based on the value of the die
		g.setColor(Color.BLACK);
		switch (value) {
		case 1:
			g.fillOval(x + 18, y + 18, 4, 4);
			break;
		case 2:
			g.fillOval(x + 10, y + 10, 4, 4);
			g.fillOval(x + 26, y + 26, 4, 4);
			break;
		case 3:
			g.fillOval(x + 10, y + 10, 4, 4);
			g.fillOval(x + 18, y + 18, 4, 4);
			g.fillOval(x + 26, y + 26, 4, 4);
			break;
		case 4:
			g.fillOval(x + 10, y + 10, 4, 4);
			g.fillOval(x + 26, y + 10, 4, 4);
			g.fillOval(x + 10, y + 26, 4, 4);
			g.fillOval(x + 26, y + 26, 4, 4);
			break;
		case 5:
			g.fillOval(x + 10, y + 10, 4, 4);
			g.fillOval(x + 26, y + 10, 4, 4);
			g.fillOval(x + 18, y + 18, 4, 4);
			g.fillOval(x + 10, y + 26, 4, 4);
			g.fillOval(x + 26, y + 26, 4, 4);
			break;
		case 6:
			g.fillOval(x + 10, y + 10, 4, 4);
			g.fillOval(x + 26, y + 10, 4, 4);
			g.fillOval(x + 10, y + 26, 4, 4);
			g.fillOval(x + 26, y + 26, 4, 4);
			g.fillOval(x + 10, y + 18, 4, 4);
			g.fillOval(x + 26, y + 18, 4, 4);
			break;

		}
	}

	@Override
	protected void paintComponent(final Graphics g) {
		final Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		int die1;
		if (game != null && game.getRolls() != null) {
			if (game.getRolls().length == 1) {
				die1 = game.getRolls()[0];
				drawDice(g, die1, 250, 300);
			}
			// int die1 = game.getRolls()[0];
			int die2;
			if (game.getRolls().length == 2) {
				die2 = game.getRolls()[1];
				die1 = game.getRolls()[0];
				drawDice(g, die2, 670, 300);
				drawDice(g, die1, 250, 300);
			}
			if (game.getRolls().length == 3) {
				die2 = game.getRolls()[1];
				die1 = game.getRolls()[0];
				drawDice(g, die2, 670, 300);
				drawDice(g, die1, 250, 300);
			}
		}

	}
}