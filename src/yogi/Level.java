package yogi;

import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.util.Arrays;

public class Level {

// Sprite dimensions
private final int SPRITE_WIDTH = 40;
private final int SPRITE_HEIGHT = 40;

// Game level elements
private ArrayList<Tree> trees;        // List of tree objects in the level
private ArrayList<Mountain> mountains; // List of mountain objects in the level
private ArrayList<Ranger> rangers;    // List of ranger objects in the level
private ArrayList<Basket> baskets;    // List of basket objects in the level

/**
 * Constructs a new Level object based on the provided level path.
 *
 * @param levelPath The path to the level file.
 * @throws IOException if an I/O error occurs while loading the level.
 */

public Level(String levelPath) throws IOException {
    initLevelElements();
    loadLevel(levelPath);
}


    private void initLevelElements() {
        trees = new ArrayList<>();
        mountains = new ArrayList<>();
        rangers = new ArrayList<>();
        baskets = new ArrayList<>();
    }

    private void loadLevel(String levelPath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(levelPath))) {
            String line;
            int y = 0;
            while ((line = br.readLine()) != null) {
                processLine(line, y++);
            }
        }
    }

    private void processLine(String line, int y) {
        int x = 0;
        for (char blockType : line.toCharArray()) {
            createLevelElement(blockType, x, y);
            x++;
        }
    }

    private void createLevelElement(char type, int x, int y) {
        int realX = x * SPRITE_WIDTH;
        int realY = y * SPRITE_HEIGHT;
        Image image = getElementImage(type);
        if (image != null) {
            addElementToLevel(type, realX, realY, image);
        }
    }

    private Image getElementImage(char type) {
        switch (type) {
            case 'T':
                return new ImageIcon("Content/Images/TreeImage.png").getImage();
            case 'M':
                return new ImageIcon("Content/Images/MountainImage.png").getImage();
            case 'R':
                return new ImageIcon("Content/Images/RangerImage.png").getImage();
            case 'B':
                return new ImageIcon("Content/Images/BasketImage.png").getImage();
            default:
                return null;
        }
    }

    private void addElementToLevel(char type, int x, int y, Image image) {
        switch (type) {
            case 'T':
                trees.add(new Tree(x, y, SPRITE_WIDTH, SPRITE_HEIGHT, image));
                break;
            case 'M':
                mountains.add(new Mountain(x, y, SPRITE_WIDTH, SPRITE_HEIGHT, image));
                break;
            case 'R':
                rangers.add(new Ranger(x, y, SPRITE_WIDTH, SPRITE_HEIGHT, image));
                break;
            case 'B':
                baskets.add(new Basket(x, y, SPRITE_WIDTH, SPRITE_HEIGHT, image));
                break;
        }
    }
    
    public boolean collides(YogiBear bear) {
        return mountains.stream().anyMatch(mountain -> bear.intersects(mountain)) ||
                trees.stream().anyMatch(tree -> bear.intersects(tree));
    }

    public void pickBasket(YogiBear bear) {
        baskets.removeIf(basket -> bear.intersects(basket));
    }

    public boolean gotBusted(YogiBear bear) {
        return rangers.stream().anyMatch(ranger -> isBusted(bear, ranger));
    }

    private boolean isBusted(YogiBear bear, Ranger ranger) {
        YogiBear[] surrounds = getSurroundingPositions(bear);
        return Arrays.stream(surrounds).anyMatch(position -> position.intersects(ranger) &&
                !isObstacleBetween(bear, ranger));
    }

    private YogiBear[] getSurroundingPositions(YogiBear bear) {
        return new YogiBear[]{
                new YogiBear(bear.getX() + SPRITE_WIDTH, bear.getY(), bear.getWidth(), bear.getHeight(), bear.getImage()),
                // ... other positions ...
        };
    }

    private boolean isObstacleBetween(YogiBear yogi, Ranger rang) {
        return baskets.stream().anyMatch(basket -> basket.liesBetween(yogi, rang)) ||
                trees.stream().anyMatch(tree -> tree.liesBetween(yogi, rang)) ||
                mountains.stream().anyMatch(mountain -> mountain.liesBetween(yogi, rang));
    }
    
    
    public boolean collides(Ranger ranger) {
        return baskets.stream().anyMatch(basket -> ranger.intersects(basket)) ||
               mountains.stream().anyMatch(mountain -> ranger.intersects(mountain)) ||
               trees.stream().anyMatch(tree -> ranger.intersects(tree)) ||
               rangers.stream().anyMatch(otherRanger -> !ranger.equals(otherRanger) && ranger.intersects(otherRanger));
    }

    public boolean isOver() {
        return baskets.isEmpty();
    }

    public void draw(Graphics g) {
        baskets.forEach(basket -> basket.render(g));
        mountains.forEach(mountain -> mountain.render(g));
        trees.forEach(tree -> tree.draw(g));
        rangers.forEach(ranger -> ranger.render(g));
    }

    private <T> ArrayList<T> getElements(ArrayList<T> elements) {
        return new ArrayList<>(elements);
    }

    public ArrayList<Tree> getTrees() {
        return getElements(trees);
    }

    public ArrayList<Mountain> getMountains() {
        return getElements(mountains);
    }

    public ArrayList<Ranger> getRangers() {
        return getElements(rangers);
    }

    public ArrayList<Basket> getBaskets() {
        return getElements(baskets);
    }
}
