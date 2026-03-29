
// current package
package controller;

// package import
import model.GuiModel;
import view.GuiView;

// importing libraries req
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GuiController extends JPanel {
    private Point lastMousePoint;
    private boolean canDrag = false;

    private GuiModel model;

    public GuiController(GuiModel model) {
        this.model = model;
        setBackground(Color.WHITE);

        MouseAdapter canvasControl = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON2) {
                    lastMousePoint = e.getPoint();
                    canDrag = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                canDrag = false;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (canDrag) {
                    int dx = e.getX() - lastMousePoint.x;
                    int dy = e.getY() - lastMousePoint.y;

                    model.moveCamera(dx, dy); // ✅ move logic to model

                    lastMousePoint = e.getPoint();
                    repaint();
                }
            }
        };

        addMouseListener(canvasControl);
        addMouseMotionListener(canvasControl);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // ✅ read from model
        g2.translate(-model.getCameraX(), -model.getCameraY());

        g2.setColor(Color.BLUE);
        g2.fillOval(0, 0, 100, 100);
    }
}