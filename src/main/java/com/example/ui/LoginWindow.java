package com.example.ui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class LoginWindow {

    private static final int WIDTH = 854;
    private static final int HEIGHT = 480;
    private static final String TITLE = "Login";

    public static void start() {
        SwingUtilities.invokeLater(() -> {
            var frame = new JFrame(TITLE);
        });
    }
}
