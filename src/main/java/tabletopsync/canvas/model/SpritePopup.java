package tabletopsync.canvas.model;

import tabletopsync.canvas.controller.CanvasController;

import javax.swing.*;

import java.awt.*;

import static tabletopsync.canvas.controller.CanvasController.selectedSprite;

public class SpritePopup extends JPopupMenu {
    public SpritePopup(CanvasController canvas) {
        // delete sprite button
        JMenuItem deleteSprite = new JMenuItem("Delete");
        deleteSprite.addActionListener(e -> {
            if (selectedSprite != null && selectedSprite.isSelected()) {
                selectedSprite.delete();
                canvas.repaint();
            }
        });
        this.add(deleteSprite);

        // lock/unlock sprite button
        JMenuItem lockSprite = new JMenuItem("Lock/Unlock");
        lockSprite.addActionListener(e -> {
            if (selectedSprite != null && selectedSprite.isSelected()) {
                selectedSprite.setLockedState(!selectedSprite.isLocked());
                canvas.repaint();
            }
        });
        this.add(lockSprite);

        this.setVisible(false);
    }
}
