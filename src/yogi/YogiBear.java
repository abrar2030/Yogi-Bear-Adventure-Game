package yogi;

import java.awt.Image;


public class YogiBear extends Sprite {
    private double velocityX;
    private double velocityY;
    private String movementBlock;

    
    public YogiBear(int posX, int posY, int width, int height, Image image) {
        super(posX, posY, width, height, image);
    }

    public void horizontalMove() {
        if (canMoveHorizontally()) {
            positionX += (int) velocityX;
        }
    }

    private boolean canMoveHorizontally() {
        return (velocityX < 0 && positionX > 0) || (velocityX > 0 && positionX + objectWidth + velocityX <= 400);
    }

    public void verticalMove() {
        if (canMoveVertically()) {
            positionY += velocityY;
        }
    }

    private boolean canMoveVertically() {
        return (velocityY < 0 && positionY > 0) || (velocityY > 0 && positionY + objectHeight + velocityY <= 400);
    }

    public void move() {
        horizontalMove();
        verticalMove();
    }

    // Getters and Setters
    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public String getMovementBlock() {
        return movementBlock;
    }

    public void setMovementBlock(String movementBlock) {
        this.movementBlock = movementBlock;
    }

    @Override
    public Image getImage() {
        return objectImage;
    }
}
