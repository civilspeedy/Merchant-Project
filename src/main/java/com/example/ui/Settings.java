package com.example.ui;

import static com.example.util.Log.start;
import static com.example.util.Log.stop;

import com.example.ui.Theme.Theme;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import lombok.val;

class Settings {

    private static final String SETTINGS_TITLE = "Settings";

    private static final String SWITCH_TO_DARK = "Switch to Dark Theme";
    private static final String SWITCH_TO_LIGHT = "Switch to Light Theme";
    public static boolean darkMode = false;

    public Settings(JFrame parent) {
        start("create settings");

        val modal = new Modal(SETTINGS_TITLE, parent);

        val panel = new JPanel(Config.MODAL_GRID, Config.DOUBLE_BUFFER);
        panel.setBorder(Config.EMPTY_BORDER);

        val themeToggle = new JButton(
            darkMode ? SWITCH_TO_LIGHT : SWITCH_TO_DARK
        );
        themeToggle.addActionListener(event -> {
            darkMode = !darkMode;
            themeToggle.setText(darkMode ? SWITCH_TO_LIGHT : SWITCH_TO_DARK);
            applyTheme();
            SwingUtilities.updateComponentTreeUI(parent);
            SwingUtilities.updateComponentTreeUI(modal);
        });

        val childPanel = new JPanel(
            new FlowLayout(FlowLayout.LEFT),
            Config.DOUBLE_BUFFER
        );
        childPanel.add(new JLabel("Massive API Key:"));
        val apiField = new JPasswordField(Config.DEFAULT_INPUT_COLUMNS);
        childPanel.add(apiField);

        val warnLabel = new JLabel("");

        val saveApiButton = new JButton("Save API Key");
        saveApiButton.addActionListener(e -> {
            val apiKey = new String(apiField.getPassword()); // should not be stored as plaintext
            if (apiKey.isEmpty() || apiKey.isBlank()) {
                warnLabel.setText("API key cannot be empty or blank!");
            }
        });

        panel.add(themeToggle);
        panel.add(childPanel);
        panel.add(saveApiButton);

        modal.add(panel, BorderLayout.CENTER);
        modal.setVisible(true);
        stop("create settings");
    }

    public static void applyTheme() {
        start("apply theme");
        try {
            UIManager.setLookAndFeel(
                "javax.swing.plaf.nimbus.NimbusLookAndFeel"
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        Theme theme = darkMode ? Config.DARK_THEME : Config.LIGHT_THEME;

        UIManager.put("control", theme.control());
        UIManager.put("text", theme.text());
        UIManager.put("Button.background", theme.buttonBackground());
        UIManager.put("Button.foreground", theme.buttonForeground());
        UIManager.put("Button.focus", theme.buttonFocus());
        UIManager.put("TextField.background", theme.textfieldBackground());
        UIManager.put("TextField.foreground", theme.textfieldForeground());
        UIManager.put("TextField.border", theme.border());
        Graph.setGraphColour(theme.graphLine());
        stop("apply theme");
    }
}
