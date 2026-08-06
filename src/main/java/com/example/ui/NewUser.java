package com.example.ui;

import com.example.database.Manager;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyEvent;
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
        var passwordPanel = new ChildPanel(new Component[] {
            passwordLabel,
            passwordField,
        }).getPanel();

        var confirmLabel = new JLabel("Confirm Password:");
        var confirmField = new JPasswordField(Config.DEFAULT_INPUT_COLUMNS);
        var confirmPanel = new ChildPanel(new Component[] {
            confirmLabel,
            confirmField,
        }).getPanel();

        this.warnLabel = new JLabel("");

        var submitButton = new JButton("Submit");
        submitButton.addActionListener(event -> {
            this.submitPassword(passwordField, confirmField);
        });

        var bottomPanel = new ChildPanel(new Component[] {
            warnLabel,
            submitButton,
        }).getPanel();

        panel.add(passwordPanel);
        panel.add(confirmPanel);
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
            int response = JOptionPane.showConfirmDialog(
                this.dialog,
                "Are you sure? This will erase all existing data.",
                "Are you sure?",
                JOptionPane.YES_NO_OPTION
            );

            if (response == JOptionPane.YES_OPTION) {
                createNewUser(pass);
            }
            this.dialog.dispose();
        }
        this.warnLabel.setText(warning);
    }

    private void createNewUser(String password) {
        try {
            Manager.newUser(password);
            JOptionPane.showMessageDialog(
                this.dialog,
                "New user creation successful!"
            );
        } catch (Exception e) {
            Message.showError(this.dialog, e);
        }
    }
}
