package tabletopsync.canvas.controller;

import tabletopsync.canvas.model.CanvasModel;
import tabletopsync.canvas.model.SelectionBox;
import tabletopsync.canvas.model.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles camera drag controls on canvas and redraws the canvas sprites
 */
public class CanvasController extends JPanel {
    public static List<Sprite> sprites = new ArrayList<>();
    public static Sprite selectedSprite;
    private static boolean isSelecting = false;
    private static boolean cameraCanMove = false;
    private static Point selectedOffset;
    private static Point lastCameraPoint;
    // size of each grid square
    private static final int GRID_SIZE = 50;
    private final CanvasModel model;
    private CanvasUpdateListener networkListener; // send updates to server

    public CanvasController(CanvasModel model) {
        this.model = model;
        setBackground(Color.WHITE);

        // listen to model updates for repaint
        model.addListener((x, y) -> SwingUtilities.invokeLater(this::repaint));

        // FOR TESTING PURPOSES: ADD TEST SPRITES ---------------------
        // TODO: Create "Add Sprite" button to do this instead
        URL url = getClass().getResource("/test_images/mario.png");
        addSpriteToCanvas(url);
        addSpriteToCanvas(url);
        addSpriteToCanvas(url);
        // ------------------------------------------------------------

        // Canvas adapter
        CanvasMouseAdapter canvasControl = new CanvasMouseAdapter();
        addMouseListener(canvasControl);
        addMouseMotionListener(canvasControl);

        // zoom using mouse wheel
        addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                model.zoom(0.1); // zoom in
            } else {
                model.zoom(-0.1); // zoom out
            }
        });

        // Sprite adapter
        SpriteMouseAdapter spriteMouseAdapter = new SpriteMouseAdapter();
        addMouseListener(spriteMouseAdapter);
        addMouseMotionListener(spriteMouseAdapter);
    }

    /**
     * Draws the canvas grid background
     */
    private void drawCanvasGrid(Graphics2D g2) {
        g2.setColor(new Color(220, 220, 220)); // light gray grid color

        int cameraX = model.getCameraX();
        int cameraY = model.getCameraY();

        double zoom = model.getZoom();

        // visible canvas size in world coordinates
        int visibleWorldWidth = (int) Math.ceil(getWidth() / zoom);
        int visibleWorldHeight = (int) Math.ceil(getHeight() / zoom);

        // find first grid line before the visible area
        int startX = (int) Math.floor((double) cameraX / GRID_SIZE) * GRID_SIZE;
        int startY = (int) Math.floor((double) cameraY / GRID_SIZE) * GRID_SIZE;

        int endX = cameraX + visibleWorldWidth;
        int endY = cameraY + visibleWorldHeight;

        // vertical lines
        for (int x = startX; x <= endX; x += GRID_SIZE) {
            g2.drawLine(x, cameraY, x, endY);
        }

        // horizontal lines
        for (int y = startY; y <= endY; y += GRID_SIZE) {
            g2.drawLine(cameraX, y, endX, y);
        }
    }

    /**
     * draws sprite
     */
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // apply zoom
        g2.scale(model.getZoom(), model.getZoom());

        // move camera
        g2.translate(-model.getCameraX(), -model.getCameraY());

        // draw the grid first (background)
        drawCanvasGrid(g2);

        // draw sprites on top of grid
        drawSpritesOnCanvas(g2);
    }

    public void addSpriteToCanvas(URL fileURL) {
        try {
            if (fileURL != null) {
                Sprite sprite = new Sprite(fileURL);

                // Creates sprite with the camera's current position
                sprite.move(model.getCameraX(), model.getCameraY());
                sprites.add(sprite);
                repaint();
            }
        } catch (IOException e) {
            System.err.println("Error when creating sprite: " + e.getMessage());
        }
    }
    public void addSpriteFromFile(File file) {
        try {
            if (file != null) {
                addSpriteToCanvas(file.toURI().toURL());
            }
        } catch (Exception e) {
            System.err.println("Error when adding sprite from file: " + e.getMessage());
        }
    }

    public void drawSpritesOnCanvas(Graphics2D g2) {
        for (Sprite s : sprites) {
            g2.drawImage(
                    s.getImage(),
                    s.getPosition().x,
                    s.getPosition().y,
                    s.getWidth(),
                    s.getHeight(),
                    null
            );

            SelectionBox box = s.getSelectionBox();
            if (box.isVisible()) {
                Rectangle r = box.getBounds();

                // draw border
                g2.setColor(Color.decode("#DDBBFF")); // TODO: change to player's colour
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(r.x, r.y, r.width, r.height);

                // player username
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                g2.drawString(box.getSelectedBy().toUpperCase(), r.x, r.y - 5);
            }
        }
    }

    //------------------------------
    // SERVER STUFF-----> network
    //------------------------------
    public void setNetworkListener(CanvasUpdateListener listener) {
        this.networkListener = listener;
    }

    // interface for server updates
    public interface CanvasUpdateListener {
        void onCameraDragged(int x, int y);
    }

    public class CanvasMouseAdapter extends MouseAdapter {
        @Override public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON2) {
                lastCameraPoint = e.getPoint();
                cameraCanMove = true;
            }
        }

        @Override public void mouseReleased(MouseEvent e) { cameraCanMove = false; }

        @Override public void mouseDragged(MouseEvent e) {
            if (cameraCanMove) {
                int dx = e.getX() - lastCameraPoint.x;
                int dy = e.getY() - lastCameraPoint.y;

                // move camera in model
                model.moveCamera(dx, dy);

                // notify server
                if (networkListener != null) networkListener.onCameraDragged(model.getCameraX(), model.getCameraY());

                lastCameraPoint = e.getPoint();
            }
        }
    }

    public class SpriteMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            // only register if mouse button 1 (left click)
            if (e.getButton() != MouseEvent.BUTTON1) {
                return;
            }

            // deselect previous sprite
            if (selectedSprite != null) {
                selectedSprite.deselect();
                repaint();
            }

            // get the mouse click coordinates
            int worldX = (int)(e.getX() / model.getZoom()) + model.getCameraX();
            int worldY = (int)(e.getY() / model.getZoom()) + model.getCameraY();

            // check all sprites for selection
            for (int i = sprites.size() - 1; i >= 0; i--) {
                Sprite sprite = sprites.get(i);
                Point pos = sprite.getPosition();

                boolean isInWidthBounds = worldX >= pos.x && worldX <= pos.x + sprite.getWidth();
                boolean isInHeightBounds = worldY >= pos.y && worldY <= pos.y + sprite.getHeight();

//                sprite.deselect();

                // select sprite
                if (isInWidthBounds && isInHeightBounds) {
                    isSelecting = true;
                    selectedSprite = sprite;
                    selectedOffset = new Point(pos.x - worldX, pos.y - worldY);
                    selectedSprite.select("Player #1"); // TODO: get username
                    repaint();
                    break;
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            isSelecting = false;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (isSelecting) {
                // move sprite with mouse
                int worldX = (int)(e.getX() / model.getZoom()) + model.getCameraX();
                int worldY = (int)(e.getY() / model.getZoom()) + model.getCameraY();
                selectedSprite.move(worldX + selectedOffset.x, worldY + selectedOffset.y);

                // TODO: notify server of change

                repaint();
            }
        }
    }
}