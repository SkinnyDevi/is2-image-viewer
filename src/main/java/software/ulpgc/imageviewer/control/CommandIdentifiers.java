package software.ulpgc.imageviewer.control;

public enum CommandIdentifiers {
    PREVIOUS_COMMAND_ID("previous"),
    NEXT_COMMAND_ID("next"),
    ZOOM_IN_COMMAND_ID("zoom_in"),
    ZOOM_OUT_COMMAND_ID("zoom_out");

    private final String identifier;

    CommandIdentifiers(String identifier) {
        this.identifier = identifier;
    }

    public String get() {
        return identifier;
    }
}