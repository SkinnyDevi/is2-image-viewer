package software.ulpgc.imageviewer.model;

public record Position(int x, int y) {
    public static final Position ZERO = new Position(0, 0);
}