package software.ulpgc.imageviewer.apps.swing;

import software.ulpgc.imageviewer.adapters.ImageAdapter;
import software.ulpgc.imageviewer.view.ImageCanvas;
import software.ulpgc.imageviewer.view.ImageDisplay;
import software.ulpgc.imageviewer.model.Scale;
import software.ulpgc.imageviewer.model.Image;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.List;

public class SwingImageDisplay extends JPanel implements ImageDisplay {
    public static final Color PANEL_BACKGROUND_COLOR = new Color(1382685);
    private final float ZOOM_FACTOR_STEP = 0.5f;
	private final float MIN_ZOOM_FACTOR = 1.0f;
	private final List<ImagePaintEntry> imagePaintQueue = new ArrayList<>();
	private final Map<Integer, java.awt.Image> swingImageCache = new HashMap<>();
	private final ImageAdapter imageAdapter;
	private ShiftEvent shiftEvent = ShiftEvent.Null;
	private ReleaseEvent releaseEvent = ReleaseEvent.Null;
	private int initialPressPosition;
	private float zoomFactor = 1.0f;

	public SwingImageDisplay(ImageAdapter imageAdapter) {
		this.imageAdapter = imageAdapter;
		this.addMouseListener(createMouseListener());
		this.addMouseMotionListener(createMouseMotionListener());
        this.setBackground(PANEL_BACKGROUND_COLOR);
	}

	private MouseMotionListener createMouseMotionListener() {
		return new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				shiftEvent.offsetBy(e.getX() - initialPressPosition);
				zoomFactor = MIN_ZOOM_FACTOR;
			}

			@Override
			public void mouseMoved(MouseEvent e) {
			}
		};
	}

	private MouseListener createMouseListener() {
		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				initialPressPosition = e.getX();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				releaseEvent.offsetBy(e.getX() - initialPressPosition);
				zoomFactor = MIN_ZOOM_FACTOR;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		};
	}

	@Override
	public void paint(Image image, int offset) {
		imagePaintQueue.add(new ImagePaintEntry(image, offset));
		repaint();
	}

	@Override
	public void clear() {
		imagePaintQueue.clear();
	}

	@Override
	public void increaseZoomFactor() {
		this.zoomFactor = normalizedZoomFactor(zoomFactor + ZOOM_FACTOR_STEP);
	}

	@Override
	public void decreaseZoomFactor() {
		this.zoomFactor = normalizedZoomFactor(zoomFactor - ZOOM_FACTOR_STEP);
	}

	private float normalizedZoomFactor(float zoom) {
		return Math.max(MIN_ZOOM_FACTOR, zoom);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawPaintOrders(g);
	}

	private void drawPaintOrders(Graphics g) {
		imagePaintQueue.forEach(p -> drawImagePaint(g, p));
	}

	private void drawImagePaint(Graphics g, ImagePaintEntry paintEntry) {
		java.awt.Image image = adaptImage(paintEntry.image());
		ImageCanvas canvas = createImageCanvasFor(image);
		drawImageInCanvas(g, paintEntry, image, canvas);
	}

	private static void drawImageInCanvas(Graphics g, ImagePaintEntry paintEntry, java.awt.Image image,
			ImageCanvas canvas) {
		g.drawImage(
				image,
				canvas.position().x() + paintEntry.offset(),
				canvas.position().y(),
				canvas.scale().width(),
				canvas.scale().height(),
				null);
	}

	private ImageCanvas createImageCanvasFor(java.awt.Image image) {
		return ImageCanvas.ofScale(new Scale(getWidth(), getHeight()))
				.alignCenter(imageScaleWithZoom(image))
				.zoomBy(this.zoomFactor);
    }

	private Scale imageScaleWithZoom(java.awt.Image image) {
		return new Scale(zoomWidthBy(this.zoomFactor, image), zoomHeightBy(this.zoomFactor, image));
	}

	private int zoomWidthBy(float zoomFactor, java.awt.Image image) {
		return (int) (image.getWidth(null) * zoomFactor);
	}

	private int zoomHeightBy(float zoomFactor, java.awt.Image image) {
		return (int) (image.getHeight(null) * zoomFactor);
	}

	private java.awt.Image adaptImage(Image image) {
		return swingImageCache.computeIfAbsent(
				imageHashCodeWithZoom(image, this.zoomFactor),
				k -> createAdaptedImageFrom(image));
	}

	private static int imageHashCodeWithZoom(Image image, float zoom) {
		return image.hashCode() + String.valueOf(zoom).hashCode();
	}

	private java.awt.Image createAdaptedImageFrom(Image image) {
		return (java.awt.Image) imageAdapter.adapt(image);
	}

	@Override
	public void on(ShiftEvent shift) {
		this.shiftEvent = shift != null ? shift : ShiftEvent.Null;
	}

	@Override
	public void on(ReleaseEvent released) {
		this.releaseEvent = released != null ? released : ReleaseEvent.Null;
	}

	private record ImagePaintEntry(Image image, int offset) {
	}
}