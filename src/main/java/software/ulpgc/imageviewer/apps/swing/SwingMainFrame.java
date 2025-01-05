package software.ulpgc.imageviewer.apps.swing;

import software.ulpgc.imageviewer.view.ImageDisplay;

import javax.swing.*;
import java.awt.*;

public class SwingMainFrame extends JFrame {
    private final ImageDisplay imageDisplay;

    public SwingMainFrame()  {
        this.setTitle("Image Viewer");
        this.setSize(800,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add((Component) (imageDisplay = createImageDisplay()));
    }

    private ImageDisplay createImageDisplay() {
        return new SwingImageDisplay(new SwingImageAdapter());
    }

    public ImageDisplay getImageDisplay() {
        return imageDisplay;
    }
}