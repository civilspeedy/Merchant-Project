package com.example.ui;

import com.example.database.Database;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

class NewUser {

    private static final String NEW_USER = "New User";
    private JDialog dialog;
    private JLabel warnLabel;

    public NewUser(JFrame parent) {
        var modal = new Modal(NEW_USER, parent);
        this.dialog = modal.getDialog();
        var panel = new JPanel(Config.MODAL_GRID, Config.DOUBLE_BUFFER);

        var passwordLabel = new JLabel("New Password:");
        var passwordField = new JPasswordField(Config.DEFAULT_INPUT_COLUMNS);
        JPanel passwordPanel = ChildPanel.create(passwordLabel, passwordField);

        var confirmLabel = new JLabel("Confirm Password:");
        var confirmField = new JPasswordField(Config.DEFAULT_INPUT_COLUMNS);
        JPanel confirmPanel = ChildPanel.create(confirmLabel, confirmField);

        this.warnLabel = new JLabel("");

        var apiLabel = new JLabel("Massive API:");
        var apiField = new JPasswordField(Config.DEFAULT_INPUT_COLUMNS);
        JPanel apiPanel = ChildPanel.create(apiLabel, apiField);

        var submitButton = new JButton("Submit");
        submitButton.addActionListener(event -> {
            this.submitPassword(passwordField, confirmField);
        });
        JPanel bottomPanel = ChildPanel.create(warnLabel, submitButton);

        panel.add(passwordPanel);
        panel.add(confirmPanel);
        panel.add(apiPanel);
        panel.add(bottomPanel);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void submitPassword(
        JPasswordField passwordField,
        JPasswordField confirmField
    ) {
        var warning = "";
        var pass = new String(passwordField.getPassword());
        var confirm = new String(confirmField.getPassword());

        if (pass.isBlank() || pass.isEmpty()) {
            warning = "Password cannot be blank!";
        } else if (confirm.isBlank() || confirm.isEmpty()) {
            warning = "Please confirm password!";
        } else if (!pass.equals(confirm)) {
            warning = "Passwords do not match!";
        } else {
            if (Database.dbExists()) {
                int response = JOptionPane.showConfirmDialog(
                    this.dialog,
                    "Are you sure? This will erase all existing data.",
                    "Are you sure?",
                    JOptionPane.YES_NO_OPTION
                );

                if (response == JOptionPane.YES_OPTION) {
                    createNewUser(pass);
                    this.dialog.dispose();
                }
            } else {
                createNewUser(pass);
                this.dialog.dispose();
            }
        }
        this.warnLabel.setText(warning);
    }

    private void createNewUser(String password) {
        try {
            Database.newUser(password);
            JOptionPane.showMessageDialog(
                this.dialog,
                "New user creation successful!"
            );
        } catch (Exception e) {
            Message.showError(this.dialog, e);
        }
    }
}
