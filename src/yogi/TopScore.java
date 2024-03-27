package yogi;


public class TopScore {
    private final String playerName;
    private final int playerScore;

    
    public TopScore(String playerName, int playerScore) {
        if (playerName == null || playerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name must not be null or empty.");
        }
        if (playerScore < 0) {
            throw new IllegalArgumentException("Player score cannot be negative.");
        }
        this.playerName = playerName;
        this.playerScore = playerScore;
    }

    
    public String getPlayerName() {
        return playerName;
    }

    
    public int getPlayerScore() {
        return playerScore;
    }


    @Override
    public String toString() {
        return String.format("TopScore{playerName='%s', playerScore=%d}", playerName, playerScore);
    }
}
