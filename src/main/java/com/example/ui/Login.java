package com.example.ui;

import static com.example.util.Log.clear;
import static com.example.util.Log.start;
import static com.example.util.Log.stop;

import com.example.database.Database;
import java.awt.BorderLayout;
import java.sql.SQLInvalidAuthorizationSpecException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import lombok.val;

class Login {

    private static final String LOGIN_TITLE = "Login";

    public Login(JFrame parent) {
        val modal = new Modal(LOGIN_TITLE, parent);

        val panel = new JPanel(Config.MODAL_GRID, Config.DOUBLE_BUFFER);
        panel.setBorder(Config.EMPTY_BORDER);

        val passwordField = new JPasswordField(Config.DEFAULT_INPUT_COLUMNS);

        val submitButton = new JButton("Submit");
        submitButton.setPreferredSize(Config.TEXT_BUTTON_SIZE);
        submitButton.addActionListener(event -> {
            submitPassword(modal, passwordField);
        });

        val newUserButton = new JButton("Create New User");
        newUserButton.setPreferredSize(Config.TEXT_BUTTON_SIZE);
        newUserButton.addActionListener(e -> {
            modal.setVisible(false);
            new NewUser(parent);
            modal.setVisible(true);
        });

        panel.add(passwordField);
        panel.add(submitButton);
        panel.add(newUserButton);

        modal.add(panel, BorderLayout.CENTER);
        modal.setVisible(true);
    }

    private static void submitPassword(
        JDialog dialog,
        JPasswordField passwordField
    ) {
        start("submit password");
        try {
            Database.login(new String(passwordField.getPassword()));
            stop("submit password");
            dialog.dispose();
            // trigger load
        } catch (SQLInvalidAuthorizationSpecException e) {
            clear("login failed");
            JOptionPane.showMessageDialog(dialog, "Invalid password!");
        } catch (Exception e) {
            clear("login failed");
            Message.showError(dialog, e);
        }
    }
}
