package view;

import controller.GuiController;

import javax.swing.*;

public class GuiView extends JFrame {

    public GuiView() {
        SwingUtilities.invokeLater(() -> {
            this.setTitle("TabletopSync");
            this.setSize(1000,700);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            this.add(new GuiController());
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        });
    }
}
