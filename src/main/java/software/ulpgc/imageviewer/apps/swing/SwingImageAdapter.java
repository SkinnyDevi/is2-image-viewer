package software.ulpgc.imageviewer.apps.swing;

import software.ulpgc.imageviewer.adapters.ImageAdapter;
import software.ulpgc.imageviewer.model.Image;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SwingImageAdapter implements ImageAdapter {
    @Override
    public Object adapt(Image image) {
        try (InputStream is = new ByteArrayInputStream(image.content())){
          return ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}