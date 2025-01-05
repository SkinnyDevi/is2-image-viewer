package software.ulpgc.imageviewer.apps.swing;

import software.ulpgc.imageviewer.adapters.ImageAdapter;
import software.ulpgc.imageviewer.view.ImageDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class SwingImageDisplay extends JPanel implements ImageDisplay {
    private final ImageAdapter imageDeserializer;
    private final List<Paint> paintOrder = new ArrayList<>();
    private ShiftEvent shiftEvent = ShiftEvent.Null;
    private ReleaseEvent releaseEvent = ReleaseEvent.Null;
    private int initialPressPosition;

    public SwingImageDisplay(ImageAdapter imageDeserializer) {
        this.imageDeserializer = imageDeserializer;
        this.addMouseListener(createMouseListener());
        this.addMouseMotionListener(createMouseMotionListener());
    }

    private MouseListener createMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                initialPressPosition = e.getX();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                releaseEvent.offsetBy(e.getX() - initialPressPosition);
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) { }
        };
    }

    private MouseMotionListener createMouseMotionListener() {
        return new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                shiftEvent.offsetBy(e.getX() - initialPressPosition);
            }

            @Override
            public void mouseMoved(MouseEvent e) {}
        };
    }

    @Override
    public void paint(software.ulpgc.imageviewer.model.Image image, int offset) {
        paintOrder.add(new Paint(image, offset));
        repaint();
    }

    @Override
    public void clear() {
        paintOrder.clear();
    }

    @Override
    public void paint(Graphics g) {
        for (Paint paint : paintOrder) {
            Image image = adaptImage(paint.image);
            g.drawImage(image, 0 + paint.offset(), 0, getWidth(), getHeight(), null);
        }
    }

    private Image adaptImage(software.ulpgc.imageviewer.model.Image image) {
        return (Image) imageDeserializer.adapt(image);
    }

    @Override
    public void on(ShiftEvent shift) {
        this.shiftEvent = shift != null ? shift : ShiftEvent.Null;
    }

    @Override
    public void on(ReleaseEvent released) {
        this.releaseEvent = released != null ? released : ReleaseEvent.Null;
    }

    private record Paint(software.ulpgc.imageviewer.model.Image image, int offset) { }
}