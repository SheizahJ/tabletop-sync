package tabletopsync.canvas.controller;

import tabletopsync.canvas.model.CanvasModel;
import tabletopsync.canvas.model.SelectionBox;
import tabletopsync.canvas.model.Sprite;
import tabletopsync.canvas.model.SpritePopup;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

/**
 * Handles camera drag controls on canvas and redraws the canvas sprites
 */
public class CanvasController extends JPanel {
    private static final String resourceFolder = "src/main/resources/test_images/";

    private static final int MAP_HANDLE_SIZE = 12;
    private static final int MIN_MAP_SIZE = 50;
    public final CanvasModel model;
    private final SpritePopup spritePopup;
    public Color colorSelection;
    private Point lastCameraPoint;
    private Point selectedOffset;
    private Point mapResizeStartPoint;
    private Dimension mapResizeStartSize;
    public CanvasUpdateListener networkListener; // send updates to server

    /**
     * Handles the canvas functions interacting with the Sprite object and other model attributes:
     *  - initializes objects, visual attributes, action listeners, and various functionality of the canvas.
     *  - creates action controller elements and functions for SPRITES:
     *      - mouse pressed
     *      - mouse released
     *      - show menu
     *   ** Further info below
     * @param model
     */
    public CanvasController(CanvasModel model, Color colorSelection) throws IOException {
        this.model = model;
        this.colorSelection = colorSelection;
        spritePopup = new SpritePopup(this, model);
        setBackground(Color.WHITE);

        // listen to model updates for repaint
        model.addListener((x, y) -> SwingUtilities.invokeLater(this::repaint));

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
                        String uuidToDelete = selectedSprite.getUuid();

                        // delete sprite
                        model.removeSprite(selectedSprite);
                        networkListener.onSpriteRemoved(uuidToDelete);

                        // redraw canvas
                        repaint();
                    }
                }
            }
        });


        /** ---------- Sprite Popup Menu - Sprite Action Functions--------------
         * M3 button functionality:
         */
        addMouseListener(new MouseAdapter() {
            /**
             * Creates an event function for M3 mouse button when it is pressed
             * {@code @references} showMenu
             * @param e the event to be processed
             */
            @Override
            public void mousePressed(MouseEvent e) {
                // checks right click (M3)
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            /**
             * Creates an event function for M3 mouse button when it is released (while pressed)
             * {@code @references} showMenu
             * @param e the event to be processed
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            /**
             * Helper method:
             * Uses spritePopup to display menu where the mouse is per x-y coordinates
             * {@code @helper} mouseReleased, mousePressed
             * @param e
             */
            private void showMenu(MouseEvent e) {
                if (model.getSelectedSprite() != null) {
                    spritePopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    /**
     * Utilizes graphics to display canvas and draws the canvas grid background utilizing the Graphics2D library
     * @param g2
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
     * Handles various view functionalities:
     *  - camera movement and zoom
     *  - draws background map
     *  - draws a grid of the map
     *  - draws sprites
     * @param g the <code>Graphics</code> object to protect
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
        if (model.isGridVisible()) {
            drawCanvasGrid(g2);
        }

        // draw sprites on top of grid
        drawSpritesOnCanvas(g2);
    }

    /**
     * Sets a map from a chosen image file
     * @param file
     */
    public void setMapFromFile(File file, int width, int height) {
        try {
            String resourcePath = addToImageResource(file);
            model.setMapResourcePath(resourcePath);

            BufferedImage img = ImageIO.read(file);
            model.setMapImage(img);

            model.setMapPosition(model.getCameraX(), model.getCameraY());
            model.setMapSize(width, height);

            if (networkListener != null) {
                networkListener.onMapUpdate(resourcePath, width, height);
            }

            repaint();
        } catch (IOException e) {
            System.err.println("Failed to load map image: " + e.getMessage());
        }
    }

    /**
     * Updates map with provided params
     * @param resourcePath
     */
    public void setMap(String resourcePath, int width, int height) {
        try {
            URL mapURL = Objects.requireNonNull(getClass().getResource(resourcePath)).toURI().toURL();
            BufferedImage img = ImageIO.read(mapURL);
            model.setMapResourcePath(resourcePath);
            model.setMapImage(img);

            // place map at current canvas view
            model.setMapPosition(model.getCameraX(), model.getCameraY());
            model.setMapSize(width, height);

            repaint();
        } catch (IOException | URISyntaxException e) {
            System.err.println("Failed to load map image using resource path: " + e.getMessage());
        }
    }

    public String addToImageResource(File file) throws IOException {
        // copy file into resources/sprites folder
        new File(resourceFolder).mkdirs();

        String fileName = file.getName();
        File destination = new File(resourceFolder + fileName);
        Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // use classpath resource path
        return "/test_images/" + fileName;
    }

    /**
     * Gets the bounds of the gui component for the map
     * @return rectangle image of map
     */
    private Rectangle getMapBounds() {
        return new Rectangle(
                model.getMapX(),
                model.getMapY(),
                model.getMapWidth(),
                model.getMapHeight()
        );
    }

    /**
     * handles resizing the map to fit the GUI bounds
     * @return rectangle
     */
    private Rectangle getMapResizeHandleBounds() {
        return new Rectangle(
                model.getMapX() + model.getMapWidth() - MAP_HANDLE_SIZE,
                model.getMapY() + model.getMapHeight() - MAP_HANDLE_SIZE,
                MAP_HANDLE_SIZE,
                MAP_HANDLE_SIZE
        );
    }

    /**
     * redraws the map to fill the gui component
     * @param g2
     */
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

    /**
     * adds sprite to the map
     * @param fileURL
     */
    public void addSpriteToCanvas(URL fileURL) {
        try {
            if (fileURL != null) {
                Sprite sprite = new Sprite(model, fileURL);

                // Creates sprite with the camera's current position
                sprite.move(model.getCameraX(), model.getCameraY());
                model.addSprite(sprite);

                    networkListener.onSpriteAdded(
                            sprite.getUuid(),
                            sprite.getResourcePath(),
                            sprite.getPosition().x,
                            sprite.getPosition().y
                    );
                repaint();
            }
        } catch (IOException e) {
            System.err.println("Error when creating sprite: " + e.getMessage());
        }
    }

    /**
     * Adds the uploaded sprite file to the map
     * @param file
     */
    public void addSpriteFromFile(File file) {
        try {
            if (file == null) return;

            String resourcePath = addToImageResource(file);
            Sprite sprite = new Sprite(model, resourcePath);

            if (networkListener != null) {
                networkListener.onSpriteAdded(
                        sprite.getUuid(),
                        sprite.getResourcePath(),
                        sprite.getPosition().x,
                        sprite.getPosition().y
                );
            }

            repaint();
        } catch (IOException e) {
            System.err.println("Error when adding sprite from file: " + e.getMessage());
        }
    }

    /**
     * updates the sprite to server
     * @param resourcePath
     * @param uuid
     * @param x
     * @param y
     */
    public void addSpriteFromNetwork(String resourcePath, String uuid, int x, int y) {
        try {
            Sprite sprite = new Sprite(model, resourcePath, uuid);
            sprite.move(x, y);
            model.addSprite(sprite);
            repaint();
        } catch (IOException e) {
            System.err.println("Error when adding sprite from network: " + e.getMessage());
        }
    }


    public void removeSpriteFromNetwork(String uuid) {
        Sprite sprite = model.getSpriteByUuid(uuid);
        if (sprite != null) {
            model.removeSprite(sprite);
            repaint();
        } else {
            System.err.println("Could not find sprite with uuid: " + uuid);
        }
    }

    /**
     * adds resource path of sprite to be updated and accessed by multiple threads
     * @param resourcePath
     */
    public void addSharedSpriteToCanvas(String resourcePath) {
        try {
            Sprite sprite = new Sprite(model, resourcePath);
            sprite.move(model.getCameraX(), model.getCameraY());
            model.addSprite(sprite);

            if (networkListener != null) {
                networkListener.onSpriteAdded(
                        sprite.getUuid(),
                        sprite.getResourcePath(),
                        sprite.getPosition().x,
                        sprite.getPosition().y
                );
            }

            repaint();
        } catch (IOException e) {
            System.err.println("Error when creating shared sprite: " + e.getMessage());
        }
    }

    /**
     * draws sprites on canvas element
     * @param g2
     */
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
                g2.setColor(colorSelection);
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

    public void setCanvasListener(CanvasUpdateListener listener) {
        this.networkListener = listener;
    }

    // interface for server updates
    public interface CanvasUpdateListener {
        void onSpriteAdded(String uuid, String resourcePath, int x, int y);

        void onSpriteMoved(String uuid, int x, int y);

        void onSpriteRemoved(String uuid);

        void onMapUpdate(String resourcePath, int width, int height);
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
            int worldX = (int) (e.getX() / model.getZoom()) + model.getCameraX();
            int worldY = (int) (e.getY() / model.getZoom()) + model.getCameraY();

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
                    model.getSelectedSprite().select(model.username);
                    model.setIsSelecting(true);
                    selectedOffset = new Point(pos.x - worldX, pos.y - worldY);
                    repaint();
                    break;
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            model.setIsSelecting(false);
            model.setMapResizing(false);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (model.isMapResizing()) {
                int worldX = (int) (e.getX() / model.getZoom()) + model.getCameraX();
                int worldY = (int) (e.getY() / model.getZoom()) + model.getCameraY();

                int newWidth = mapResizeStartSize.width + (worldX - mapResizeStartPoint.x);
                int newHeight = mapResizeStartSize.height + (worldY - mapResizeStartPoint.y);

                newWidth = Math.max(MIN_MAP_SIZE, newWidth);
                newHeight = Math.max(MIN_MAP_SIZE, newHeight);

                model.setMapSize(newWidth, newHeight);

                if (networkListener != null) {
                    String currentPath = model.getMapResourcePath();
                    networkListener.onMapUpdate(currentPath, newWidth, newHeight);
                }

                repaint();
                return;
            }

            if (model.isSelecting() && !model.getSelectedSprite().isLocked()) {
                int worldX = (int) (e.getX() / model.getZoom()) + model.getCameraX();
                int worldY = (int) (e.getY() / model.getZoom()) + model.getCameraY();

                int targetX = worldX + selectedOffset.x;
                int targetY = worldY + selectedOffset.y;

                if (model.snapsToGrid()) {
                    int grid = CanvasModel.GRID_SIZE;
                    targetX = Math.round((float) targetX / grid) * grid;
                    targetY = Math.round((float) targetY / grid) * grid;
                }

                model.getSelectedSprite().move(targetX, targetY);

                if (model.getSelectedSprite() != null && networkListener != null) {
                    Sprite sprite = model.getSelectedSprite();
                    networkListener.onSpriteMoved(
                            sprite.getUuid(),
                            sprite.getPosition().x,
                            sprite.getPosition().y
                    );
                }

                repaint();
            }
        }
    }
}