package software.ulpgc.imageviewer.apps.swing;

import javax.swing.border.Border;
import java.awt.*;

public class SwingRoundedBorder implements Border {
    private final int radius;

    public SwingRoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component component, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius, radius, radius, radius);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}