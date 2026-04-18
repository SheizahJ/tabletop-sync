package tabletopsync.canvas.model;

import tabletopsync.canvas.controller.CanvasController;

import javax.swing.*;
import java.awt.*;

public class SpritePopup extends JPopupMenu {
    private final JMenuItem lockSprite;
    private final CanvasController canvasController;
    private final CanvasModel canvasModel;

    public SpritePopup(CanvasController canvasController, CanvasModel canvasModel) {
        this.canvasController = canvasController;
        this.canvasModel = canvasModel;

        // lock/unlock sprite button
        this.lockSprite = new JMenuItem("Lock");
        lockSprite.addActionListener(e -> {
            Sprite selectedSprite = canvasModel.getSelectedSprite();
            if (selectedSprite != null && selectedSprite.isSelected()) {
                selectedSprite.setLockedState(!selectedSprite.isLocked());
                canvasController.repaint();
            }
        });

        // delete sprite button
        JMenuItem deleteSprite = new JMenuItem("Delete");
        deleteSprite.addActionListener(e -> {
            Sprite selectedSprite = canvasModel.getSelectedSprite();
            if (selectedSprite != null && selectedSprite.isSelected()) {
                canvasModel.removeSprite(selectedSprite);
                this.canvasController.networkListener.onSpriteRemoved(selectedSprite.getUuid());
                canvasController.repaint();
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
        this.lockSprite.setText(canvasModel.getSelectedSprite().isLocked() ? "Unlock" : "Lock");
    }
}
