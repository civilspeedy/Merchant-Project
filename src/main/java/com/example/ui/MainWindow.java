package com.example.ui;

import com.example.ThemeValue;
import com.example.util.Resource;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainWindow {

    private static final Dimension WINDOW_SIZE = new Dimension(1920, 1080);
    private static final Dimension MODAL_SIZE = new Dimension(854, 480);
    private static final String SWITCH_TO_DARK = "Switch to Dark Theme";
    private static final String SWITCH_TO_LIGHT = "Switch to Light Theme";
    private static final Dimension SEARCH_FIELD_SIZE = new Dimension(300, 30);
    private static final Dimension GRAPH_SIZE = new Dimension(800, 600);
    private static final String WINDOW_TITLE = "Merchant";
    private static final String SETTINGS_TITLE = "Settings";
    private static final String LOGIN_TITLE = "Login";
    private static boolean darkMode = false;
    private static final String LIGHT_THEME_PATH = "json/lightTheme.json";
    private static final String DARK_THEME_PATH = "json/darkTheme.json";
    private static ThemeValue[] lightTheme;
    private static ThemeValue[] darkTheme;

    public static void start() {
        try {
            darkTheme = Resource.get(DARK_THEME_PATH, ThemeValue[].class);
            lightTheme = Resource.get(LIGHT_THEME_PATH, ThemeValue[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            applyTheme();

            var frame = new JFrame(WINDOW_TITLE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            // Use native OS window decorations (minimize, maximize, close buttons)
            // JFrame defaults to decorated=true, which shows native window controls

            // Top panel with search
            var topPanel = new JPanel(new BorderLayout());
            var searchField = new JTextField();
            searchField.setPreferredSize(SEARCH_FIELD_SIZE);
            searchField.setToolTipText("Search...");
            topPanel.add(searchField, BorderLayout.CENTER);

            // Settings button
            var settingsButton = new JButton(
                Resource.getIcon(Resource.Icon.SETTINGS)
            );
            settingsButton.setToolTipText("Settings");
            settingsButton.addActionListener(event -> {
                showSettingsDialog(frame);
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
        });
    }

    private static void applyTheme() {
        try {
            UIManager.setLookAndFeel(
                "javax.swing.plaf.nimbus.NimbusLookAndFeel"
            );

            var activeTheme = darkMode ? darkTheme : lightTheme;

            for (var theme : activeTheme) {
                var key = theme.name().replace('_', '.');
                UIManager.put(key, new Color(theme.r(), theme.g(), theme.b()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addToPanel(JPanel panel, Component[] components) {
        for (var component : components) {
            panel.add(component);
        }
    }

    private static JDialog newModal(String title, JFrame parent) {
        var dialog = new JDialog(parent, title, true);
        dialog.setSize(MODAL_SIZE);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());
        return dialog;
    }

    private static void showLoginDialog(JFrame parent) {
        var loginDialog = newModal(LOGIN_TITLE, parent);

        var loginPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        var passwordTextBox = new JPasswordField();
    }

    private static void showSettingsDialog(JFrame parent) {
        var settingsDialog = newModal(SETTINGS_TITLE, parent);

        // Create settings panel
        var settingsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        settingsPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Theme toggle
        var themeToggle = new JButton(
            darkMode ? SWITCH_TO_LIGHT : SWITCH_TO_DARK
        );
        themeToggle.addActionListener(e -> {
            darkMode = !darkMode;
            themeToggle.setText(darkMode ? SWITCH_TO_LIGHT : SWITCH_TO_DARK);
            applyTheme();
            SwingUtilities.updateComponentTreeUI(parent);
            SwingUtilities.updateComponentTreeUI(settingsDialog);
        });

        // API Key input
        var apiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        apiPanel.add(new JLabel("Massive API Key:"));
        var apiField = new JPasswordField(20);
        apiPanel.add(apiField);

        var saveApiButton = new JButton("Save API Key");
        saveApiButton.addActionListener(e -> {
            var apiKey = new String(apiField.getPassword()); // should not be stored as plaintext
        });

        var components = new Component[] {
            themeToggle,
            apiPanel,
            saveApiButton,
        };

        addToPanel(settingsPanel, components);

        settingsDialog.add(settingsPanel, BorderLayout.CENTER);
        settingsDialog.setVisible(true);
    }
}
