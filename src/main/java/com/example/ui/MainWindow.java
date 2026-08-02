package com.example.ui;

import com.example.util.Resource;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MainWindow {

    // Size
    private static final Dimension WINDOW_SIZE = new Dimension(1920, 1080);
    private static final Dimension SEARCH_FIELD_SIZE = new Dimension(300, 30);
    private static final Dimension GRAPH_SIZE = new Dimension(800, 600);
    private static final Dimension ICON_BUTTON_SIZE = new Dimension(75, 75);

    // Display Text
    private static final String WINDOW_TITLE = "Merchant";

    // Theme
    private static final String LIGHT_THEME_PATH = "json/lightTheme.json";
    private static final String DARK_THEME_PATH = "json/darkTheme.json";
    private static ThemeValue[] lightTheme;
    private static ThemeValue[] darkTheme;

    public static ThemeValue[] getTheme(boolean dark) {
        return dark ? darkTheme : lightTheme;
    }

    public static void start() {
        try {
            darkTheme = Resource.get(DARK_THEME_PATH, ThemeValue[].class);
            lightTheme = Resource.get(LIGHT_THEME_PATH, ThemeValue[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            Settings.applyTheme();

            var frame = new JFrame(WINDOW_TITLE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            // Use native OS window decorations (minimize, maximize, close buttons)
            // JFrame defaults to decorated=true, which shows native window controls

            // Top panel with search
            var topPanel = new JPanel(new BorderLayout(), Config.DOUBLE_BUFFER);
            var searchField = new JTextField(Config.DEFAULT_INPUT_COLUMNS);
            searchField.setPreferredSize(SEARCH_FIELD_SIZE);
            searchField.setToolTipText("Search...");
            topPanel.add(searchField, BorderLayout.CENTER);

            // Settings button
            var settingsButton = new JButton(
                Resource.getIcon(Resource.Icon.SETTINGS)
            );
            settingsButton.setToolTipText("Settings");
            settingsButton.setPreferredSize(ICON_BUTTON_SIZE);
            settingsButton.addActionListener(event -> {
                new Settings(frame);
            });
            topPanel.add(settingsButton, BorderLayout.EAST);

            // Graph display area (placeholder)
            var graphPanel = new JPanel();
            graphPanel.setBorder(
                BorderFactory.createTitledBorder("Graph Display")
            );
            graphPanel.setPreferredSize(GRAPH_SIZE);
            var graphLabel = new JLabel(
                "Graph will be displayed here",
                JLabel.CENTER
            );
            graphPanel.add(graphLabel);

            frame.add(topPanel, BorderLayout.NORTH);
            frame.add(graphPanel, BorderLayout.CENTER);

            frame.setSize(WINDOW_SIZE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // needs to check if db exist and prompt new user if not
            new Login(frame);
        });
    }
}
