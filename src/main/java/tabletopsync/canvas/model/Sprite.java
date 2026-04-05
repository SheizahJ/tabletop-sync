package tabletopsync.canvas.model;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// TODO: Create a constant variable in Canvas somewhere to store grid size (50x50)
public class Sprite {
    private static List<Sprite> sprites = new ArrayList<>();
    private static Sprite selectedSprite;

    // OBJECT VARIABLES
    private BufferedImage spriteImage;
    private boolean isLocked;
    private boolean isSelected;
    private String selectedBy;
    private int width;
    private int height;
    private Point position;

    // CONSTRUCTOR
    public Sprite(URL imageURL) throws IOException {
        this.spriteImage = ImageIO.read(imageURL);

        // Defaults
        resetSpriteDefaults();
    }

    // GETTERS & SETTERS
    private void resetSpriteDefaults() {
        this.isLocked = false;
        this.isSelected = false;
        this.selectedBy = null;
        this.width = 100;
        this.height = 100;
        this.position = new Point(0, 0);
    }

    public BufferedImage getImage() {
        return spriteImage;
    }

    public void setLockedState(boolean locked) {
        isLocked = locked;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getSelectedBy() {
        return selectedBy;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
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

    // STATIC METHODS
    public static List<Sprite> getCreatedSprites() {
        return sprites;
    }

    public static Sprite getSelectedSprite() {
        return selectedSprite;
    }

    // SPRITE METHODS
    public void move(int x, int y) {
        this.position.setLocation(x, y);
    }

    public void select(String username) {
        selectedSprite = this;
        this.isSelected = true;
        this.selectedBy = username;

        // bring sprite to front
        sprites.remove(selectedSprite);
        sprites.add(selectedSprite);

        // TODO: create a box around the selected sprite w/ player colour
    }

    public void deselect() {
        selectedSprite = null;
        this.isSelected = false;
        this.selectedBy = null;

        // TODO: destroy the box (if any) around the sprite
    }
}
