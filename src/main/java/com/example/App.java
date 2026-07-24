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
                UIManager.put("control", new Color(60, 63, 65));
                UIManager.put("text", new Color(214, 217, 223));
                UIManager.put("Button.background", new Color(70, 73, 75));
                UIManager.put("Button.foreground", new Color(230, 230, 230));
                UIManager.put("Button.focus", new Color(100, 149, 237));
                UIManager.put("TextField.background", new Color(50, 53, 55));
                UIManager.put("TextField.foreground", new Color(220, 220, 220));
                UIManager.put(
                    "TextField.border",
                    BorderFactory.createLineBorder(new Color(100, 100, 100))
                );
            } else {
                // Light mode colors
                UIManager.put("control", new Color(240, 240, 240));
                UIManager.put("text", new Color(0, 0, 0));
                UIManager.put("Button.background", new Color(230, 230, 230));
                UIManager.put("Button.foreground", new Color(30, 30, 30));
                UIManager.put("Button.focus", new Color(0, 51, 153));
                UIManager.put("TextField.background", new Color(255, 255, 255));
                UIManager.put("TextField.foreground", new Color(0, 0, 0));
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
        JDialog settingsDialog = new JDialog(parent, "Settings", true);
        settingsDialog.setSize(400, 300);
        settingsDialog.setLocationRelativeTo(parent);
        settingsDialog.setLayout(new BorderLayout());

        // Create settings panel
        JPanel settingsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        settingsPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Theme toggle
        JButton themeToggle = new JButton(
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
        JPanel apiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        apiPanel.add(new JLabel("API Key:"));
        JTextField apiField = new JTextField(apiKey, 20);
        apiPanel.add(apiField);

        JButton saveApiButton = new JButton("Save API Key");
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
            JPanel topPanel = new JPanel(new BorderLayout());
            JTextField searchField = new JTextField();
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
            JPanel graphPanel = new JPanel();
            graphPanel.setBorder(
                BorderFactory.createTitledBorder("Graph Display")
            );
            graphPanel.setPreferredSize(new Dimension(800, 600));
            JLabel graphLabel = new JLabel(
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
