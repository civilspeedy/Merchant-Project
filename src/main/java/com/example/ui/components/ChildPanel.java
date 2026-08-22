package com.example.ui.components;

import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JPanel;

import com.example.ui.Config;

public class ChildPanel extends JPanel {

    public ChildPanel(Component... components) {
        super(new FlowLayout(FlowLayout.LEFT), Config.DOUBLE_BUFFER);
        for (Component c : components)
            this.add(c);
    }
}