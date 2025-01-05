package software.ulpgc.imageviewer.view;

import software.ulpgc.imageviewer.model.Image;

public interface ImageDisplay {
    void paint(Image image, int offset);
    int getWidth();

    void clear();
    void on(ShiftEvent shift);
    void on(ReleaseEvent released);

    interface ShiftEvent {
        ShiftEvent Null = offset -> {};
        void offsetBy(int offset);
    }

    interface ReleaseEvent {
        ReleaseEvent Null = offset -> {};
        void offsetBy(int offset);
    }
}