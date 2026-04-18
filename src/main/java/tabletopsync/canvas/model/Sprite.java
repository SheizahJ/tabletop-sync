package tabletopsync.canvas.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;


public class Sprite {
    private final String uuid;
    private final String resourcePath;
    private final CanvasModel model;
    private final BufferedImage spriteImage;
    private final SelectionBox selectionBox;
    private SpritePopup popup;
    private boolean isLocked;
    private boolean isSelected;
    private int width;
    private int height;
    private Point position;

    // CONSTRUCTOR for a brand-new sprite from a shared resource path
    public Sprite(CanvasModel model, String resourcePath) throws IOException {
        this(model, resourcePath, UUID.randomUUID().toString());
    }

    // CONSTRUCTOR for an already existing sprite with a known uuid
    public Sprite(CanvasModel model, String resourcePath, String uuid) throws IOException {
        this.model = model;
        this.uuid = uuid;
        this.resourcePath = resourcePath;

        URL imageURL = getClass().getResource(resourcePath);
        BufferedImage original;
        if (imageURL != null) {
            original = ImageIO.read(imageURL);
        } else {
            // fallback: load directly from src/main/resources on disk
            File fallback = new File("src/main/resources" + resourcePath);
            original = ImageIO.read(fallback);
        }

        // make sure the sprite is a max size we choose, if not make it smaller
        int maxSize = 512;
        int width = original.getWidth();
        int height = original.getHeight();

        if (width > maxSize || height > maxSize) {

            double scale = Math.min((double) maxSize / width, (double) maxSize / height);

            int newWidth = (int) (width * scale);
            int newHeight = (int) (height * scale);

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

    //keep compatibility with existing URL-based code for now
    public Sprite(CanvasModel model, URL imageURL) throws IOException {
        this.model = model;
        this.uuid = UUID.randomUUID().toString();
        this.resourcePath = null;

        BufferedImage original = ImageIO.read(imageURL);

        int maxSize = 512;
        int width = original.getWidth();
        int height = original.getHeight();

        if (width > maxSize || height > maxSize) {
            double scale = Math.min((double) maxSize / width, (double) maxSize / height);

            int newWidth = (int) (width * scale);
            int newHeight = (int) (height * scale);

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


    public String getUuid() {
        return uuid;
    }

    public String getResourcePath() {
        return resourcePath;
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

    public void select(String username, Color color) {
        // bring sprite to front
        model.removeSprite(this);
        model.addSprite(this);

        this.isSelected = true;
        model.selectSprite(this);
        selectionBox.show(username, color);
    }

    public void deselect() {
        this.isSelected = false;
        model.unselectSprites();
        selectionBox.hide();
    }
}
