package tabletopsync.canvas.model;

import java.util.ArrayList;
import java.util.List;

public class CanvasModel {
    private int cameraX = 0;
    private int cameraY = 0;

    private final List<CameraListener> listeners = new ArrayList<>();

    public int getCameraX() { return cameraX; }
    public int getCameraY() { return cameraY; }

    public void moveCamera(int dx, int dy) {
        cameraX -= dx;
        cameraY -= dy;
        notifyListeners();
    }

    public void setCamera(int x, int y) {
        cameraX = x;
        cameraY = y;
        notifyListeners();
    }

    private void notifyListeners() {
        for (CameraListener l : listeners) {
            l.cameraMoved(cameraX, cameraY);
        }
    }

    // Functional interface
    public interface CameraListener {
        void cameraMoved(int x, int y);
    }

    public void addListener(CameraListener listener) {
        listeners.add(listener);
    }

}