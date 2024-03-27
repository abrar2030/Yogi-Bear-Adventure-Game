package yogi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.awt.event.ActionListener;

public class AdventureGame extends JPanel {
    
// Game configuration constants
private final int FRAME_RATE;                // Frame rate of the game
private final int MOVEMENT_SPEED_BEAR;       // Movement speed of the bear character

// Game state variables
private boolean isGamePaused;                // Flag to indicate if the game is paused
private int currentLevelNumber;              // Current level number in the game
private int playerLives;                     // Number of lives remaining for the player

// Game entities and components
private Image sceneBackground;               // Background image of the current scene
private Level gameLevel;                     // The current game level
private YogiBear yogiBear;                   // Main character of the game, Yogi Bear
private Timer frameRefreshTimer;             // Timer for refreshing the game frame
TopScores highscores;                        // Object to track high scores in the game


    public AdventureGame() {
        super();
        FRAME_RATE = 240;
        MOVEMENT_SPEED_BEAR = 40;
        isGamePaused = false;
        currentLevelNumber = 0;
        playerLives = 3;
        initGame();
    }

    private void initGame() {
        setupInitialGameConditions();
        configureKeyBindings();
        startGameTimer();
    }

    private void setupInitialGameConditions() {
        initializeHighScores();
        sceneBackground = new ImageIcon("Content/Images/BackgroundImage.png").getImage();
        restart();
    }

    private void initializeHighScores() {
        try {
            highscores = new TopScores(10);
            System.out.println("highscores are :" + highscores.fetchHighScores());
        } catch (SQLException ex) {
            // Handle exception
        }
    }

    private void configureKeyBindings() {
        configureKeyBinding("LEFT", "pressed left", new MoveAction(-MOVEMENT_SPEED_BEAR, 0));
        configureKeyBinding("RIGHT", "pressed right", new MoveAction(MOVEMENT_SPEED_BEAR, 0));
        configureKeyBinding("DOWN", "pressed down", new MoveAction(0, MOVEMENT_SPEED_BEAR));
        configureKeyBinding("UP", "pressed up", new MoveAction(0, -MOVEMENT_SPEED_BEAR));
        configureKeyBinding("ESCAPE", "escape", new TogglePauseAction());
    }

    private void configureKeyBinding(String key, String actionMapKey, Action action) {
        this.getInputMap().put(KeyStroke.getKeyStroke(key), actionMapKey);
        this.getActionMap().put(actionMapKey, action);
    }

    private void startGameTimer() {
        frameRefreshTimer = new Timer(1000 / FRAME_RATE, new NewFrameListener());
        frameRefreshTimer.start();
    }

    public void restart() {
        try {
            gameLevel = new Level("Content/levels/level0" + currentLevelNumber + ".txt");
        } catch (IOException ex) {
            Logger.getLogger(AdventureGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        Image bearImage = new ImageIcon("Content/Images/BearImage.png").getImage();
        yogiBear = new YogiBear(0, 0, 40, 40, bearImage);
    }

    public void setLevel(int level) {
        currentLevelNumber = level;
    }

    public ArrayList<TopScore> getScores() {
        ArrayList<TopScore> scores = null;
        try {
            scores = highscores.fetchHighScores();
        } catch (SQLException ex) {
            // Handle exception
        }
        return scores;
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        grphcs.drawImage(sceneBackground, 0, 0, 400, 400, null);
        gameLevel.draw(grphcs);
        yogiBear.render(grphcs);
    }

    class MoveAction extends AbstractAction {
        int dx, dy;

        MoveAction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            YogiBear newBear = new YogiBear(yogiBear.getX() + dx, yogiBear.getY() + dy, yogiBear.getWidth(), yogiBear.getWidth(), yogiBear.getImage());
            if (!gameLevel.collides(newBear)) {
                yogiBear.setVelocityX(dx);
                yogiBear.setVelocityY(dy);
                yogiBear.horizontalMove();
                yogiBear.verticalMove();
                gameLevel.pickBasket(yogiBear);
                System.out.println("bear.x : " + yogiBear.getX() + " bear.y: " + yogiBear.getY());
            }
        }
    }

    class TogglePauseAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent ae) {
            isGamePaused = !isGamePaused;
        }
    }

    class NewFrameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            gameLogic();
        }

        private void gameLogic() {
            if (!isGamePaused) {
                moveRangers();
            }

            checkGameStatus();
            repaint();
        }

        private void moveRangers() {
            for (Ranger ranger : gameLevel.getRangers()) {
                ranger.move();
                if (gameLevel.collides(ranger)) 
                    ranger.invertVel();
            }
        }

        private void checkGameStatus() {
            checkCollisionWithRanger();
            checkLevelCompletion();
        }

        private void checkCollisionWithRanger() {
            if (gameLevel.gotBusted(yogiBear)) {
                playerLives--;
                if (playerLives == 0) {
                    String msg = "Oops, You have lost the Game!";
                    JOptionPane.showMessageDialog(AdventureGame.this, msg, "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
                    try {
                        System.out.println(highscores.fetchHighScores());
                        String name = JOptionPane.showInputDialog("Enter your name");
                        highscores.addHighScore(name, currentLevelNumber);
                        System.out.println(highscores.fetchHighScores());
                    } catch (SQLException ex) {
                        // Handle exception
                    }

                    currentLevelNumber = 0;
                    restart();
                    playerLives = 3;
                } else {
                    String msg = "You have lost a life. You have only " + playerLives + " lives left!";
                    JOptionPane.showMessageDialog(AdventureGame.this, msg, "LIFE LOST MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                    Image bearImage = new ImageIcon("Content/Images/BearImage.png").getImage();
                    yogiBear = new YogiBear(0, 0, 40, 40, bearImage);
                }
            }
        }

        private void checkLevelCompletion() {
            if (gameLevel.isOver()) {
                if (currentLevelNumber == 9) {
                    String msg = "Wow. You are a Pro!";
                    JOptionPane.showMessageDialog(AdventureGame.this, msg, "PRO Spotted", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                } else {
                    currentLevelNumber = (currentLevelNumber + 1) % 9;
                    restart();
                }
            }
        }
    }
}
