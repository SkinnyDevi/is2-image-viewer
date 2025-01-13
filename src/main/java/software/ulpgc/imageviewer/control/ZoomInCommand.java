package software.ulpgc.imageviewer.control;

public class ZoomInCommand implements Command {
    private final ImagePresenter presenter;

    public ZoomInCommand(ImagePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        presenter.zoomIn();
    }
}