package com.example.ui;

import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JPanel;

class ChildPanel {

    private JPanel panel;

    public ChildPanel(Component[] components) {
        this.panel = new JPanel(
            new FlowLayout(FlowLayout.LEFT),
            Config.DOUBLE_BUFFER
        );
        for (var c : components) this.panel.add(c);
    }

    public JPanel getPanel() {
        return this.panel;
    }
}
