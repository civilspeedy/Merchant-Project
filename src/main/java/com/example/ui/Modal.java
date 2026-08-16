package com.example.ui;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;

class Modal extends JDialog {

    public Modal(String title, JFrame parent) {
        super(parent, title, true);
        this.setSize(Config.MODAL_SIZE);
        this.setLocationRelativeTo(parent);
        this.setLayout(new BorderLayout());
    }
}
