package com.example.ui.modal;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class Modal extends JDialog {
    private static final Dimension SIZE = new Dimension(854, 480);

    public Modal(String title, JFrame parent) {
        super(parent, title, true);
        this.setSize(SIZE);
        this.setLocationRelativeTo(parent);
        this.setLayout(new BorderLayout());
    }
}
