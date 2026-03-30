package tabletopsync.canvas.model;

public class CanvasModel {
    // handles camera functionality
    private int cameraX = 0;
    private int cameraY = 0;

    public int getCameraX() {
        return cameraX;
    }

    public int getCameraY() {
        return cameraY;
    }

    public void moveCamera(int dx, int dy) {
        cameraX -= dx;
        cameraY -= dy;
    }
}