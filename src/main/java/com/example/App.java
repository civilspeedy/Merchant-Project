package com.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class App {

    private static String dbUrl =
        "jdbc:h2:file:./data/productionDatabase;CIPHER=AES";
    private static final String WINDOW_TITLE = "Merchant";
    private static final int WINDOW_WIDTH = 1920;
    private static final int WINDOW_HEIGHT = 1080;

    private static boolean darkMode = false;
    private static String apiKey = "";

    // Color constants for theme management
    // Dark mode colors
    private static final Color DARK_CONTROL = new Color(60, 63, 65);
    private static final Color DARK_TEXT = new Color(214, 217, 223);
    private static final Color DARK_BUTTON_BG = new Color(70, 73, 75);
    private static final Color DARK_BUTTON_FG = new Color(230, 230, 230);
    private static final Color DARK_BUTTON_FOCUS = new Color(100, 149, 237);
    private static final Color DARK_TEXTFIELD_BG = new Color(50, 53, 55);
    private static final Color DARK_TEXTFIELD_FG = new Color(220, 220, 220);
    private static final Color DARK_BORDER = new Color(100, 100, 100);

    // Light mode colors
    private static final Color LIGHT_CONTROL = new Color(240, 240, 240);
    private static final Color LIGHT_TEXT = new Color(0, 0, 0);
    private static final Color LIGHT_BUTTON_BG = new Color(230, 230, 230);
    private static final Color LIGHT_BUTTON_FG = new Color(30, 30, 30);
    private static final Color LIGHT_BUTTON_FOCUS = new Color(0, 51, 153);
    private static final Color LIGHT_TEXTFIELD_BG = new Color(255, 255, 255);
    private static final Color LIGHT_TEXTFIELD_FG = new Color(0, 0, 0);

    private static enum Icon {
        SETTINGS("settings.png", "Settings cog");

        public final String path;
        public final String description;

        private Icon(String path, String description) {
            this.path = path;
            this.description = description;
        }
    }

    private static ImageIcon getIcon(Icon icon) {
        URL resource = App.class
            .getClassLoader()
            .getResource("icons/" + icon.path);
        return new ImageIcon(resource, icon.description);
    }

    private static void applyTheme() {
        try {
            UIManager.setLookAndFeel(
                "javax.swing.plaf.nimbus.NimbusLookAndFeel"
            );

            if (darkMode) {
                // Dark mode colors
                UIManager.put("control", DARK_CONTROL);
                UIManager.put("text", DARK_TEXT);
                UIManager.put("Button.background", DARK_BUTTON_BG);
                UIManager.put("Button.foreground", DARK_BUTTON_FG);
                UIManager.put("Button.focus", DARK_BUTTON_FOCUS);
                UIManager.put("TextField.background", DARK_TEXTFIELD_BG);
                UIManager.put("TextField.foreground", DARK_TEXTFIELD_FG);
                UIManager.put(
                    "TextField.border",
                    BorderFactory.createLineBorder(DARK_BORDER)
                );
            } else {
                // Light mode colors
                UIManager.put("control", LIGHT_CONTROL);
                UIManager.put("text", LIGHT_TEXT);
                UIManager.put("Button.background", LIGHT_BUTTON_BG);
                UIManager.put("Button.foreground", LIGHT_BUTTON_FG);
                UIManager.put("Button.focus", LIGHT_BUTTON_FOCUS);
                UIManager.put("TextField.background", LIGHT_TEXTFIELD_BG);
                UIManager.put("TextField.foreground", LIGHT_TEXTFIELD_FG);
            }
        } catch (
            ClassNotFoundException
            | InstantiationException
            | IllegalAccessException
            | UnsupportedLookAndFeelException e
        ) {
            e.printStackTrace();
        }
    }

    private static void showSettingsDialog(JFrame parent) {
        var settingsDialog = new JDialog(parent, "Settings", true);
        settingsDialog.setSize(400, 300);
        settingsDialog.setLocationRelativeTo(parent);
        settingsDialog.setLayout(new BorderLayout());

        // Create settings panel
        var settingsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        settingsPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Theme toggle
        var themeToggle = new JButton(
            darkMode ? "Switch to Light Mode" : "Switch to Dark Mode"
        );
        themeToggle.addActionListener(e -> {
            darkMode = !darkMode;
            themeToggle.setText(
                darkMode ? "Switch to Light Mode" : "Switch to Dark Mode"
            );
            applyTheme();
            SwingUtilities.updateComponentTreeUI(parent);
            SwingUtilities.updateComponentTreeUI(settingsDialog);
        });

        // API Key input
        var apiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        apiPanel.add(new JLabel("API Key:"));
        var apiField = new JTextField(apiKey, 20);
        apiPanel.add(apiField);

        var saveApiButton = new JButton("Save API Key");
        saveApiButton.addActionListener(e -> {
            apiKey = apiField.getText();
            System.out.println("API Key saved: " + apiKey);
        });

        settingsPanel.add(themeToggle);
        settingsPanel.add(apiPanel);
        settingsPanel.add(saveApiButton);

        settingsDialog.add(settingsPanel, BorderLayout.CENTER);
        settingsDialog.setVisible(true);
    }

    public static void main(String[] args) {
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
            searchField.setPreferredSize(new Dimension(300, 30));
            searchField.setToolTipText("Search...");
            topPanel.add(searchField, BorderLayout.CENTER);

            // Settings button
            var settingsButton = new JButton(getIcon(Icon.SETTINGS));
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
            graphPanel.setPreferredSize(new Dimension(800, 600));
            var graphLabel = new JLabel(
                "Graph will be displayed here",
                JLabel.CENTER
            );
            graphPanel.add(graphLabel);

            frame.add(topPanel, BorderLayout.NORTH);
            frame.add(graphPanel, BorderLayout.CENTER);

            frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
