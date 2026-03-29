
package model;

import controller.GuiController;
import view.GuiView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiModel {
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