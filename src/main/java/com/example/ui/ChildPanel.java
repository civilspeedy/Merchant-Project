package com.example.ui;

import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import lombok.val;

class ChildPanel {

    private static JPanel createPanel() {
        return new JPanel(
            new FlowLayout(FlowLayout.LEFT),
            Config.DOUBLE_BUFFER
        );
    }

    public static JPanel create(Component[] components) {
        val panel = createPanel();
        for (val c : components) panel.add(c);
        return panel;
    }

    public static JPanel create(Component compOne) {
        val panel = createPanel();
        panel.add(compOne);
        return panel;
    }

    public static JPanel create(Component compOne, Component compTwo) {
        val panel = createPanel();
        panel.add(compOne);
        panel.add(compTwo);
        return panel;
    }

    public static JPanel create(
        Component compOne,
        Component compTwo,
        Component compThree
    ) {
        val panel = createPanel();
        panel.add(compOne);
        panel.add(compTwo);
        panel.add(compThree);
        return panel;
    }

    public static JPanel create(
        Component compOne,
        Component compTwo,
        Component compThree,
        Component compFour
    ) {
        val panel = createPanel();
        panel.add(compOne);
        panel.add(compTwo);
        panel.add(compThree);
        panel.add(compFour);
        return panel;
    }

    public static JPanel create(
        Component compOne,
        Component compTwo,
        Component compThree,
        Component compFour,
        Component compFive
    ) {
        val panel = createPanel();
        panel.add(compOne);
        panel.add(compTwo);
        panel.add(compThree);
        panel.add(compFour);
        panel.add(compFive);
        return panel;
    }

    public static JPanel create(
        Component compOne,
        Component compTwo,
        Component compThree,
        Component compFour,
        Component compFive,
        Component compSix
    ) {
        val panel = createPanel();
        panel.add(compOne);
        panel.add(compTwo);
        panel.add(compThree);
        panel.add(compFour);
        panel.add(compFive);
        panel.add(compSix);
        return panel;
    }
}
