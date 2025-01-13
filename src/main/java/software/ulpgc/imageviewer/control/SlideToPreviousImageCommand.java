package software.ulpgc.imageviewer.control;

public class SlideToPreviousImageCommand implements Command {
    private final ImagePresenter presenter;

    public SlideToPreviousImageCommand(ImagePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        presenter.presentPrevious();
    }
}