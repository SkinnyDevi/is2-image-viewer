package software.ulpgc.imageviewer.apps.swing;

import software.ulpgc.imageviewer.io.FileImageLoader;
import software.ulpgc.imageviewer.model.Image;
import software.ulpgc.imageviewer.control.ImagePresenter;

import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static final String RESOURCES_APP_PATH = "src/main/resources";

    public static void main(String[] args) throws NotDirectoryException {
        SwingMainFrame frame = new SwingMainFrame();
        ImagePresenter presenter = new ImagePresenter(frame.getImageDisplay());
        presenter.present(initialImage());
        frame.setVisible(true);
    }

    private static Image initialImage() throws NotDirectoryException {
        return new FileImageLoader(getResourcesPath()).load();
    }

    private static Path getResourcesPath() {
        return Paths.get(RESOURCES_APP_PATH);
    }
}