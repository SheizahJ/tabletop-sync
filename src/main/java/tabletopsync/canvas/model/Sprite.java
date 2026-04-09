package tabletopsync.canvas.model;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;


public class Sprite {
    private final CanvasModel model;
    private final BufferedImage spriteImage;
    private final SelectionBox selectionBox;
    private SpritePopup popup;
    private boolean isLocked;
    private boolean isSelected;
    private int width;
    private int height;
    private Point position;

    // CONSTRUCTOR
    public Sprite(CanvasModel model, URL imageURL) throws IOException {
        this.model = model;
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

    public void select(String username) {
        // bring sprite to front
        model.removeSprite(this);
        model.addSprite(this);

        this.isSelected = true;
        model.selectSprite(this);
        selectionBox.show(username);
    }

    public void deselect() {
        this.isSelected = false;
        model.unselectSprites();
        selectionBox.hide();
    }
}
