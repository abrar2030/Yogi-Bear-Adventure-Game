package yogi;

import java.awt.Image;
import java.awt.Graphics;


public class Tree extends Sprite {
    private boolean inBloom;
    private Image imageInBloom;

    
    public Tree(int posX, int posY, int treeWidth, int treeHeight, Image standardImage) {
        this(posX, posY, treeWidth, treeHeight, standardImage, null);  // Delegate to the main constructor
    }

    
    public Tree(int posX, int posY, int treeWidth, int treeHeight, Image standardImage, Image bloomingImg) {
        super(posX, posY, treeWidth, treeHeight, standardImage);
        this.imageInBloom = bloomingImg != null ? bloomingImg : standardImage;  // Default to standard image if blooming image is absent
        this.inBloom = false;
    }

    
    public void switchBloomingState() {
        inBloom = !inBloom;
    }

    
    public void draw(Graphics graphics) {
        Image currentImg = inBloom ? imageInBloom : getImage();
        graphics.drawImage(currentImg, getX(), getY(), getWidth(), getHeight(), null);
    }

}
