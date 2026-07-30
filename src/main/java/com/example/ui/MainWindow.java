package com.example.ui;

import com.example.database.Manager;
import com.example.util.Exit;
import com.example.util.Resource;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainWindow {

    // Size
    private static final Dimension WINDOW_SIZE = new Dimension(1920, 1080);
    private static final Dimension MODAL_SIZE = new Dimension(854, 480);
    private static final Dimension SEARCH_FIELD_SIZE = new Dimension(300, 30);
    private static final Dimension GRAPH_SIZE = new Dimension(800, 600);
    private static final Dimension TEXT_BUTTON_SIZE = new Dimension(100, 65);
    private static final Dimension ICON_BUTTON_SIZE = new Dimension(75, 75);
    private static final int DEFAULT_INPUT_COLUMNS = 20; // they are still way to big

    // Grid
    private static final GridLayout MODAL_GRID = new GridLayout(0, 1, 10, 10);

    // Display Text
    private static final String SWITCH_TO_DARK = "Switch to Dark Theme";
    private static final String SWITCH_TO_LIGHT = "Switch to Light Theme";
    private static final String WINDOW_TITLE = "Merchant";
    private static final String SETTINGS_TITLE = "Settings";
    private static final String LOGIN_TITLE = "Login";
    private static final String NEW_USER = "New User";

    // Theme
    private static boolean darkMode = false;
    private static final String LIGHT_THEME_PATH = "json/lightTheme.json";
    private static final String DARK_THEME_PATH = "json/darkTheme.json";
    private static ThemeValue[] lightTheme;
    private static ThemeValue[] darkTheme;
    private static final boolean DOUBLE_BUFFER = true;

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
            var topPanel = new JPanel(new BorderLayout(), DOUBLE_BUFFER);
            var searchField = new JTextField(DEFAULT_INPUT_COLUMNS);
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

            showLoginDialog(frame);
        });
    }

    private static void applyTheme() {
        try {
            UIManager.setLookAndFeel(
                "javax.swing.plaf.nimbus.NimbusLookAndFeel"
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(Exit.ERR.code);
        }

        ThemeValue[] theme = darkMode ? darkTheme : lightTheme;
        for (var t : theme) UIManager.put(t.key(), t.color());
    }

    private static void addToPanel(JPanel panel, Component[] components) {
        for (var c : components) panel.add(c);
    }

    private static JDialog newModal(String title, JFrame parent) {
        var dialog = new JDialog(parent, title, true);
        dialog.setSize(MODAL_SIZE);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());
        return dialog;
    }

    private static JPanel newChildPanel(Component[] components) {
        var panel = new JPanel(new FlowLayout(FlowLayout.LEFT), DOUBLE_BUFFER);
        addToPanel(panel, components);
        return panel;
    }

    private static void createNewUser(Component parent, String password) {
        try {
            Manager.newUser(password);
            JOptionPane.showMessageDialog(
                parent,
                "New user creation successful!"
            );
            // parent needs to close
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                parent,
                ex.getMessage(),
                "Something went wrong!",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private static void showNewUserDialog(JFrame parent) {
        var dialog = newModal(NEW_USER, parent);
        var panel = new JPanel(MODAL_GRID, DOUBLE_BUFFER);

        var passwordLabel = new JLabel("New Password:");
        var passwordField = new JPasswordField(DEFAULT_INPUT_COLUMNS);
        var passwordPanel = newChildPanel(new Component[] {
            passwordLabel,
            passwordField,
        });

        var confirmLabel = new JLabel("Confirm Password:");
        var confirmField = new JPasswordField(DEFAULT_INPUT_COLUMNS);
        var confirmPanel = newChildPanel(new Component[] {
            confirmLabel,
            confirmField,
        });

        var warnLabel = new JLabel("");
        var submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            var pass = new String(passwordField.getPassword());
            var confirm = new String(confirmField.getPassword());

            if (pass.isBlank() || pass.isEmpty()) {
                warnLabel.setText("Password cannot be blank!");
            } else if (confirm.isBlank() || confirm.isEmpty()) {
                warnLabel.setText("Please confirm password!");
            } else if (!pass.equals(confirm)) {
                warnLabel.setText("Passwords do not match!");
            } else {
                int response = JOptionPane.showConfirmDialog(
                    dialog,
                    "Are you sure? This will erase all existing data.",
                    "Are you sure?",
                    JOptionPane.YES_NO_OPTION
                );

                switch (response) {
                    case JOptionPane.YES_OPTION:
                        createNewUser(dialog, pass);
                        break;
                    case JOptionPane.NO_OPTION:
                        System.out.println("No");
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        System.out.println("Cancel");
                        break;
                    default:
                        System.out.println("Default");
                        break;
                }
            }
        });

        var bottomPanel = newChildPanel(new Component[] {
            warnLabel,
            submitButton,
        });

        addToPanel(panel, new Component[] {
            passwordPanel,
            confirmPanel,
            bottomPanel,
        });

        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private static void showLoginDialog(JFrame parent) {
        var dialog = newModal(LOGIN_TITLE, parent);

        var panel = new JPanel(MODAL_GRID, DOUBLE_BUFFER);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        var passwordField = new JPasswordField(DEFAULT_INPUT_COLUMNS);
        var submitButton = new JButton("Submit");
        submitButton.setPreferredSize(TEXT_BUTTON_SIZE);
        submitButton.addActionListener(e -> {});

        var newUserButton = new JButton(NEW_USER);
        newUserButton.setPreferredSize(TEXT_BUTTON_SIZE);
        newUserButton.addActionListener(e -> {
            dialog.setVisible(false);
            showNewUserDialog(parent);
            dialog.setVisible(true);
        });

        var components = new Component[] {
            passwordField,
            submitButton,
            newUserButton,
        };
        addToPanel(panel, components);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private static void showSettingsDialog(JFrame parent) {
        var dialog = newModal(SETTINGS_TITLE, parent);

        // Create settings panel
        var mainPanel = new JPanel(MODAL_GRID, DOUBLE_BUFFER);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Theme toggle
        var themeToggle = new JButton(
            darkMode ? SWITCH_TO_LIGHT : SWITCH_TO_DARK
        );
        themeToggle.addActionListener(e -> {
            darkMode = !darkMode;
            themeToggle.setText(darkMode ? SWITCH_TO_LIGHT : SWITCH_TO_DARK);
            applyTheme();
            SwingUtilities.updateComponentTreeUI(parent);
            SwingUtilities.updateComponentTreeUI(dialog);
        });
        // API Key input
        var childPanel = new JPanel(
            new FlowLayout(FlowLayout.LEFT),
            DOUBLE_BUFFER
        );
        childPanel.add(new JLabel("Massive API Key:"));
        var apiField = new JPasswordField(DEFAULT_INPUT_COLUMNS);
        childPanel.add(apiField);

        var saveApiButton = new JButton("Save API Key");
        saveApiButton.addActionListener(e -> {
            var apiKey = new String(apiField.getPassword()); // should not be stored as plaintext
        });

        var components = new Component[] {
            themeToggle,
            childPanel,
            saveApiButton,
        };

        addToPanel(mainPanel, components);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}
