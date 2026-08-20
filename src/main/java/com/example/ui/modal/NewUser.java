package com.example.ui.modal;

import com.example.database.Database;
import com.example.ui.Config;
import com.example.ui.components.ChildPanel;
import com.example.ui.messages.ErrorMessage;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import lombok.NonNull;
import lombok.val;

public class NewUser implements AuthWindow {

    private static final String NEW_USER = "New User";
    private final Modal modal;
    private final JLabel warnLabel;
    private final JPasswordField passwordField;
    private final JPasswordField confirmField;
    private final JPasswordField apiField;
    private final JFrame parent;
    private boolean complete = false;

    @Override
    public boolean getComplete() {
        return this.complete;
    }

    public Modal getModal() {
        return this.modal;
    }

    public NewUser(@NonNull JFrame parent) {
        this.parent = parent;
        this.modal = new Modal(NEW_USER, this.parent);
        val panel = new JPanel(Config.MODAL_GRID, Config.DOUBLE_BUFFER);

        val passwordLabel = new JLabel("New Password:");
        this.passwordField = new JPasswordField(Config.DEFAULT_INPUT_COLUMNS);
        val passwordPanel = new ChildPanel(passwordLabel, passwordField);

        val confirmLabel = new JLabel("Confirm Password:");
        this.confirmField = new JPasswordField(Config.DEFAULT_INPUT_COLUMNS);
        val confirmPanel = new ChildPanel(confirmLabel, confirmField);

        this.warnLabel = new JLabel("");

        val apiLabel = new JLabel("Massive API:");
        this.apiField = new JPasswordField(Config.DEFAULT_INPUT_COLUMNS);
        val apiPanel = new ChildPanel(apiLabel, apiField);

        val submitButton = new JButton("Submit");
        submitButton.addActionListener(event -> {
            this.submitPassword();
        });
        val bottomPanel = new ChildPanel(warnLabel, submitButton);

        panel.add(passwordPanel);
        panel.add(confirmPanel);
        panel.add(apiPanel);
        panel.add(bottomPanel);

        this.modal.add(panel, BorderLayout.CENTER);
        this.modal.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        try {
                        } catch (Exception ex) {
                            System.out.println(ex);
                            System.exit(-1);
                        }
                    }
                });
        this.modal.setVisible(true);
    }

    private void submitPassword() {
        var warning = "";
        val password = new String(this.passwordField.getPassword());
        val confirm = new String(this.confirmField.getPassword());
        val key = new String(this.apiField.getPassword());

        if (password.isBlank() || password.isEmpty()) {
            warning = "Password cannot be blank!";
        } else if (confirm.isBlank() || confirm.isEmpty()) {
            warning = "Please confirm password!";
        } else if (!password.equals(confirm)) {
            warning = "Passwords do not match!";
        } else {
            if (Database.dbExists()) {
                int response = JOptionPane.showConfirmDialog(
                        this.modal,
                        "Are you sure? This will erase all existing data.",
                        "Are you sure?",
                        JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    createNewUser(password, key);
                    this.modal.dispose();
                    this.complete = true;
                }
            } else {
                createNewUser(password, key);
                this.modal.dispose();
                this.complete = true;
            }
        }
        this.warnLabel.setText(warning);
    }

    private void createNewUser(@NonNull String password, @NonNull String key) {
        try {
            Database.newUser(password, key);
            this.modal.dispose();
        } catch (Exception e) {
            new ErrorMessage(this.modal, e);
        }
    }
}
