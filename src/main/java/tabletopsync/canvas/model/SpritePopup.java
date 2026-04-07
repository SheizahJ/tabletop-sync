package tabletopsync.canvas.model;

import tabletopsync.canvas.controller.CanvasController;
import javax.swing.*;
import java.awt.*;

public class SpritePopup extends JPopupMenu {
    private final JMenuItem lockSprite;
    private final CanvasModel model;

    public SpritePopup(CanvasController canvas, CanvasModel model) {
        this.model = model;

        // lock/unlock sprite button
        this.lockSprite = new JMenuItem("Lock");
        lockSprite.addActionListener(e -> {
            Sprite selectedSprite = model.getSelectedSprite();
            if (selectedSprite != null && selectedSprite.isSelected()) {
                selectedSprite.setLockedState(!selectedSprite.isLocked());
                canvas.repaint();
            }
        });

        // delete sprite button
        JMenuItem deleteSprite = new JMenuItem("Delete");
        deleteSprite.addActionListener(e -> {
            Sprite selectedSprite = model.getSelectedSprite();
            if (selectedSprite != null && selectedSprite.isSelected()) {
                model.removeSprite(selectedSprite);
                canvas.repaint();
            }
        });

        this.add(deleteSprite);
        this.add(lockSprite);

        this.setVisible(false);
    }

    @Override
    public void show(Component invoker, int x, int y) {
        super.show(invoker, x, y);

        this.setVisible(true);
        this.lockSprite.setText(model.getSelectedSprite().isLocked() ? "Unlock" : "Lock");
    }
}
