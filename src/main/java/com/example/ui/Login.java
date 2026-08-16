package com.example.ui;

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
import static com.example.util.Log.out;

class Login {

    private static final String LOGIN_TITLE = "Login";

    public Login(JFrame parent) {
        out("creating login modal");
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
        out("submitting password");
        try {
            Database.login(new String(passwordField.getPassword()));
            dialog.dispose();
            // trigger load
        } catch (SQLInvalidAuthorizationSpecException e) {
            JOptionPane.showMessageDialog(dialog, "Invalid password!");
        } catch (Exception e) {
            Message.showError(dialog, e);
        }
    }
}
