package yogi;

import java.awt.Image;

public class Ranger extends Sprite {
// Position and size attributes
private int x;      // X-coordinate of the object
private int y;      // Y-coordinate of the object
private int width;  // Width of the object
private int height; // Height of the object

// Visual representation
private Image image; // Image representing the object visually

// Movement attributes
private double velx; // Velocity of the object along the X-axis
private double vely; // Velocity of the object along the Y-axis


    public Ranger(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        initializeVelocity();
    }

    private void initializeVelocity() {
        double rand = Math.random() * 2 - 1;
        velx = rand >= 0 ? 1 : -1;
        vely = rand < 0 ? 1 : -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final Ranger other = (Ranger) obj;
        return Double.doubleToLongBits(this.velx) == Double.doubleToLongBits(other.velx) &&
               Double.doubleToLongBits(this.vely) == Double.doubleToLongBits(other.vely);
    }

    public void moveX() {
        x += velx;
        checkAndInvertX();
    }

    private void checkAndInvertX() {
        if (x + width >= 400 || x <= 0) {
            invertVelX();
        }
    }

    public void moveY() {
        y += vely;
        checkAndInvertY();
    }

    private void checkAndInvertY() {
        if (y + height >= 400 || y <= 0) {
            invertVelY();
        }
    }

    public void move() {
        if (velx != 0) {
            moveX();
        } else {
            moveY();
        }
    }

    public void invertVel() {
        if (velx != 0) {
            invertVelX();
        } else {
            invertVelY();
        }
    }

    public void invertVelX() {
        velx = -velx;
    }

    public void invertVelY() {
        vely = -vely;
    }
}