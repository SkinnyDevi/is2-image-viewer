package software.ulpgc.imageviewer.io;

import software.ulpgc.imageviewer.model.Image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class FileImageLoader implements ImageLoader {
    private final List<File> imageFiles;

    public FileImageLoader(Path directoryPath) throws NotDirectoryException {
        throwIfPathIsNotDirectory(directoryPath);
        this.imageFiles = loadImageFilesFromDirectory(directoryPath);
    }

    private List<File> loadImageFilesFromDirectory(Path directoryPath) {
        try (Stream<Path> paths = Files.walk(directoryPath)) {
            return paths.filter(Files::isRegularFile).filter(this::imageFileFilter).map(Path::toFile).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean imageFileFilter(Path path) {
        return getSupportedImageExtensions().stream().anyMatch(ie -> path.toString().endsWith(ie));
    }
    
    private List<String> getSupportedImageExtensions() {
        return Arrays.stream(Image.Type.values()).map(Image.Type::toString).toList();
    }

    private void throwIfPathIsNotDirectory(Path directoryPath) throws NotDirectoryException {
        if (directoryPath.toFile().isDirectory()) return;
        throw new NotDirectoryException("Path '"+ directoryPath.toAbsolutePath() +"' is not a directory.");
    }

    @Override
    public Image load() {
        return imageAt(0);
    }

    private Image imageAt(int index) {
        return new Image() {
            @Override
            public Image next() {
                return imageAt(nextIndex());
            }

            private int nextIndex() {
                return index >= lastIndex() ? 0 : index + 1;
            }

            @Override
            public Image previous() {
                return imageAt(previousIndex());
            }

            private int previousIndex() {
                return index <= 0 ? lastIndex() : index - 1;
            }

            private int lastIndex() {
                return imageFiles.size() - 1;
            }

            @Override
            public byte[] content() {
                try {
                    return Files.readAllBytes(fromPath(this.file()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public int hashCode() {
                return this.file().hashCode();
            }

            private Path fromPath(File file) {
                return file.toPath();
            }

            private File file() {
                return imageFiles.get(index);
            }
        };
    }
}