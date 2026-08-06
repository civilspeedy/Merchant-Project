package com.example.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

final class Config {

    //Size
    public static final Dimension MODAL_SIZE = new Dimension(854, 480);
    public static final Dimension TEXT_BUTTON_SIZE = new Dimension(100, 65);
    public static final int DEFAULT_INPUT_COLUMNS = 20;

    // Grid
    public static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(
        20,
        20,
        20,
        20
    );
    public static final GridLayout MODAL_GRID = new GridLayout(0, 1, 10, 10);
    public static final boolean DOUBLE_BUFFER = true;
}
