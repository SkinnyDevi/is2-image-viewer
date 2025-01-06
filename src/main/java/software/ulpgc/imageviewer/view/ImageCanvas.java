package software.ulpgc.imageviewer.view;

import software.ulpgc.imageviewer.model.Position;
import software.ulpgc.imageviewer.model.Scale;

public record ImageCanvas(Position position, Scale scale) {
    public static ImageCanvas ofScale(Scale scale) {
        return new ImageCanvas(new Position(0, 0), scale);
    }

    public ImageCanvas alignCenter(Scale imageScale) {
        return isLandscape(imageScale) ? fitToLandscapeCanvas(imageScale) : fitToPortraitCanvas(imageScale);
    }

    private ImageCanvas fitToPortraitCanvas(Scale imageScale) {
        int newWidth = fitToWidth(imageScale);
        Scale newScale = new Scale(newWidth, scale.height());
        return new ImageCanvas(new Position(centeredXFor(newWidth), 0), newScale);
    }

    private int centeredXFor(int newWidth) {
        return (this.scale.width() - newWidth) / 2;
    }

    private int fitToWidth(Scale imageScale) {
        return (int) (scale.height() * aspectRatio(imageScale.width(), imageScale.height()));
    }

    private ImageCanvas fitToLandscapeCanvas(Scale imageScale) {
        int newHeight = fitToHeight(imageScale);
        Scale newScale = new Scale(scale.width(), newHeight);
        return new ImageCanvas(new Position(0, centeredYFor(newHeight)), newScale);
    }

    private int centeredYFor(int newHeight) {
        return (this.scale.height() - newHeight) / 2;
    }

    private int fitToHeight(Scale imageScale) {
        return (int) (scale.width() / aspectRatio(imageScale.width(), imageScale.height()));
    }

    private boolean isLandscape(Scale scale) {
        return aspectRatio(scale.width(), scale.height()) > canvasAspectRatio();
    }

    private double canvasAspectRatio() {
        return aspectRatio(this.scale);
    }

    private double aspectRatio(Scale scale) {
        return this.aspectRatio(scale.width(), scale.height());
    }

    private double aspectRatio(int width, int height) {
        return (double) width / height;
    }
}