package tabletopsync.canvas.model;

import java.util.ArrayList;
import java.util.List;

/**
 * CanvasModel:
 * Holds camera state and notifies listeners on changes
 */
public class CanvasModel {

    private int cameraX = 0;
    private int cameraY = 0;
    private final List<CameraListener> listeners = new ArrayList<>();

    public int getCameraX() { return cameraX; }
    public int getCameraY() { return cameraY; }

    /**
     * Move camera by delta values and notify listeners
     */
    public void moveCamera(int dx, int dy) {
        cameraX -= dx;
        cameraY -= dy;
        notifyListeners();
    }

    /**
     * Set camera to exact coordinates
     */
    public void setCamera(int x, int y) {
        cameraX = x;
        cameraY = y;
        notifyListeners();
    }

    public void addListener(CameraListener listener) { listeners.add(listener); }

    private void notifyListeners() {
        for (CameraListener l : listeners) l.cameraMoved(cameraX, cameraY);
    }

    /**
     * Functional interface for camera movement updates
     */
    public interface CameraListener {
        void cameraMoved(int x, int y);
    }
}