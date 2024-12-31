import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SysData {

    // Class to represent a question
    public static class Question {
        private String question;
        private List<String> answers;
        private int correctAns; // Use integer for the index of the correct answer
        private int difficulty;
        private int score;

        // Getters and Setters
        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public List<String> getAnswers() {
            return answers;
        }

        public void setAnswers(List<String> answers) {
            this.answers = answers;
        }

        public int getCorrectAns() {
            return correctAns;
        }

        public void setCorrectAns(int correctAns) {
            this.correctAns = correctAns;
        }

        public int getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(int difficulty) {
            this.difficulty = difficulty;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return "Question{" +
                    "question='" + question + '\'' +
                    ", answers=" + answers +
                    ", correctAns=" + correctAns +
                    ", difficulty=" + difficulty +
                    ", score=" + score +
                    '}';
        }
    }

    // Class to represent game history
    public static class GameHistory {
        private String player1Name;
        private String player2Name;
        private String winner;
        private int difficultyLevel;
        private String gameTime;
        private int player1Score;
        private int player2Score;

        // Constructor
        public GameHistory(String player1Name, String player2Name, String winner, int difficultyLevel, String gameTime, int player1Score, int player2Score) {
            this.player1Name = player1Name;
            this.player2Name = player2Name;
            this.winner = winner;
            this.difficultyLevel = difficultyLevel;
            this.gameTime = gameTime;
            this.player1Score = player1Score;
            this.player2Score = player2Score;
        }

        // Getters and Setters
        public String getPlayer1Name() {
            return player1Name;
        }

        public void setPlayer1Name(String player1Name) {
            this.player1Name = player1Name;
        }

        public String getPlayer2Name() {
            return player2Name;
        }

        public void setPlayer2Name(String player2Name) {
            this.player2Name = player2Name;
        }

        public String getWinner() {
            return winner;
        }

        public void setWinner(String winner) {
            this.winner = winner;
        }

        public int getDifficultyLevel() {
            return difficultyLevel;
        }

        public void setDifficultyLevel(int difficultyLevel) {
            this.difficultyLevel = difficultyLevel;
        }

        public String getGameTime() {
            return gameTime;
        }

        public void setGameTime(String gameTime) {
            this.gameTime = gameTime;
        }

        public int getPlayer1Score() {
            return player1Score;
        }

        public void setPlayer1Score(int player1Score) {
            this.player1Score = player1Score;
        }

        public int getPlayer2Score() {
            return player2Score;
        }

        public void setPlayer2Score(int player2Score) {
            this.player2Score = player2Score;
        }

        @Override
        public String toString() {
            return "GameHistory{" +
                    "player1Name='" + player1Name + '\'' +
                    ", player2Name='" + player2Name + '\'' +
                    ", winner='" + winner + '\'' +
                    ", difficultyLevel=" + difficultyLevel +
                    ", gameTime='" + gameTime + '\'' +
                    ", player1Score=" + player1Score +
                    ", player2Score=" + player2Score +
                    '}';
        }
    }

    private List<Question> questions;
    private List<GameHistory> gameHistories;
    private String filePath;

    // Constructor
    public SysData() {
        this.filePath = '../../questions_scheme.json';
        questions = new ArrayList<>();
        gameHistories = new ArrayList<>();
        loadFromFile();
    }

    // Load questions and game history from JSON file
    private void loadFromFile() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> data = objectMapper.readValue(new File(filePath), new TypeReference<Map<String, Object>>() {});

            // Load questions
            List<Question> loadedQuestions = objectMapper.convertValue(data.get("questions"), new TypeReference<List<Question>>() {});
            if (loadedQuestions != null) {
                questions = loadedQuestions;
            }

            // Load game histories
            List<GameHistory> loadedHistories = objectMapper.convertValue(data.get("gameHistories"), new TypeReference<List<GameHistory>>() {});
            if (loadedHistories != null) {
                gameHistories = loadedHistories;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save current state to JSON file
    private void saveToFile() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> data = new HashMap<>();
            data.put("questions", questions);
            data.put("gameHistories", gameHistories);

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add a new question
    public void addQuestion(Question question) {
        questions.add(question);
        saveToFile();
    }

    // Add a new game history
    public void addGameHistory(GameHistory gameHistory) {
        gameHistories.add(gameHistory);
        saveToFile();
    }

    // Get questions based on difficulty
    public List<Question> getQuestionsByDifficulty(int difficulty) {
        List<Question> filteredQuestions = new ArrayList<>();
        for (Question question : questions) {
            if (question.getDifficulty() == difficulty) {
                filteredQuestions.add(question);
            }
        }
        return filteredQuestions;
    }

    // Get a random question based on difficulty
    public Question getRandomQuestion(int difficulty) {
        List<Question> filteredQuestions = getQuestionsByDifficulty(difficulty);
        if (filteredQuestions.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return filteredQuestions.get(random.nextInt(filteredQuestions.size()));
    }

    // Get all game histories
    public List<GameHistory> getGameHistories() {
        return gameHistories;
    }

    // Get all questions
    public List<Question> getQuestions() {
        return questions;
    }
}
