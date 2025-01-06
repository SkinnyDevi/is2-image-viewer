package software.ulpgc.imageviewer.apps.swing;

import software.ulpgc.imageviewer.adapters.ImageAdapter;
import software.ulpgc.imageviewer.view.ImageCanvas;
import software.ulpgc.imageviewer.view.ImageDisplay;
import software.ulpgc.imageviewer.model.Scale;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwingImageDisplay extends JPanel implements ImageDisplay {
    private final ImageAdapter imageDeserializer;
    private final List<Paint> paintOrder = new ArrayList<>();
    private final Map<software.ulpgc.imageviewer.model.Image, Image> swingImageCache = new HashMap<>();
    private ShiftEvent shiftEvent = ShiftEvent.Null;
    private ReleaseEvent releaseEvent = ReleaseEvent.Null;
    private int initialPressPosition;

    public SwingImageDisplay(ImageAdapter imageDeserializer) {
        this.imageDeserializer = imageDeserializer;
        this.addMouseListener(createMouseListener());
        this.addMouseMotionListener(createMouseMotionListener());
        setDoubleBuffered(true);
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
        Rectangle bounds = getBounds();
        repaint(0, 0, bounds.width, bounds.height);
    }

    @Override
    public void clear() {
        paintOrder.clear();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Paint paint : paintOrder) {
            Image image = adaptImage(paint.image);
            ImageCanvas canvas = createImageCanvasFor(image);
            drawImageInCanvas(g, paint, image, canvas);
        }
    }

    private ImageCanvas createImageCanvasFor(Image image) {
        return ImageCanvas.ofScale(new Scale(getWidth(), getHeight()))
                .alignCenter(new Scale(image.getWidth(null), image.getHeight(null)));
    }

    private static void drawImageInCanvas(Graphics g, Paint paint, Image image, ImageCanvas canvas) {
        g.drawImage(
                image,
                canvas.position().x() + paint.offset(),
                canvas.position().y(),
                canvas.scale().width(),
                canvas.scale().height(),
                null
        );
    }

    private Image adaptImage(software.ulpgc.imageviewer.model.Image image) {
        return swingImageCache.computeIfAbsent(image, img -> (Image) imageDeserializer.adapt(img));
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