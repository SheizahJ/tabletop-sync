package tabletopsync.canvas.controller;

import tabletopsync.canvas.model.CanvasModel;
import tabletopsync.canvas.model.SelectionBox;
import tabletopsync.canvas.model.Sprite;
import tabletopsync.canvas.model.SpritePopup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Handles camera drag controls on canvas and redraws the canvas sprites
 */
public class CanvasController extends JPanel {
    private final SpritePopup spritePopup;
    private Point lastCameraPoint;
    private Point selectedOffset;
    private SpriteMoveListener spriteMoveListener;
    private static final int MAP_HANDLE_SIZE = 12;
    private static final int MIN_MAP_SIZE = 50;
    private Point mapResizeStartPoint;
    private Dimension mapResizeStartSize;

    public final CanvasModel model;
    private CanvasUpdateListener networkListener; // send updates to server


    public CanvasController(CanvasModel model) {
        this.model = model;
        spritePopup = new SpritePopup(this, model);
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

        // allow panel to receive keyboard input
        setFocusable(true);

        // listen for delete key
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE) {
                    Sprite selectedSprite = model.getSelectedSprite();
                    if (selectedSprite != null) {
                        // remove sprite
                        model.removeSprite(selectedSprite);

                        // redraw canvas
                        repaint();
                    }
                }
            }
        });

        // Sprite Popup Menu
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            private void showMenu(MouseEvent e) {
                if (model.getSelectedSprite() != null) {
                    spritePopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
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
        int GRID_SIZE = CanvasModel.GRID_SIZE;
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

        // draw map if one exists
        if (model.getMapImage() != null) {
            g2.drawImage(
                    model.getMapImage(),
                    model.getMapX(),
                    model.getMapY(),
                    model.getMapWidth(),
                    model.getMapHeight(),
                    null
            );

            drawMapResizeHandle(g2);
        }

        // draw the grid first (background)
        drawCanvasGrid(g2);

        // draw sprites on top of grid
        drawSpritesOnCanvas(g2);
    }


    public void setMapFromFile(File file) {
        try {
            BufferedImage img = ImageIO.read(file);
            model.setMapImage(img);

            // place map at current canvas view
            model.setMapPosition(model.getCameraX(), model.getCameraY());

            repaint();
        } catch (IOException e) {
            System.err.println("Failed to load map image: " + e.getMessage());
        }
    }

    private Rectangle getMapBounds() {
        return new Rectangle(
                model.getMapX(),
                model.getMapY(),
                model.getMapWidth(),
                model.getMapHeight()
        );
    }

    private Rectangle getMapResizeHandleBounds() {
        return new Rectangle(
                model.getMapX() + model.getMapWidth() - MAP_HANDLE_SIZE,
                model.getMapY() + model.getMapHeight() - MAP_HANDLE_SIZE,
                MAP_HANDLE_SIZE,
                MAP_HANDLE_SIZE
        );
    }

    private void drawMapResizeHandle(Graphics2D g2) {
        if (model.getMapImage() == null) {
            return;
        }

        Rectangle handle = getMapResizeHandleBounds();

        g2.setColor(Color.WHITE);
        g2.fillRect(handle.x, handle.y, handle.width, handle.height);

        g2.setColor(Color.BLACK);
        g2.drawRect(handle.x, handle.y, handle.width, handle.height);
    }

    public void addSpriteToCanvas(URL fileURL) {
        try {
            if (fileURL != null) {
                Sprite sprite = new Sprite(model, fileURL);

                // Creates sprite with the camera's current position
                sprite.move(model.getCameraX(), model.getCameraY());
                model.addSprite(sprite);
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
        for (Sprite s : model.getSpritesOnCanvas()) {
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

    public interface SpriteMoveListener {
        void onSpriteMoved(String uuid, int x, int y);
    }

    public void setSpriteMoveListener(SpriteMoveListener listener) {
        this.spriteMoveListener = listener;
    }

    public class CanvasMouseAdapter extends MouseAdapter {
        @Override public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON2) {
                lastCameraPoint = e.getPoint();
                model.setCameraCanMove(true);
            }
        }

        @Override public void mouseReleased(MouseEvent e) { model.setCameraCanMove(false); }

        @Override public void mouseDragged(MouseEvent e) {
            if (model.canCameraMove()) {
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
            if (e.getButton() != MouseEvent.BUTTON1 && e.getButton() != MouseEvent.BUTTON3) {
                return;
            }
            requestFocusInWindow();

            // deselect previous sprite
            if (model.getSelectedSprite() != null) {
                model.getSelectedSprite().deselect();
                repaint();
            }

            // get the mouse click coordinates
            int worldX = (int)(e.getX() / model.getZoom()) + model.getCameraX();
            int worldY = (int)(e.getY() / model.getZoom()) + model.getCameraY();

            // start resizing map if the handle is clicked
            if (model.getMapImage() != null && getMapResizeHandleBounds().contains(worldX, worldY)) {
                model.setMapResizing(true);
                mapResizeStartPoint = new Point(worldX, worldY);
                mapResizeStartSize = new Dimension(model.getMapWidth(), model.getMapHeight());
                repaint();
                return;
            }

            // check all sprites for selection
            for (int i = model.getSpritesOnCanvas().size() - 1; i >= 0; i--) {
                Sprite sprite = model.getSpritesOnCanvas().get(i);
                Point pos = sprite.getPosition();

                boolean isInWidthBounds = worldX >= pos.x && worldX <= pos.x + sprite.getWidth();
                boolean isInHeightBounds = worldY >= pos.y && worldY <= pos.y + sprite.getHeight();

                // select sprite
                if (isInWidthBounds && isInHeightBounds) {
                    model.selectSprite(sprite);
                    model.getSelectedSprite().select(model.username); // TODO: get username
                    selectedOffset = new Point(pos.x - worldX, pos.y - worldY);
                    repaint();
                    break;
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (model.getSelectedSprite() != null && spriteMoveListener != null) {
                Sprite sprite = model.getSelectedSprite();
                spriteMoveListener.onSpriteMoved(
                        sprite.getUuid(),
                        sprite.getPosition().x,
                        sprite.getPosition().y
                );
            }

            model.setIsSelecting(false);
            model.setMapResizing(false);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (model.isMapResizing()) {
                int worldX = (int)(e.getX() / model.getZoom()) + model.getCameraX();
                int worldY = (int)(e.getY() / model.getZoom()) + model.getCameraY();

                int newWidth = mapResizeStartSize.width + (worldX - mapResizeStartPoint.x);
                int newHeight = mapResizeStartSize.height + (worldY - mapResizeStartPoint.y);

                newWidth = Math.max(MIN_MAP_SIZE, newWidth);
                newHeight = Math.max(MIN_MAP_SIZE, newHeight);

                model.setMapSize(newWidth, newHeight);
                repaint();
                return;
            }
            
            if (model.isSelecting() && !model.getSelectedSprite().isLocked()) {
                int worldX = (int)(e.getX() / model.getZoom()) + model.getCameraX();
                int worldY = (int)(e.getY() / model.getZoom()) + model.getCameraY();

                int targetX = worldX + selectedOffset.x;
                int targetY = worldY + selectedOffset.y;

                if (model.snapsToGrid()) {
                    int grid = CanvasModel.GRID_SIZE;
                    targetX = Math.round((float)targetX / grid) * grid;
                    targetY = Math.round((float)targetY / grid) * grid;
                }

                model.getSelectedSprite().move(targetX, targetY);

                // TODO: notify server of change

                repaint();
            }
        }
    }
}