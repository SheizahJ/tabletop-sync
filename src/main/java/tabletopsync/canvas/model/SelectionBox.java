package tabletopsync.canvas.model;

import java.awt.*;

public class SelectionBox {
    private Sprite owner;
    private String selectedBy;
    private boolean visible = false;

    public SelectionBox(Sprite owner) {
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
