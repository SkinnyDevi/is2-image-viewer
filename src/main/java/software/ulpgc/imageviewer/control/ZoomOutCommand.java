package software.ulpgc.imageviewer.control;

public class ZoomOutCommand implements Command {
    private final ImagePresenter presenter;

    public ZoomOutCommand(ImagePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        presenter.zoomOut();
    }
}