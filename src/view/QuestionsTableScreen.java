package view;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuestionsTableScreen {

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

            DefaultTableModel tableModel = new DefaultTableModel(data, columns);
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
}