package tabletopsync.canvas.model;

import tabletopsync.canvas.controller.CanvasController;

import javax.swing.*;

import java.awt.*;

import static tabletopsync.canvas.controller.CanvasController.selectedSprite;

public class SpritePopup extends JPopupMenu {
    private final JMenuItem lockSprite;

    public SpritePopup(CanvasController canvas) {
        // lock/unlock sprite button
        this.lockSprite = new JMenuItem("Lock");
        lockSprite.addActionListener(e -> {
            if (selectedSprite != null && selectedSprite.isSelected()) {
                selectedSprite.setLockedState(!selectedSprite.isLocked());
                canvas.repaint();
            }
        });

        // delete sprite button
        JMenuItem deleteSprite = new JMenuItem("Delete");
        deleteSprite.addActionListener(e -> {
            if (selectedSprite != null && selectedSprite.isSelected()) {
                selectedSprite.delete();
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
        this.lockSprite.setText(selectedSprite.isLocked() ? "Unlock" : "Lock");
    }
}
