package yogi;

import java.awt.Image;


public class Basket extends Sprite {
// Position and size attributes
private int x;      // X-coordinate of the object
private int y;      // Y-coordinate of the object
private int width;  // Width of the object
private int height; // Height of the object

// Visual representation
private Image image; // Image representing the object visually


    
    public Basket(int x, int y, int width, int height, Image image){
        super(x, y, width, height, image);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

}