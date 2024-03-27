package yogi;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;


public class TopScores {

// Logger for debugging and logging information
private static final Logger LOG = Logger.getLogger(TopScores.class.getName());

// Database connection and SQL query constants
private static final String DATABASE_URL = "jdbc:derby://localhost:1527/YogiHighscores";
private static final String SQL_INSERT = "INSERT INTO HIGHSCORES (TIMESTAMP, NAME, SCORE) VALUES (?, ?, ?)";
private static final String SQL_DELETE = "DELETE FROM HIGHSCORES WHERE SCORE=?";
private static final String SQL_SELECT = "SELECT * FROM HIGHSCORES ORDER BY SCORE DESC";

// Configuration and state of the high scores management
private final int maximumScores;             // Maximum number of scores to retain
private final Connection dbConnection;       // Database connection instance


    public TopScores(int maximumScores) throws SQLException {
        this.maximumScores = maximumScores;
        this.dbConnection = connectToDatabase();
    }

    private Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    public ArrayList<TopScore> fetchHighScores() throws SQLException {
        ArrayList<TopScore> scores = new ArrayList<>();
        try (Statement statement = dbConnection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT)) {
            while (resultSet.next()) {
                scores.add(new TopScore(resultSet.getString("NAME"), resultSet.getInt("SCORE")));
            }
        }
        return scores;
    }

    public void addHighScore(String playerName, int playerScore) throws SQLException {
        ArrayList<TopScore> scores = fetchHighScores();

        if (qualifiesForHighScore(scores, playerScore)) {
            updateHighScores(scores, playerName, playerScore);
        }
    }

    private boolean qualifiesForHighScore(ArrayList<TopScore> scores, int playerScore) {
        return scores.size() < maximumScores || playerScore > scores.get(scores.size() - 1).getPlayerScore();
    }

    private void updateHighScores(ArrayList<TopScore> scores, String playerName, int playerScore) throws SQLException {
        if (scores.size() >= maximumScores) {
            removeLowestScore(scores.get(scores.size() - 1).getPlayerScore());
        }
        recordScore(playerName, playerScore);
    }

    private void recordScore(String playerName, int playerScore) throws SQLException {
        try (PreparedStatement psInsert = dbConnection.prepareStatement(SQL_INSERT)) {
            psInsert.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            psInsert.setString(2, playerName);
            psInsert.setInt(3, playerScore);
            psInsert.executeUpdate();
        }
    }

    private void removeLowestScore(int playerScore) throws SQLException {
        try (PreparedStatement psDelete = dbConnection.prepareStatement(SQL_DELETE)) {
            psDelete.setInt(1, playerScore);
            psDelete.executeUpdate();
        }
    }
}
