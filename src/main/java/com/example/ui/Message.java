package com.example.ui;

import java.awt.Component;
import javax.swing.JOptionPane;

final class Message {

    public static void showError(Component parent, Exception e) {
        JOptionPane.showMessageDialog(
            parent,
            e.getMessage(),
            "Something went wrong!",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
