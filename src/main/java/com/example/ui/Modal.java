package com.example.ui;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;

class Modal {

    private JDialog dialog;

    public Modal(String title, JFrame parent) {
        this.dialog = new JDialog(parent, title, true);
        dialog.setSize(Config.MODAL_SIZE);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());
    }

    public JDialog getDialog() {
        return this.dialog;
    }
}
