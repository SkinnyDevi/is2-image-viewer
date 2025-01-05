package software.ulpgc.imageviewer.control;

import software.ulpgc.imageviewer.view.ImageDisplay;
import software.ulpgc.imageviewer.view.ImageDisplay.*;
import software.ulpgc.imageviewer.model.Image;

public class ImagePresenter {
    private final ImageDisplay display;
    private Image image;

    public ImagePresenter(ImageDisplay display) {
        this.display = display;
        this.display.on((ShiftEvent) this::shift);
        this.display.on((ReleaseEvent) this::release);
    }

    private void shift(int offset) {
        display.clear();
        display.paint(image, offset);
        if (offset > 0)
            display.paint(image.previous(), offset - display.getWidth());
        else
            display.paint(image.next(), display.getWidth() + offset);
    }

    private void release(int offset) {
        if (Math.abs(offset) >= display.getWidth() / 2)
            image = offset > 0 ? image.previous() : image.next();
        repaint();
    }

    public void present(Image image) {
        this.image = image;
        repaint();
    }

    private void repaint() {
        this.display.clear();
        this.display.paint(image, 0);
    }
}