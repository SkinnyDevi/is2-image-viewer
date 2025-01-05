package software.ulpgc.imageviewer.model;

public interface Image {
    Image next();
    Image previous();
    byte[] content();

    enum Type {
        JPG, JPEG, PNG;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}