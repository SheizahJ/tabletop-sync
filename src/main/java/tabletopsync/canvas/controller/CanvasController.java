package tabletopsync.canvas.controller;

import tabletopsync.canvas.model.CanvasModel;
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
    private static boolean dragHeld;
    private static Point selectedOffset;

    private Point lastMousePoint;
    private boolean canDrag = false;

    private final CanvasModel model;
    private CanvasUpdateListener networkListener; // send updates to server

    public CanvasController(CanvasModel model) {
        this.model = model;
        setBackground(Color.WHITE);

        // CREATE TEST SPRITES ---------------------
        URL url = getClass().getResource("/test_images/mario.png");
        addDefaultSprite(url);
        addDefaultSprite(url);
        addDefaultSprite(url);

        MouseAdapter spriteMouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // only register if mouse button 1 (left click)
                if (e.getButton() != MouseEvent.BUTTON1) {
                    return;
                }

                // get the mouse click coordinates
                int mouseX = e.getX();
                int mouseY = e.getY();

                // check all sprites for selection
                for (int i = Sprite.getCreatedSprites().size() - 1; i >= 0; i--) {
                    Sprite sprite = Sprite.getCreatedSprites().get(i);
                    Point spritePosition = sprite.getPosition();

                    boolean isInWidthBounds = mouseX >= spritePosition.x &&
                            mouseX <= spritePosition.x + sprite.getWidth();
                    boolean isInHeightBounds = mouseY >= spritePosition.y &&
                            mouseY <= spritePosition.y + sprite.getHeight();

                    int offsetX = spritePosition.x - mouseX;
                    int offsetY = spritePosition.y - mouseY;

                    System.out.println(offsetX + ", " + offsetY);

                    // select sprite
                    if (isInWidthBounds && isInHeightBounds) {
                        System.out.println("Clicked on sprite at index: " + i);
                        dragHeld = true;
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
                dragHeld = false;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragHeld) {
                    // move sprite with mouse
                    Sprite selectedSprite = Sprite.getSelectedSprite();
                    selectedSprite.move(e.getX() + selectedOffset.x, e.getY() + selectedOffset.y);
                    System.out.println(Sprite.getSelectedSprite().getPosition());

                    // TODO: notify server of change
                    repaint();
                }
            }
        };

        addMouseListener(spriteMouseAdapter);
        addMouseMotionListener(spriteMouseAdapter);


        // listen to model updates for repaint
        model.addListener((x, y) -> SwingUtilities.invokeLater(this::repaint));

        MouseAdapter canvasControl = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON2) {
                    lastMousePoint = e.getPoint();
                    canDrag = true;
                }
            }

            @Override public void mouseReleased(MouseEvent e) { canDrag = false; }

            @Override public void mouseDragged(MouseEvent e) {
                if (canDrag) {
                    int dx = e.getX() - lastMousePoint.x;
                    int dy = e.getY() - lastMousePoint.y;

                    // move camera in model
                    model.moveCamera(dx, dy);

                    // notify server
                    if (networkListener != null) networkListener.onCameraDragged(model.getCameraX(), model.getCameraY());

                    lastMousePoint = e.getPoint();
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

        g2.translate(-model.getCameraX(), -model.getCameraY());


        // SPRITE TEST
//        g2.setColor(Color.BLUE);
//        g2.fillOval(0, 0, 100, 100);

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

    public void addDefaultSprite(URL fileURL) {
        try {
            if (fileURL != null) {
                Sprite sprite = new Sprite(fileURL);
                sprite.move(Sprite.getCreatedSprites().size() * 150, 0); // FOR TESTING PURPOSES
                Sprite.getCreatedSprites().add(sprite);
                repaint();
            }
        } catch (IOException e) {
            e.printStackTrace();
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