package yogi;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;


public class Sprite {
// Position and dimensions of the object
protected int positionX;      // X-coordinate of the object's position
protected int positionY;      // Y-coordinate of the object's position
protected int objectWidth;    // Width of the object
protected int objectHeight;   // Height of the object

// Visual representation of the object
protected Image objectImage;  // Image representing the object visually


    
    public Sprite(int positionX, int positionY, int objectWidth, int objectHeight, Image objectImage) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.objectWidth = objectWidth;
        this.objectHeight = objectHeight;
        this.objectImage = objectImage;
    }

    public void render(Graphics graphics) {
        graphics.drawImage(objectImage, positionX, positionY, objectWidth, objectHeight, null);
    }

    public boolean intersects(Sprite anotherSprite) {
        Rectangle thisSprite = new Rectangle(positionX, positionY, objectWidth, objectHeight);
        Rectangle otherSprite = new Rectangle(anotherSprite.positionX, anotherSprite.positionY, anotherSprite.objectWidth, anotherSprite.objectHeight);
        return thisSprite.intersects(otherSprite);
    }

    public boolean liesBetween(Sprite spriteOne, Sprite spriteTwo) {
        return adjacentTo(spriteOne) && adjacentTo(spriteTwo);
    }

    public boolean adjacentTo(Sprite anotherSprite) {
        for (Sprite adjacent : generateAdjacentSprites(anotherSprite)) {
            if (this.intersects(adjacent)) {
                return true;
            }
        }
        return false;
    }

    private Sprite[] generateAdjacentSprites(Sprite referenceSprite) {
        return new Sprite[] {
            createAdjacent(referenceSprite, 40, 0),
            createAdjacent(referenceSprite, -40, 0),
            createAdjacent(referenceSprite, 0, 40),
            createAdjacent(referenceSprite, 0, -40),
            createAdjacent(referenceSprite, 80, 0),
            createAdjacent(referenceSprite, -80, 0),
            createAdjacent(referenceSprite, 0, 80),
            createAdjacent(referenceSprite, 0, -80)
        };
    }

    private Sprite createAdjacent(Sprite base, int xShift, int yShift) {
        return new Sprite(
            base.getX() + xShift, 
            base.getY() + yShift, 
            base.getWidth(), 
            base.getHeight(), 
            base.getImage());
    }

    // Getters and setters
    public int getX() { return positionX; }
    public void setX(int x) { this.positionX = x; }
    public int getY() { return positionY; }
    public void setY(int y) { this.positionY = y; }
    public int getWidth() { return objectWidth; }
    public void setWidth(int width) { this.objectWidth = width; }
    public int getHeight() { return objectHeight; }
    public void setHeight(int height) { this.objectHeight = height; }
    public Image getImage() { return objectImage; }
}
