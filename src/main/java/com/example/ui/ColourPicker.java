package com.example.ui;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import lombok.NonNull;

public class ColourPicker extends JButton {

    private Color colour;

    public ColourPicker(@NonNull String title, @NonNull Color defaultColour) {
        super(title);

        this.addActionListener(e -> {
            this.colour = JColorChooser.showDialog(
                this,
                "Colour Picker",
                defaultColour
            );
        });
    }

    public Color getColour() {
        return this.colour;
    }
}
