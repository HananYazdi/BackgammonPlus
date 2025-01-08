package view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import model.SysData;
import model.SysData.Question;

public class QuestionsTableScreen {
	private static DefaultTableModel tableModel;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Questions Table");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

			// Panel with a custom background
			ImageIcon bgIcon = new ImageIcon(QuestionsTableScreen.class.getResource("/images/questions.png"));
			Image backgroundImg = bgIcon.getImage();

			JPanel backgroundPanel = new JPanel(null) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
				}
			};

			frame.setContentPane(backgroundPanel);

			SysData sysData = new SysData();
			List<Question> list = sysData.getQuestions();
			// Table data and columns
			String[] columns = { "Question", "Answer 1", "Answer 2", "Answer 3", "Answer 4", "Correct Answer",
					"Difficulty" };
//            Object[][] data = {
//                    {"What is the main purpose of software design?", "Create hardware", "Design structure", "Build user interface", "Write documentation", "Design structure", "Easy"},
//                    {"Which software development model is iterative?", "Waterfall", "Spiral", "Agile", "V-model", "Spiral", "Medium"},
//                    {"What is a 'bug' in software development?", "A feature", "A defect", "A new release", "A type of code", "A defect", "Easy"},
//                    {"Which principle is part of Object-Oriented Programming (OOP)?", "Inheritance", "Compilation", "Execution", "Networking", "Inheritance", "Medium"},
//                    {"What is the main goal of unit testing?", "Test the whole system", "Test individual components", "Test user experience", "Test final product", "Test individual components", "Hard"}
//            };
			Object[][] data = new Object[list.size()][7];
			for (int i = 0; i < list.size(); i++) {
				Question question = list.get(i);
				data[i][0] = question.getQuestion();
				data[i][1] = question.getAnswers().get(0);
				data[i][2] = question.getAnswers().get(1);
				data[i][3] = question.getAnswers().get(2);
				data[i][4] = question.getAnswers().get(3);
				data[i][5] = question.getCorrectAns();
				data[i][6] = question.getDifficulty();
			}
			JsonArray questionsArray = loadQuestionsFromJson("questions_scheme.json");
			// DefaultTableModel tableModel = new DefaultTableModel(data, columns);
			tableModel = createTableModelFromJson(questionsArray);
			JTable table = new JTable(tableModel);

			table.setRowHeight(30);
			table.setFont(new Font("Arial", Font.PLAIN, 16));

			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.setBounds(50, 200, 1100, 400);
			backgroundPanel.add(scrollPane);

			// Buttons
			JButton editButton = new JButton("EDIT");
			editButton.setBounds(45, 125, 100, 40);
			editButton.setFont(new Font("Arial", Font.BOLD, 12));
			backgroundPanel.add(editButton);

			JButton addButton = new JButton("ADD");
			addButton.setBounds(1130, 125, 100, 40);
			addButton.setFont(new Font("Arial", Font.BOLD, 12));
			backgroundPanel.add(addButton);

			JButton BackButton = new JButton("Back");
			BackButton.setBounds(50, 600, 100, 40);
			BackButton.setFont(new Font("Arial", Font.BOLD, 12));
			backgroundPanel.add(BackButton);

			JButton DeleteButton = new JButton("Delete");
			DeleteButton.setBounds(1050, 600, 100, 40);
			DeleteButton.setFont(new Font("Arial", Font.BOLD, 12));
			backgroundPanel.add(DeleteButton);

			JButton InfoButton = new JButton("INFO");
			InfoButton.setBounds(550, 600, 100, 40);
			InfoButton.setFont(new Font("Arial", Font.BOLD, 12));
			backgroundPanel.add(InfoButton);
	
			
			editButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// קבלת השורה הנבחרת מהטבלה
					int selectedRow = table.getSelectedRow();
					if (selectedRow != -1) {
						// קבלת השאלה הנוכחית מהטבלה
						String questionText = (String) tableModel.getValueAt(selectedRow, 0);
						Question selectedQuestion = sysData.getQuestions().get(selectedRow);

						// בקשה לעדכון השאלה
						String updatedQuestion = JOptionPane.showInputDialog(frame, "Edit Question:", questionText);
						if (updatedQuestion != null && !updatedQuestion.trim().isEmpty()) {
							tableModel.setValueAt(updatedQuestion, selectedRow, 0);
							selectedQuestion.setQuestion(updatedQuestion); // עדכון השאלה ב-JSON
						}

						// בקשה לעדכון התשובות
						List<String> updatedAnswers = new ArrayList<>();
						for (int i = 0; i < 4; i++) {
							String answer = JOptionPane.showInputDialog(frame, "Enter Answer " + (i + 1) + ":",
									selectedQuestion.getAnswers().get(i));
							if (answer != null && !answer.trim().isEmpty()) {
								updatedAnswers.add(answer);
							} else {
								JOptionPane.showMessageDialog(frame, "Answer " + (i + 1) + " cannot be empty!");
								return;
							}
						}

						// בדיקת כפילויות בתשובות
						Set<String> uniqueAnswers = new HashSet<>(updatedAnswers);
						if (uniqueAnswers.size() != updatedAnswers.size()) {
							JOptionPane.showMessageDialog(frame, "Answers must be unique!");
							return;
						}

						// עדכון התשובות ב-JSON
						selectedQuestion.setAnswers(updatedAnswers);

						// בקשה לעדכון רמת הקושי
						String difficultyStr = JOptionPane.showInputDialog(frame, "Enter Difficulty Level (1-3):",
								selectedQuestion.getDifficulty());
						int difficulty;
						try {
							difficulty = Integer.parseInt(difficultyStr);
							if (difficulty < 1 || difficulty > 3) {
								JOptionPane.showMessageDialog(frame, "Difficulty level must be between 1 and 3.");
								return;
							}
						} catch (NumberFormatException ex) {
							JOptionPane.showMessageDialog(frame, "Invalid input for difficulty level!");
							return;
						}

						// עדכון רמת הקושי ב-JSON
						selectedQuestion.setDifficulty(difficulty);

						// בקשה לעדכון התשובה הנכונה
						String correctAnswerStr = JOptionPane.showInputDialog(frame, "Enter Correct Answer (1-4):",
								selectedQuestion.getCorrectAns());
						int correctAnswer;
						try {
							correctAnswer = Integer.parseInt(correctAnswerStr);
							if (correctAnswer < 1 || correctAnswer > 4) {
								JOptionPane.showMessageDialog(frame, "Correct answer must be between 1 and 4.");
								return;
							}
						} catch (NumberFormatException ex) {
							JOptionPane.showMessageDialog(frame, "Invalid input for correct answer!");
							return;
						}

						// עדכון התשובה הנכונה ב-JSON
						selectedQuestion.setCorrectAns(correctAnswer);

						// שמירה לקובץ JSON
						sysData.saveToFile(); // שמירת השינויים
						JOptionPane.showMessageDialog(frame, "Question updated successfully!");
					} else {
						JOptionPane.showMessageDialog(frame, "Please select a row to edit.", "No Selection",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			});
			addButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// בקשה לשאלה
					String newQuestion = JOptionPane.showInputDialog(frame, "Enter New Question:");
					if (newQuestion == null || newQuestion.trim().isEmpty()) {
						JOptionPane.showMessageDialog(frame, "Question cannot be empty!");
						return;
					}

					// בקשה לתשובות
					List<String> answers = new ArrayList<>();
					for (int i = 0; i < 4; i++) {
						String answer = JOptionPane.showInputDialog(frame, "Enter Answer " + (i + 1) + ":");
						if (answer == null || answer.trim().isEmpty()) {
							JOptionPane.showMessageDialog(frame, "Answer " + (i + 1) + " cannot be empty!");
							return;
						}
						answers.add(answer);
					}

					// בדיקת כפילויות בתשובות
					Set<String> uniqueAnswers = new HashSet<>(answers);
					if (uniqueAnswers.size() != answers.size()) {
						JOptionPane.showMessageDialog(frame, "Answers must be unique!");
						return;
					}

					// בקשה לרמת קושי
					String difficultyStr = JOptionPane.showInputDialog(frame, "Enter Difficulty Level (1-3):");
					int difficulty;
					try {
						difficulty = Integer.parseInt(difficultyStr);
						if (difficulty < 1 || difficulty > 3) {
							JOptionPane.showMessageDialog(frame, "Difficulty level must be between 1 and 3.");
							return;
						}
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(frame, "Invalid input for difficulty level!");
						return;
					}

					// בקשה לתשובה נכונה
					String correctAnswerStr = JOptionPane.showInputDialog(frame, "Enter Correct Answer (1-4):");
					int correctAnswer;
					try {
						correctAnswer = Integer.parseInt(correctAnswerStr);
						if (correctAnswer < 1 || correctAnswer > 4) {
							JOptionPane.showMessageDialog(frame, "Correct answer must be between 1 and 4.");
							return;
						}
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(frame, "Invalid input for correct answer!");
						return;
					}

					// יצירת אובייקט Question
					Question question = new Question();
					question.setQuestion(newQuestion);
					question.setAnswers(answers);
					question.setDifficulty(difficulty);
					question.setCorrectAns(correctAnswer); // הגדרת התשובה הנכונה

				
					sysData.addQuestion(question);
					tableModel.addRow(new Object[] {sysData.getQuestions().size(), question.getQuestion(),answers.get(0),answers.get(1),answers.get(2),answers.get(3),correctAnswerStr, question.getDifficulty() });
				}
			});
			
			InfoButton.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent e) {
			        // הצגת מידע על המסך
			        StringBuilder infoMessage = new StringBuilder();
			        infoMessage.append("Questions Table Screen:\n");
			        infoMessage.append("- View and manage questions.\n");
			        infoMessage.append("- Use 'ADD' to add a new question.\n");
			        infoMessage.append("- Use 'EDIT' to edit the selected question.\n");
			        infoMessage.append("- Use 'DELETE' to remove a question.\n");
			        infoMessage.append("- Difficulty Levels: 1 = Easy, 2 = Medium, 3 = Hard.\n");

			        JOptionPane.showMessageDialog(frame, infoMessage.toString(), "Information", JOptionPane.INFORMATION_MESSAGE);
			    }
			});

			
			DeleteButton.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent e) {
			        // קבלת השורה הנבחרת מהטבלה
			        int selectedRow = table.getSelectedRow();
			        if (selectedRow != -1) {
			            // בקשת אישור מהמשתמש
			            int confirm = JOptionPane.showConfirmDialog(frame, 
			                "Are you sure you want to delete this question?", 
			                "Confirm Delete", 
			                JOptionPane.YES_NO_OPTION);
			            if (confirm == JOptionPane.YES_OPTION) {
			                try {
			                    // מחיקת השאלה מ-SysData
			                    sysData.deleteQuestion(selectedRow);
			                    // מחיקת השורה מהטבלה
			                    tableModel.removeRow(selectedRow);
			                    JOptionPane.showMessageDialog(frame, "Question deleted successfully!");
			                } catch (Exception ex) {
			                    JOptionPane.showMessageDialog(frame, 
			                        "An error occurred while deleting the question: " + ex.getMessage(), 
			                        "Error", 
			                        JOptionPane.ERROR_MESSAGE);
			                }
			            }
			        } else {
			            JOptionPane.showMessageDialog(frame, 
			                "Please select a row to delete.", 
			                "No Selection", 
			                JOptionPane.WARNING_MESSAGE);
			        }
			    }
			});


			BackButton.addActionListener(e -> {
				frame.setVisible(false); // Hide the current frame
				HistoryAndQuestionsMenu.main(null); // Open the Questions and History menu
				frame.dispose(); // Dispose of the current frame

			});

			// Set frame visibility
			frame.setVisible(true);
		});
	}

	public static JsonArray loadQuestionsFromJson(String filePath) {
		// Open the JSON file
		FileReader jsonFile = null;
		try {
			jsonFile = new FileReader(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Parse the JSON file using Gson
		Gson gson = new Gson();
		JsonObject rootObject = gson.fromJson(jsonFile, JsonObject.class);

		// Get the "questions" array from the root object
		return rootObject.getAsJsonArray("questions");
	}

	public static DefaultTableModel createTableModelFromJson(JsonArray questionsArray) {
		// Define column names for the table (include separate columns for each answer)
		String[] columnNames = { "ID", "Question", "Answer1", "Answer2", "Answer3", "Answer4", "Correct Answer",
				"Difficulty" };

		// Prepare the data for the table model (ArrayList of Object arrays)
		ArrayList<Object[]> data = new ArrayList<>(); // List to hold Object[] rows

		// Variable to hold the running ID
		int runningId = 1;

		// Iterate over the questions array to populate the table data
		for (JsonElement questionElement : questionsArray) {
			JsonObject questionObject = questionElement.getAsJsonObject();

			// Extract data for each question (ensure everything is a string)
			String questionText = questionObject.get("question").getAsString();
			JsonArray answers = questionObject.getAsJsonArray("answers");
			String correctAnswer = questionObject.get("correct_ans").getAsString();
			String difficulty = questionObject.get("difficulty").getAsString();

			// Prepare answers for separate columns
			String answer1 = answers.size() > 0 ? answers.get(0).getAsString() : "";
			String answer2 = answers.size() > 1 ? answers.get(1).getAsString() : "";
			String answer3 = answers.size() > 2 ? answers.get(2).getAsString() : "";
			String answer4 = answers.size() > 3 ? answers.get(3).getAsString() : "";

			// Add the running ID and extracted data as an Object array (for table row)
			data.add(new Object[] { Integer.toString(runningId), // Use running ID as a string
					questionText, // Question text
					answer1, // Answer1
					answer2, // Answer2
					answer3, // Answer3
					answer4, // Answer4
					correctAnswer, // Correct answer
					difficulty // Difficulty
			});

			// Increment the running ID for the next question
			runningId++;
		}

		// Convert List to 2D array for DefaultTableModel
		Object[][] dataArray = new Object[data.size()][columnNames.length];
		data.toArray(dataArray);

		// Create and return the DefaultTableModel with String data
		return new DefaultTableModel(dataArray, columnNames);
	}

//	private static void saveUpdatedJson() {
//		// Read the current table data
//		JsonArray updatedQuestionsArray = new JsonArray();
//
//		for (int i = 0; i < tableModel.getRowCount(); i++) {
//			JsonObject questionObject = new JsonObject();
//			questionObject.addProperty("question", (String) tableModel.getValueAt(i, 1));
//
//			// Collect answers and other fields
//			JsonArray answers = new JsonArray();
//			answers.add((String) tableModel.getValueAt(i, 2));
//			answers.add((String) tableModel.getValueAt(i, 3));
//			answers.add((String) tableModel.getValueAt(i, 4));
//			answers.add((String) tableModel.getValueAt(i, 5));
//
//			questionObject.add("answers", answers);
//			questionObject.addProperty("correct_ans", (String) tableModel.getValueAt(i, 6));
//			questionObject.addProperty("difficulty", (String) tableModel.getValueAt(i, 7));
//			System.out.println((String) tableModel.getValueAt(i, 6));
//			System.out.println(tableModel.getValueAt(i, 7));
//			// Add to the updated questions array
//			updatedQuestionsArray.add(questionObject);
//		}
//
//		// Create a root object and add the updated questions array
//		JsonObject rootObject = new JsonObject();
//		rootObject.add("questions", updatedQuestionsArray);
//
//		try {
//			// Save the updated JSON data to the file
//			FileWriter writer = new FileWriter("questions_scheme.json");
//			writer.write(rootObject.toString());
//			writer.close();
//
//			JOptionPane.showMessageDialog(null, "Question updated successfully!");
//		} catch (IOException e) {
//			e.printStackTrace();
//			JOptionPane.showMessageDialog(null, "Failed to save the updated JSON.", "Error", JOptionPane.ERROR_MESSAGE);
//		}
//	}

//	private static void addNewQuestionToJson(JsonObject newQuestionObject) {
//		try {
//			// Read the existing JSON file
//			FileReader jsonFile = new FileReader("questions_scheme.json");
//			JsonObject rootObject = JsonParser.parseReader(jsonFile).getAsJsonObject();
//			JsonArray questionsArray = rootObject.getAsJsonArray("questions");
//
//			// Add the new question to the questions array
//			questionsArray.add(newQuestionObject);
//
//			// Save the updated JSON back to the file
//			FileWriter writer = new FileWriter("questions_scheme.json");
//			writer.write(rootObject.toString());
//			writer.close();
//
//			JOptionPane.showMessageDialog(null, "New question added to the JSON.");
//		} catch (IOException e) {
//			e.printStackTrace();
//			JOptionPane.showMessageDialog(null, "Failed to add the new question to the JSON.", "Error",
//					JOptionPane.ERROR_MESSAGE);
//		}
//	}
}