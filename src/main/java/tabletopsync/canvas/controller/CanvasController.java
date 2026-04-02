package tabletopsync.canvas.controller;

import tabletopsync.canvas.model.CanvasModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CanvasController extends JPanel {
    private Point lastMousePoint;
    private boolean canDrag = false;

    private static final int GRID_SIZE = 50;

    private CanvasModel model;

    /**
     *
     * @param model the model for the canvas where it handles camera movement
     */
    public CanvasController(CanvasModel model) {
        this.model = model;
        setBackground(Color.WHITE);

        MouseAdapter canvasControl = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON2) {
                    lastMousePoint = e.getPoint();
                    canDrag = true;
                }
            }

            @Override public void mouseReleased(MouseEvent e) {
                canDrag = false;
            }

            @Override public void mouseDragged(MouseEvent e) {
                if (canDrag) {
                    int dx = e.getX() - lastMousePoint.x;
                    int dy = e.getY() - lastMousePoint.y;

                    model.moveCamera(dx, dy); // move logic to model

                    lastMousePoint = e.getPoint();
                    repaint();
                }
            }
        };

        addMouseListener(canvasControl);
        addMouseMotionListener(canvasControl);
    }

    /**
     * Draws a grid across the canvas
     * @param g2 the Graphics2D object used for drawing
     */
    private void drawGrid(Graphics2D g2) {
        g2.setColor(new Color(220, 220, 220));

        int width = getWidth();
        int height = getHeight();

        int cameraX = model.getCameraX();
        int cameraY = model.getCameraY();

        // Start positions shift with camera so the grid appears endless while panning
        int startX = Math.floorMod(-cameraX, GRID_SIZE);
        int startY = Math.floorMod(-cameraY, GRID_SIZE);

        for (int x = startX; x < width; x += GRID_SIZE) {
            g2.drawLine(x, 0, x, height);
        }

        for (int y = startY; y < height; y += GRID_SIZE) {
            g2.drawLine(0, y, width, y);
        }
    }

    /**
     * Draws to the screen
     * @param g the <code>Graphics</code> object to protect
     */
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Draw grid first so it stays behind all objects
        drawGrid(g2);

        // Then draw world objects using camera translation
        g2.translate(-model.getCameraX(), -model.getCameraY());
        g2.setColor(Color.BLUE);
        g2.fillOval(0, 0, 100, 100);

        g2.dispose();
    }
}