package com.example.ui;

import com.example.database.Database;
import com.example.util.Resource;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalDateTime;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import lombok.val;

public class MainWindow {

    // Size
    private static final Dimension WINDOW_SIZE = new Dimension(1920, 1080);
    private static final Dimension SEARCH_FIELD_SIZE = new Dimension(300, 30);
    private static final Dimension ICON_BUTTON_SIZE = new Dimension(75, 75);

    // Display Text
    private static final String WINDOW_TITLE = "Merchant";
    private static JFrame frame;

    public static void start() {
        SwingUtilities.invokeLater(() -> {
            Settings.applyTheme();
            frame = new JFrame(WINDOW_TITLE);
            frame.setVisible(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            val topPanel = new JPanel(new BorderLayout(), Config.DOUBLE_BUFFER);
            val searchField = new JTextField(Config.DEFAULT_INPUT_COLUMNS);
            searchField.setPreferredSize(SEARCH_FIELD_SIZE);
            searchField.setToolTipText("Search...");
            topPanel.add(searchField, BorderLayout.CENTER);

            // Settings button
            val settingsButton = new JButton(
                Resource.getIcon(Resource.Icon.SETTINGS)
            );
            settingsButton.setToolTipText("Settings");
            settingsButton.setPreferredSize(ICON_BUTTON_SIZE);
            settingsButton.addActionListener(event -> {
                new Settings(frame);
            });
            topPanel.add(settingsButton, BorderLayout.EAST);

            frame.add(topPanel, BorderLayout.NORTH);

            frame.setSize(WINDOW_SIZE);
            frame.setLocationRelativeTo(null);

            double[] sampleData = { 10, 25, 15, 40, 35, 50, 45, 60, 55, 70 };

            // Create sample LocalDateTime values all on the same day (today) - ai generated
            LocalDateTime[] sampleTimes = new LocalDateTime[sampleData.length];
            LocalDateTime baseDate = LocalDateTime.now()
                .withHour(9)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
            for (int i = 0; i < sampleTimes.length; i++) {
                sampleTimes[i] = baseDate.plusHours(i);
            }

            authenticate();

            val graph = new Graph(sampleData, sampleTimes);
            frame.add(graph, BorderLayout.CENTER);
        });
    }

    private static void authenticate() {
        val window = Database.dbExists()
            ? new Login(frame)
            : new NewUser(frame);

        if (window.getComplete()) {
            frame.setVisible(true);
        } else {
            authenticate();
        }
    }
}
