package software.ulpgc.imageviewer.control;

public class SlideToNextImageCommand implements Command {
    private final ImagePresenter presenter;

    public SlideToNextImageCommand(ImagePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        presenter.presentNext();
    }
}