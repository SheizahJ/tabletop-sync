package tabletopsync.canvas.model;

import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.util.List;

public class CanvasModel {
    // camera variables
    public static final int GRID_SIZE = 50;

    private boolean cameraCanMove = false;
    private int cameraX = 0;
    private int cameraY = 0;
    private double zoom = 1.0;
    private final double MIN_ZOOM = 0.25;
    private final double MAX_ZOOM = 3.0;
    // map background
    private BufferedImage mapImage;
    private int mapX = 0;
    private int mapY = 0;
    private int mapWidth = 0;
    private int mapHeight = 0;
    private boolean mapIsResizing = false;


    public BufferedImage getMapImage() {
        return mapImage;
    }

    //---------------------QUARANTINE------------------------//
    public String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
    //------------------------------------------------------//

    public void setMapImage(BufferedImage image) {
        this.mapImage = image;

        if (image != null) {
            this.mapWidth = image.getWidth();
            this.mapHeight = image.getHeight();
        }

        notifyListeners();
    }
    // Background getters and setter
    public int getMapX() {
        return mapX;
    }
    public int getMapY() {
        return mapY;
    }
    public void setMapPosition(int x, int y) {
        this.mapX = x;
        this.mapY = y;
        notifyListeners();
    }
    public int getMapWidth() {
        return mapWidth;
    }
    public int getMapHeight() {
        return mapHeight;
    }
    public void setMapSize(int width, int height) {
        this.mapWidth = width;
        this.mapHeight = height;
        notifyListeners();
    }
    public boolean isMapResizing() {
        return mapIsResizing;
    }
    public void setMapResizing(boolean resizing) {
        this.mapIsResizing = resizing;
    }

    // sprite variables
    private final List<Sprite> sprites = new ArrayList<>();
    private Sprite selectedSprite;
    private boolean isSelecting = false;
    private boolean spriteSnapsToGrid = true;

    // CAMERA METHODS

    public boolean canCameraMove() {
        return cameraCanMove;
    }

    public void setCameraCanMove(boolean moveable) {
        this.cameraCanMove = moveable;
    }

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

    public double getZoom() {
        return zoom;
    }

    public void zoom(double amount) {
        zoom += amount;

        if (zoom < MIN_ZOOM) zoom = MIN_ZOOM;
        if (zoom > MAX_ZOOM) zoom = MAX_ZOOM;

        notifyListeners();
    }

    // SPRITE METHODS
    public List<Sprite> getSpritesOnCanvas() {
        return sprites;
    }

    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
    }

    public void removeSprite(Sprite sprite) {
        sprites.remove(sprite);
        selectedSprite = null;
    }

    public Sprite getSelectedSprite() {
        return selectedSprite;
    }

    public void selectSprite(Sprite sprite) {
        this.selectedSprite = sprite;
        setIsSelecting(true);
    }

    public void unselectSprites() {
        this.selectedSprite = null;
        setIsSelecting(false);
    }

    public boolean isSelecting() {
        System.out.println(isSelecting);
        System.out.println(this.selectedSprite);
        return isSelecting && this.selectedSprite != null;
    }

    public void setIsSelecting(boolean selecting) {
        this.isSelecting = selecting;
    }

    public boolean snapsToGrid() {
        return spriteSnapsToGrid;
    }

    public void setSnapToGridState(boolean enabled) {
        this.spriteSnapsToGrid = enabled;
    }

    // Functional interface
    public interface CameraListener {
        void cameraMoved(int x, int y);
    }

    public void addListener(CameraListener listener) {
        listeners.add(listener);
    }

    // getter for sprite with uuid
    public Sprite getSpriteByUuid(String uuid) {
        for (Sprite sprite : sprites) {
            if (sprite.getUuid().equals(uuid)) {
                return sprite;
            }
        }
        return null;
    }

}