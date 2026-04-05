package tabletopsync.canvas.controller;

import tabletopsync.canvas.model.CanvasModel;
import tabletopsync.canvas.model.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

/**
 * Handles camera drag controls on canvas and redraws the canvas sprites
 */
public class CanvasController extends JPanel {
    private static boolean spriteCanMove = false;
    private boolean cameraCanMove = false;
    private static Point selectedOffset;
    private Point lastCameraPoint;

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
//        addSpriteToCanvas(url);
//        addSpriteToCanvas(url);
        // ------------------------------------------------------------

        // Sprite adapter
        MouseAdapter spriteMouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // only register if mouse button 1 (left click)
                if (e.getButton() != MouseEvent.BUTTON1) {
                    return;
                }

                // get the mouse click coordinates
                int relativeX = e.getX() - Math.abs(model.getCameraX());
                int relativeY = e.getY() - Math.abs(model.getCameraY());

                // check all sprites for selection
                for (int i = Sprite.getCreatedSprites().size() - 1; i >= 0; i--) {
                    Sprite sprite = Sprite.getCreatedSprites().get(i);
                    Point spritePosition = sprite.getPosition();

                    boolean isInWidthBounds = relativeX >= spritePosition.x &&
                            relativeX <= spritePosition.x + sprite.getWidth();
                    boolean isInHeightBounds = relativeY >= spritePosition.y &&
                            relativeY <= spritePosition.y + sprite.getHeight();

//                    System.out.print("Sprite Position: " + spritePosition.x + ", " + spritePosition.y + " || ");
//                    System.out.print("Mouse Position: " + mouseX + ", " + mouseY + " || ");
//                    System.out.println("In Bounds: " + isInWidthBounds + ", " + isInHeightBounds);

                    int offsetX = spritePosition.x - e.getX();
                    int offsetY = spritePosition.y - e.getY();

                    // select sprite
                    if (isInWidthBounds && isInHeightBounds) {
                        spriteCanMove = true;
                        selectedOffset = new Point(offsetX, offsetY);
                        sprite.select("Player #1"); // TODO: get username
                        break;
                    }

                    // deselect sprite if already selected
                    else if (sprite.isSelected()) {
                        sprite.deselect();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                spriteCanMove = false;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (spriteCanMove) {
                    // move sprite with mouse
                    Sprite selectedSprite = Sprite.getSelectedSprite();
                    selectedSprite.move(e.getX() + selectedOffset.x, e.getY() + selectedOffset.y);

                    // TODO: notify server of change

                    repaint();
                }
            }
        };

        addMouseListener(spriteMouseAdapter);
        addMouseMotionListener(spriteMouseAdapter);

        // Canvas adapter
        MouseAdapter canvasControl = new MouseAdapter() {
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
        };

        addMouseListener(canvasControl);
        addMouseMotionListener(canvasControl);
    }

    /**
     * draws sprite
     */
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // move camera
        g2.translate(-model.getCameraX(), -model.getCameraY());

        // draw all sprites
        for (Sprite s : Sprite.getCreatedSprites()) {
            g2.drawImage(
                    s.getImage(),
                    s.getPosition().x,
                    s.getPosition().y,
                    s.getWidth(),
                    s.getHeight(),
                    null
            );
        }
    }

    public void addSpriteToCanvas(URL fileURL) {
        try {
            if (fileURL != null) {
                Sprite sprite = new Sprite(fileURL);

                // Creates sprite with the camera's current position
                sprite.move(model.getCameraX(), model.getCameraY());
                Sprite.getCreatedSprites().add(sprite);
                repaint();
            }
        } catch (IOException e) {
            System.err.println("Error when creating sprite: " + e.getMessage());
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
}