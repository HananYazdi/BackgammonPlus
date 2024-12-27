package view;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class QuestionsTableScreen {
	private static  DefaultTableModel tableModel ;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Questions Table");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            
            // Panel with a custom background
            ImageIcon bgIcon = new ImageIcon("src/images/questions.png");
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
            String[] columns = {"Question", "Answer 1", "Answer 2", "Answer 3", "Answer 4", "Correct Answer", "Difficulty"};
            Object[][] data = {
                    {"What is the main purpose of software design?", "Create hardware", "Design structure", "Build user interface", "Write documentation", "Design structure", "Easy"},
                    {"Which software development model is iterative?", "Waterfall", "Spiral", "Agile", "V-model", "Spiral", "Medium"},
                    {"What is a 'bug' in software development?", "A feature", "A defect", "A new release", "A type of code", "A defect", "Easy"},
                    {"Which principle is part of Object-Oriented Programming (OOP)?", "Inheritance", "Compilation", "Execution", "Networking", "Inheritance", "Medium"},
                    {"What is the main goal of unit testing?", "Test the whole system", "Test individual components", "Test user experience", "Test final product", "Test individual components", "Hard"}
            };
            JsonArray questionsArray = loadQuestionsFromJson("questions_scheme.json");
            //DefaultTableModel tableModel = new DefaultTableModel(data, columns);
            tableModel =  createTableModelFromJson(questionsArray);
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

            // Add ActionListeners
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        String question = (String) tableModel.getValueAt(selectedRow, 0);
                        String updatedQuestion = JOptionPane.showInputDialog(frame, "Edit Question:", question);
                        if (updatedQuestion != null) {
                            tableModel.setValueAt(updatedQuestion, selectedRow, 0);
                            JsonObject questionObject = questionsArray.get(selectedRow-1).getAsJsonObject();
                            questionObject.addProperty("question", updatedQuestion); // Update the 'question' field in JSON

                            // Save the updated JSON to the file
                            saveUpdatedJson();
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Please select a row to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });

            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newQuestion = JOptionPane.showInputDialog(frame, "Enter New Question:");
                    if (newQuestion != null && !newQuestion.trim().isEmpty()) {
                        tableModel.addRow(new Object[]{newQuestion, "", "", "", "", "", ""});
                        
                        JsonObject newQuestionObject = new JsonObject();
                        newQuestionObject.addProperty("question", newQuestion);

                        // Create a new JsonArray for answers (currently empty)
                        JsonArray newAnswers = new JsonArray();
                        newAnswers.add("");  // Add empty answers initially
                        newAnswers.add("");
                        newAnswers.add("");
                        newAnswers.add("");
                        newQuestionObject.add("answers", newAnswers);

                        // Set default values for correct answer and difficulty
                        newQuestionObject.addProperty("correct_ans", " ");
                        newQuestionObject.addProperty("difficulty", " ");

                        // Add the new question to the JSON file
                        addNewQuestionToJson(newQuestionObject);

                        // Optionally, save the updated JSON after adding the question
                        saveUpdatedJson();
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
        String[] columnNames = {"ID", "Question", "Answer1", "Answer2", "Answer3", "Answer4", "Correct Answer", "Difficulty"};

        // Prepare the data for the table model (ArrayList of Object arrays)
        ArrayList<Object[]> data = new ArrayList<>();  // List to hold Object[] rows

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
            data.add(new Object[]{
                Integer.toString(runningId),     // Use running ID as a string
                questionText,                    // Question text
                answer1,                          // Answer1
                answer2,                          // Answer2
                answer3,                          // Answer3
                answer4,                          // Answer4
                correctAnswer,                   // Correct answer
                difficulty                       // Difficulty
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
    
    private static void saveUpdatedJson() {
        // Read the current table data
        JsonArray updatedQuestionsArray = new JsonArray();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            JsonObject questionObject = new JsonObject();
            questionObject.addProperty("question", (String) tableModel.getValueAt(i, 1));

            // Collect answers and other fields
            JsonArray answers = new JsonArray();
            answers.add((String) tableModel.getValueAt(i, 2));
            answers.add((String) tableModel.getValueAt(i, 3));
            answers.add((String) tableModel.getValueAt(i, 4));
            answers.add((String) tableModel.getValueAt(i, 5));

            questionObject.add("answers", answers);
            questionObject.addProperty("correct_ans", (String) tableModel.getValueAt(i, 6));
            questionObject.addProperty("difficulty", (String) tableModel.getValueAt(i, 7));
            System.out.println((String) tableModel.getValueAt(i, 6));
            System.out.println( tableModel.getValueAt(i, 7));
            // Add to the updated questions array
            updatedQuestionsArray.add(questionObject);
        }

        // Create a root object and add the updated questions array
        JsonObject rootObject = new JsonObject();
        rootObject.add("questions", updatedQuestionsArray);

        try {
            // Save the updated JSON data to the file
            FileWriter writer = new FileWriter("questions_scheme.json");
            writer.write(rootObject.toString());
            writer.close();

            JOptionPane.showMessageDialog(null, "Question updated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save the updated JSON.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void addNewQuestionToJson(JsonObject newQuestionObject) {
        try {
            // Read the existing JSON file
            FileReader jsonFile = new FileReader("questions_scheme.json");
            JsonObject rootObject = JsonParser.parseReader(jsonFile).getAsJsonObject();
            JsonArray questionsArray = rootObject.getAsJsonArray("questions");

            // Add the new question to the questions array
            questionsArray.add(newQuestionObject);

            // Save the updated JSON back to the file
            FileWriter writer = new FileWriter("questions_scheme.json");
            writer.write(rootObject.toString());
            writer.close();
            
            JOptionPane.showMessageDialog(null, "New question added to the JSON.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add the new question to the JSON.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}