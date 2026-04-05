package tabletopsync.canvas.model;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

// TODO: Create a constant variable in Canvas somewhere to store grid size (50x50)
public class Sprite {
    private BufferedImage spriteImage;
    private boolean isLocked;
    private String selectedBy;
    private int width;
    private int height;
    private Point position;

    // CONSTRUCTOR
    public Sprite(URL imageURL) throws IOException {
        this.spriteImage = ImageIO.read(imageURL);

        // Defaults
        this.isLocked = false;
        this.selectedBy = null;
        this.width = 100;
        this.height = 100;
        this.position = new Point(0, 0);
    }


    // GETTERS & SETTERS
    public BufferedImage getImage() {
        return spriteImage;
    }

    public void setLockedState(boolean locked) {
        isLocked = locked;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setSelectedBy(String username) {
        this.selectedBy = username;
    }

    public String getSelectedBy() {
        return selectedBy;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int[] getSize() {
        return new int[]{width, height};
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point getPosition() {
        return position;
    }

    // SPRITE METHODS
    public void moveSprite(int x, int y) {
        this.position.move(x, y);
    }
}
