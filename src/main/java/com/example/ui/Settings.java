package com.example.ui;

import static com.example.util.Log.start;
import static com.example.util.Log.stop;

import com.example.ui.Theme.Icon;
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

    private static boolean darkMode = false;
    private static final JButton themeToggle = new JButton(Icon.MOON);

    public Settings(JFrame parent) {
        start("create settings");

        val modal = new Modal(SETTINGS_TITLE, parent);

        val panel = new JPanel(Config.MODAL_GRID, Config.DOUBLE_BUFFER);
        panel.setBorder(Config.EMPTY_BORDER);

        themeToggle.addActionListener(event -> {
            darkMode = !darkMode;
            applyTheme();
            SwingUtilities.updateComponentTreeUI(parent);
            SwingUtilities.updateComponentTreeUI(modal);
        });

        val childPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT),
                Config.DOUBLE_BUFFER);
        childPanel.add(new JLabel("Massive API Key:"));
        val apiField = new JPasswordField(Config.DEFAULT_INPUT_COLUMNS);
        childPanel.add(apiField);

        val warnLabel = new JLabel("");

        val saveApiButton = new JButton("Update API Key");
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
        Theme theme = darkMode ? Config.DARK_THEME : Config.LIGHT_THEME;

        UIManager.put("control", theme.control());
        UIManager.put("text", theme.text());
        UIManager.put("Button.background", theme.buttonBackground());
        UIManager.put("Button.foreground", theme.buttonForeground());
        UIManager.put("Button.focus", theme.buttonFocus());
        UIManager.put("Button[Default].background", theme.buttonBackground());
        UIManager.put("Button[Default].foreground", theme.buttonForeground());
        UIManager.put("Button[Default].focus", theme.buttonFocus());
        UIManager.put("TextField.background", theme.textfieldBackground());
        UIManager.put("TextField.foreground", theme.textfieldForeground());
        UIManager.put("TextField.border", theme.border());
        Graph.setGraphColour(theme.graphLine());
        MainWindow.setSettingsIcon(
                darkMode ? Icon.SETTINGS_WHITE : Icon.SETTINGS_BLACK);
        MainWindow.setSearchIcon(darkMode ? Icon.SEARCH_WHITE : Icon.SEARCH_BLACK);
        themeToggle.setIcon(darkMode ? Icon.SUN : Icon.MOON);
        stop("apply theme");
    }
}
