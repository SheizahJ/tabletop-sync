package tabletopsync.canvas.view;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

/**
 * Basic container for canvas panel
 */
public class CanvasView extends JPanel {

    public CanvasView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
    }
}