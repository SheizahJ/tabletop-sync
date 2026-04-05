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
        this.spriteImage = ImageIO.read(imageURL);
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
