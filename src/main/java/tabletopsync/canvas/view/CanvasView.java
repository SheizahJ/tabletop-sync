package tabletopsync.canvas.view;

import tabletopsync.canvas.controller.CanvasController;
import tabletopsync.canvas.model.CanvasModel;

import javax.swing.*;
import java.awt.*;

public class CanvasView extends JPanel {

    public CanvasView() {
        setLayout(new BorderLayout());
        SwingUtilities.invokeLater(() -> {
            CanvasModel model = new CanvasModel();
            this.add(new CanvasController(model));
            this.setVisible(true);
        });
    }
}
