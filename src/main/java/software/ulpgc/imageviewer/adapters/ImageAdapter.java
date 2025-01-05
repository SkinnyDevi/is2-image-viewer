package software.ulpgc.imageviewer.adapters;

import software.ulpgc.imageviewer.model.Image;

public interface ImageAdapter {
    Object adapt(Image content);
}