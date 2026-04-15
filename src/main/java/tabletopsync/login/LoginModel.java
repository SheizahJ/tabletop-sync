package tabletopsync.login;

import javax.swing.*;
import java.awt.*;

public class LoginModel {
    private String username;

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public static class ColourSwatch extends JPanel {
        private Color colour;
        private String label;

        public ColourSwatch(Color colour) {
            this.colour = colour;
        }


        public Color getColour() { return colour; }
        public void setColour(Color colour) { this.colour = colour; }

        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }

        @Override public void paintComponent(Graphics g) {
            super.paintComponent(g);
            // rectangle
            g.setColor(colour);
            g.fillRect(0, 0, getWidth(), getHeight());

            // border
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, getWidth(), getHeight());

            // label for swatch
            if (label != null) {
                // to switch between dark and light text
                int brightness = (colour.getRed() + colour.getGreen() + colour.getBlue()) / 3;

                int brightnessNormal = 128;
                int brightnessHigh = 86;

                g.setColor(brightness < brightnessHigh ? Color.WHITE : Color.BLACK);

                FontMetrics fm = g.getFontMetrics();
                // maths to center the text
                int textWidth = fm.stringWidth(label);
                int textHeight = fm.getAscent();
                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() + textHeight) / 2;

                g.drawString(label, x, y);
            }
        }

        @Override public Dimension getPreferredSize() {
            return new Dimension(50, 100);
        }
    }
}
