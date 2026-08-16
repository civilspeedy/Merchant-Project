package com.example.ui;

import com.example.database.Database;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import lombok.NonNull;
import lombok.val;

class NewUser {

    private static final String NEW_USER = "New User";
    private Modal modal;
    private JLabel warnLabel;

    public NewUser(@NonNull JFrame parent) {
        this.modal = new Modal(NEW_USER, parent);
        val panel = new JPanel(Config.MODAL_GRID, Config.DOUBLE_BUFFER);

        val passwordLabel = new JLabel("New Password:");
        val passwordField = new JPasswordField(Config.DEFAULT_INPUT_COLUMNS);
        val passwordPanel = new ChildPanel(passwordLabel, passwordField);

        val confirmLabel = new JLabel("Confirm Password:");
        val confirmField = new JPasswordField(Config.DEFAULT_INPUT_COLUMNS);
        val confirmPanel = new ChildPanel(confirmLabel, confirmField);

        this.warnLabel = new JLabel("");

        val apiLabel = new JLabel("Massive API:");
        val apiField = new JPasswordField(Config.DEFAULT_INPUT_COLUMNS);
        val apiPanel = new ChildPanel(apiLabel, apiField);

        val submitButton = new JButton("Submit");
        submitButton.addActionListener(event -> {
            this.submitPassword(passwordField, confirmField, apiField);
        });
        val bottomPanel = new ChildPanel(warnLabel, submitButton);

        panel.add(passwordPanel);
        panel.add(confirmPanel);
        panel.add(apiPanel);
        panel.add(bottomPanel);

        modal.add(panel, BorderLayout.CENTER);
        modal.setVisible(true);
    }

    private void submitPassword(
        JPasswordField passwordField,
        JPasswordField confirmField,
        JPasswordField apiField
    ) {
        var warning = "";
        val pass = new String(passwordField.getPassword());
        val confirm = new String(confirmField.getPassword());
        val key = new String(apiField.getPassword());

        if (pass.isBlank() || pass.isEmpty()) {
            warning = "Password cannot be blank!";
        } else if (confirm.isBlank() || confirm.isEmpty()) {
            warning = "Please confirm password!";
        } else if (!pass.equals(confirm)) {
            warning = "Passwords do not match!";
        } else {
            if (Database.dbExists()) {
                int response = JOptionPane.showConfirmDialog(
                    this.modal,
                    "Are you sure? This will erase all existing data.",
                    "Are you sure?",
                    JOptionPane.YES_NO_OPTION
                );

                if (response == JOptionPane.YES_OPTION) {
                    createNewUser(pass, key);
                    this.modal.dispose();
                }
            } else {
                createNewUser(pass, key);
                this.modal.dispose();
            }
        }
        this.warnLabel.setText(warning);
    }

    private void createNewUser(@NonNull String password, @NonNull String key) {
        try {
            Database.newUser(password, key);
            this.modal.dispose();
        } catch (Exception e) {
            Message.showError(this.modal, e);
            System.out.println(e);
        }
    }
}
