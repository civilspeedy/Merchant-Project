package com.example.ui.components;

import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JPanel;

import com.example.ui.Config;

public class ChildPanel extends JPanel {

    public ChildPanel() {
        super(new FlowLayout(FlowLayout.LEFT), Config.DOUBLE_BUFFER);
    }

    public ChildPanel(Component[] components) {
        this();
        for (Component c : components)
            this.add(c);
    }

    public ChildPanel(Component compOne) {
        this();
        this.add(compOne);
    }

    public ChildPanel(Component compOne, Component compTwo) {
        this();
        this.add(compOne);
        this.add(compTwo);
    }

    public ChildPanel(
            Component compOne,
            Component compTwo,
            Component compThree) {
        this();
        this.add(compOne);
        this.add(compTwo);
        this.add(compThree);
    }

    public ChildPanel(
            Component compOne,
            Component compTwo,
            Component compThree,
            Component compFour) {
        this();
        this.add(compOne);
        this.add(compTwo);
        this.add(compThree);
        this.add(compFour);
    }

    public ChildPanel(
            Component compOne,
            Component compTwo,
            Component compThree,
            Component compFour,
            Component compFive) {
        this();
        this.add(compOne);
        this.add(compTwo);
        this.add(compThree);
        this.add(compFour);
        this.add(compFive);
    }

    public ChildPanel(
            Component compOne,
            Component compTwo,
            Component compThree,
            Component compFour,
            Component compFive,
            Component compSix) {
        this();
        this.add(compOne);
        this.add(compTwo);
        this.add(compThree);
        this.add(compFour);
        this.add(compFive);
        this.add(compSix);
    }
}
