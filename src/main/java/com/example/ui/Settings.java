package com.example.ui;

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
    }

    public static void applyTheme() {
        try {
            UIManager.setLookAndFeel(
                "javax.swing.plaf.nimbus.NimbusLookAndFeel"
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        ThemeValue[] theme = MainWindow.getTheme(darkMode);
        for (val t : theme) UIManager.put(t.key(), t.color());
    }
}
