package com.example.ui;

import static com.example.util.Log.start;
import static com.example.util.Log.stop;

import com.example.database.Database;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLInvalidAuthorizationSpecException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import lombok.val;

class Login implements AuthWindow {

    private static final String LOGIN_TITLE = "Login";
    private final Modal modal;
    private final JPasswordField passwordField;
    private boolean complete = false;
    private NewUser newUserWindow;

    @Override
    public boolean getComplete() {
        return this.complete;
    }

    public Login(JFrame parent) {
        this.modal = new Modal(LOGIN_TITLE, parent);

        val panel = new JPanel(Config.MODAL_GRID, Config.DOUBLE_BUFFER);
        panel.setBorder(Config.EMPTY_BORDER);

        this.passwordField = new JPasswordField(Config.DEFAULT_INPUT_COLUMNS);

        val submitButton = new JButton("Submit");
        submitButton.setPreferredSize(Config.TEXT_BUTTON_SIZE);
        submitButton.addActionListener(e -> {
            this.submitPassword();
        });

        val newUserButton = new JButton("Create New User");
        newUserButton.setPreferredSize(Config.TEXT_BUTTON_SIZE);
        newUserButton.addActionListener(e -> {
            modal.setVisible(false);
            newUserWindow = new NewUser(parent);
            newUserWindow.getModal().addWindowListener(
                    new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            newUserWindow = null;
                            modal.setVisible(true);
                        }
                    });
        });

        panel.add(passwordField);
        panel.add(submitButton);
        panel.add(newUserButton);

        this.modal.add(panel, BorderLayout.CENTER);
        this.modal.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        try {
                            System.exit(0);
                        } catch (Exception ex) {
                            System.out.println(ex);
                            System.exit(-1);
                        }

                    }
                });
        this.modal.setVisible(true);
    }

    private void submitPassword() {
        start("submit password");
        try {
            Database.login(new String(this.passwordField.getPassword()));
            stop("submit password");
            this.modal.dispose();
            this.complete = true;
            // trigger load
        } catch (SQLInvalidAuthorizationSpecException e) {
            JOptionPane.showMessageDialog(this.modal, "Invalid password!");
        } catch (Exception e) {
            Message.showError(this.modal, e);
        }
    }
}
