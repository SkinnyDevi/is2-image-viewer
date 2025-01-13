package software.ulpgc.imageviewer.apps.swing;

import software.ulpgc.imageviewer.control.*;
import software.ulpgc.imageviewer.io.FileImageLoader;
import software.ulpgc.imageviewer.model.Image;

import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static final String RESOURCES_APP_PATH = "src/main/resources";

    public static void main(String[] args) throws NotDirectoryException {
        SwingMainFrame mainFrame = new SwingMainFrame();
        ImagePresenter presenter = new ImagePresenter(mainFrame.getImageDisplay());
        presenter.present(initialImage());
        attachCommandsTo(mainFrame, presenter);

        mainFrame.setVisible(true);
    }

    private static void attachCommandsTo(SwingMainFrame frame, ImagePresenter presenter) {
        frame.put(CommandIdentifiers.PREVIOUS_COMMAND_ID, createPreviousCommand(presenter))
                .put(CommandIdentifiers.NEXT_COMMAND_ID, createNextCommand(presenter))
                .put(CommandIdentifiers.ZOOM_IN_COMMAND_ID, createZoomInCommand(presenter))
                .put(CommandIdentifiers.ZOOM_OUT_COMMAND_ID, createZoomOutCommand(presenter));
    }

    private static Command createZoomOutCommand(ImagePresenter presenter) {
        return new ZoomOutCommand(presenter);
    }

    private static Command createZoomInCommand(ImagePresenter presenter) {
        return new ZoomInCommand(presenter);
    }

    private static Command createNextCommand(ImagePresenter presenter) {
        return new SlideToNextImageCommand(presenter);
    }

    private static Command createPreviousCommand(ImagePresenter presenter) {
        return new SlideToPreviousImageCommand(presenter);
    }

    private static Image initialImage() throws NotDirectoryException {
        return new FileImageLoader(getResourcesPath()).load();
    }

    private static Path getResourcesPath() {
        return Paths.get(RESOURCES_APP_PATH);
    }
}