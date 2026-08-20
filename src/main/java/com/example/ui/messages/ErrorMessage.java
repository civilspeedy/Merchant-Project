package com.example.ui.messages;

import java.awt.Component;

import javax.swing.JOptionPane;

import lombok.NonNull;

public class ErrorMessage {
    public ErrorMessage(@NonNull Component parent, @NonNull Exception e) {
        JOptionPane.showMessageDialog(
                parent,
                e.getMessage(),
                "Something went wrong!",
                JOptionPane.ERROR_MESSAGE);
    }
}