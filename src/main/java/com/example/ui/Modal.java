package com.example.ui;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

    public void onKey(int keyCode, Runnable l) {
        this.dialog.addKeyListener(
            new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == keyCode) {
                        l.run();
                    }
                }

                public void keyReleased(KeyEvent e) {}

                public void keyTyped(KeyEvent e) {}
            }
        );
    }
}
