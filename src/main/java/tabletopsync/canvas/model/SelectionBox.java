package tabletopsync.canvas.model;

import tabletopsync.login.LoginView;

import java.awt.*;
import java.io.IOException;

public class SelectionBox {
    private final Sprite owner;
    private String selectedBy;
    private boolean visible = false;

    public SelectionBox(Sprite owner) throws IOException {
        this.owner = owner;
    }

    public String getSelectedBy() {
        return selectedBy;
    }

    public boolean isVisible() {
        return visible;
    }

    public void show(String username) {
        this.selectedBy = username;
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
