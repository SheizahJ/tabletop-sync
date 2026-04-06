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

import static tabletopsync.canvas.controller.CanvasController.selectedSprite;
import static tabletopsync.canvas.controller.CanvasController.sprites;

// TODO: Create a constant variable in Canvas somewhere to store grid size (50x50)
public class Sprite {
    private BufferedImage spriteImage;
    private SelectionBox selectionBox;
    private boolean isLocked;
    private boolean isSelected;
    private int width;
    private int height;
    private Point position;

    // CONSTRUCTOR
    public Sprite(URL imageURL) throws IOException {
        BufferedImage original = ImageIO.read(imageURL);
        // Max sure the sprite is a max size we choose, if not make it smaller
        int maxSize = 512;
        int width = original.getWidth();
        int height = original.getHeight();

        if (width > maxSize || height > maxSize) {

            double scale = Math.min((double) maxSize / width, (double) maxSize / height);

            int newWidth = (int)(width * scale);
            int newHeight = (int)(height * scale);

            Image scaled = original.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resized.createGraphics();
            g2.drawImage(scaled, 0, 0, null);
            g2.dispose();

            this.spriteImage = resized;

        } else {
            this.spriteImage = original;
        }

        // keep these exactly the same
        this.selectionBox = new SelectionBox(this);
        resetSpriteDefaults();
    }

    // GETTERS & SETTERS
    private void resetSpriteDefaults() {
        this.isLocked = false;
        this.isSelected = false;
        this.width = 100;
        this.height = 100;
        this.position = new Point(0, 0);
    }

    public BufferedImage getImage() {
        return spriteImage;
    }

    public SelectionBox getSelectionBox() {
        return selectionBox;
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

    // SPRITE METHODS
    public void move(int x, int y) {
        this.position.setLocation(x, y);
    }

    public void delete() {
        sprites.remove(this);
    }

    public void select(String username) {
        selectedSprite = this;
        this.isSelected = true;

        // bring sprite to front
        sprites.remove(selectedSprite);
        sprites.add(selectedSprite);

        // TODO: create a box around the selected sprite w/ player colour
        selectionBox.show(username);
    }

    public void deselect() {
        selectedSprite = null;
        this.isSelected = false;
        selectionBox.hide();
    }
}
