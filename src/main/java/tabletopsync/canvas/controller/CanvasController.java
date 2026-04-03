package tabletopsync.canvas.controller;

import tabletopsync.canvas.model.CanvasModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Handles camera drag controls on canvas and redraws the canvas sprites
 */
public class CanvasController extends JPanel {
    private Point lastMousePoint;
    private boolean canDrag = false;

    private final CanvasModel model;
    private CanvasUpdateListener networkListener; // send updates to server

    public CanvasController(CanvasModel model) {
        this.model = model;
        setBackground(Color.WHITE);


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
        g2.setColor(Color.BLUE);
        g2.fillOval(0, 0, 100, 100);
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