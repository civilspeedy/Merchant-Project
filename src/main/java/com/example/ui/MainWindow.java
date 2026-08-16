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
import static com.example.util.Log.out;

public class MainWindow {

    // Size
    private static final Dimension WINDOW_SIZE = new Dimension(1920, 1080);
    private static final Dimension SEARCH_FIELD_SIZE = new Dimension(300, 30);
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
        out("creating main window");
        try {
            out("fetching themes");
            darkTheme = Resource.get(DARK_THEME_PATH, ThemeValue[].class);
            lightTheme = Resource.get(LIGHT_THEME_PATH, ThemeValue[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            Settings.applyTheme();

            out("creating primary frame");
            val frame = new JFrame(WINDOW_TITLE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            out("creating top panel");
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
            frame.setVisible(true);

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

            val graph = new Graph(sampleData, sampleTimes);
            frame.add(graph, BorderLayout.CENTER);

            if (Database.dbExists()) {
                new Login(frame);
            } else {
                new NewUser(frame);
            }
        });
    }
}
