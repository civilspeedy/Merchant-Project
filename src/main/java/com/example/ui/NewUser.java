package com.example.ui;

import com.example.database.Manager;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

class NewUser {

    private static final String NEW_USER = "New User";

    public NewUser(JFrame parent) {
        var dialog = new Modal(NEW_USER, parent).getDialog();
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

        var warnLabel = new JLabel("");
        var submitButton = new JButton("Submit");
        submitButton.addActionListener(event -> {
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

                if (response == JOptionPane.YES_OPTION) {
                    createNewUser(dialog, pass);
                } else {
                    System.out.println("No");
                }
            }
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

    private static void createNewUser(Component parent, String password) {
        try {
            Manager.newUser(password);
            JOptionPane.showMessageDialog(
                parent,
                "New user creation successful!"
            );
            // parent needs to close
        } catch (Exception e) {
            Message.showError(parent, e);
        }
    }
}
