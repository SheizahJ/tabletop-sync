import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Canvas extends JPanel {
    private Point lastMousePoint;

    private boolean canDrag = false;
    private int cameraX = 0;
    private int cameraY = 0;

    public Canvas() {
        setBackground(Color.WHITE);

        // logic for moving around the canvas
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
                    int distanceX = e.getX() - lastMousePoint.x;
                    int distanceY = e.getY() - lastMousePoint.y;

                    cameraX -= distanceX;
                    cameraY -= distanceY;

                    lastMousePoint = e.getPoint();
                    repaint();
                }
            }
        };

        this.addMouseListener(canvasControl);
        this.addMouseMotionListener(canvasControl);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.translate(-cameraX, -cameraY);

        g2.setColor(Color.BLUE);
        g2.fillOval(0,0,100,100);
    }
}
