package yogi;

import java.awt.Image;

public class Mountain extends Sprite {
// Spatial properties of the object
private int x;      // X-coordinate position
private int y;      // Y-coordinate position
private int width;  // Width of the object
private int height; // Height of the object

// Visual representation
private Image image; // Image used for the object's visual representation


    
    public Mountain(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

}
