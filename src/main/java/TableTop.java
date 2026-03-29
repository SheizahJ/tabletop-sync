import javax.swing.*;

public class TableTop extends JFrame {
    static void main(String[] args) {
        TableTop tableTop = new TableTop();
    }

    public TableTop() {
        SwingUtilities.invokeLater(() -> {
            this.setTitle("Tabletop Sync");
            this.setSize(1000,700);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            this.add(new Canvas());
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        });
    }
}
