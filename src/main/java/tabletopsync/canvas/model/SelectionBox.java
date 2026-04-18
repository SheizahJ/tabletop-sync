package tabletopsync.canvas.model;

import tabletopsync.login.LoginView;

import java.awt.*;
import java.io.IOException;

public class SelectionBox {
    private final Sprite owner;
    private String selectedBy;
    private boolean visible = false;
    private Color color;

    public SelectionBox(Sprite owner) throws IOException {
        this.owner = owner;
    }

    public String getSelectedBy() {
        return selectedBy;
    }

    public boolean isVisible() {
        return visible;
    }

    public Color getColor() {
        return color;
    }

    public void show(String username, Color color) {
        this.selectedBy = username;
        this.color = color;
        this.visible = true;
    }

    public void hide() {
        this.visible = false;
    }

    public Rectangle getBounds() {
        return new Rectangle(
                owner.getPosition().x,
                owner.getPosition().y,
                owner.getWidth(),
                owner.getHeight()
        );
    }
}
