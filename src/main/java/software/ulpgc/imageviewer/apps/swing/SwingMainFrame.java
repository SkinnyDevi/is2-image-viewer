package software.ulpgc.imageviewer.apps.swing;

import software.ulpgc.imageviewer.control.Command;
import software.ulpgc.imageviewer.control.CommandIdentifiers;
import software.ulpgc.imageviewer.view.ImageDisplay;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class SwingMainFrame extends JFrame {
    public static final Color PANEL_BACKGROUND_COLOR = new Color(1382685);
    public static final Color BUTTON_BG_COLOR = new Color(2172201);
    public static final Color BACKGROUND_BUTTON_HOVER_COLOR = new Color(3226443);
    private final Map<String, Command> commands = new HashMap<>();
    private ImageDisplay imageDisplay;

    public SwingMainFrame()  {
        this.setTitle("Image Viewer");
        this.setSize(800,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setBackground(PANEL_BACKGROUND_COLOR);
        this.addContainers();
    }

    private void addContainers() {
        this.add(BorderLayout.CENTER, (Component) (imageDisplay = createImageDisplay()));
        this.add(BorderLayout.SOUTH, createHelperButtonsContainer());
    }

    private Component createHelperButtonsContainer() {
        JPanel jPanel = new JPanel();
        jPanel.setBackground(PANEL_BACKGROUND_COLOR);
        jPanel.setLayout(new BorderLayout());
        jPanel.add(BorderLayout.CENTER, createImageZoomHelperButtonsContainer());
        jPanel.add(BorderLayout.WEST, createPreviousButton());
        jPanel.add(BorderLayout.EAST, createNextButton());
        return jPanel;
    }

    private Component createImageZoomHelperButtonsContainer() {
        JPanel jPanel = new JPanel();
        jPanel.setBackground(PANEL_BACKGROUND_COLOR);
        jPanel.add(createZoomOutButton());
        jPanel.add(createZoomInButton());
        return jPanel;
    }

    private Component createZoomOutButton() {
        JButton jButton = new JButton("Zoom Out");
        configureButton(jButton, CommandIdentifiers.ZOOM_OUT_COMMAND_ID);
        return jButton;
    }

    private Component createZoomInButton() {
        JButton jButton = new JButton("Zoom In");
        configureButton(jButton, CommandIdentifiers.ZOOM_IN_COMMAND_ID);
        return jButton;
    }

    private Component createNextButton() {
        JButton jButton = new JButton("Next");
        configureButton(jButton, CommandIdentifiers.NEXT_COMMAND_ID);
        return jButton;
    }

    private Component createPreviousButton() {
        JButton jButton = new JButton("Previous");
        configureButton(jButton, CommandIdentifiers.PREVIOUS_COMMAND_ID);
        return jButton;
    }

    private void configureButton(JButton jButton, CommandIdentifiers identifier) {
        styleButton(jButton);
        addActionListenerTo(jButton, identifier);
    }

    private void addActionListenerTo(JButton jButton, CommandIdentifiers identifier) {
        jButton.addActionListener(__ -> executeCommandFor(identifier));
        jButton.addMouseListener(createHoverStyleMouseAdapter(jButton));
    }

    private MouseAdapter createHoverStyleMouseAdapter(JButton button) {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BACKGROUND_BUTTON_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_BG_COLOR);
            }
        };
    }

    private void executeCommandFor(CommandIdentifiers identifier) {
        commands.get(identifier.get()).execute();
    }

    private void styleButton(JButton jButton) {
        jButton.setOpaque(true);
        jButton.setPreferredSize(new Dimension(140, 30));
        jButton.setFont(new Font("Arial", Font.BOLD, 14));
        jButton.setForeground(Color.WHITE);
        jButton.setBackground(BUTTON_BG_COLOR);
        jButton.setFocusPainted(false);
        jButton.setBorder(createRoundedBorder());
    }

    private static CompoundBorder createRoundedBorder() {
        return BorderFactory.createCompoundBorder(
                new SwingRoundedBorder(20),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        );
    }

    public SwingMainFrame put(CommandIdentifiers identifier, Command command) {
        this.commands.put(identifier.get(), command);
        return this;
    }

    private ImageDisplay createImageDisplay() {
        return new SwingImageDisplay(new SwingImageAdapter());
    }

    public ImageDisplay getImageDisplay() {
        return imageDisplay;
    }
}