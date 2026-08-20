package com.example.ui;

import com.example.ui.Theme.Theme;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public final class Config {

        // Size
        public static final Dimension TEXT_BUTTON_SIZE = new Dimension(100, 65);
        public static final int DEFAULT_INPUT_COLUMNS = 20;

        // Grid
        public static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20);
        public static final GridLayout MODAL_GRID = new GridLayout(0, 1, 10, 10);
        public static final boolean DOUBLE_BUFFER = true;

        // Colors
        private static final Color DARK_GRAY = new Color(60, 63, 65);
        private static final Color LIGHT_GRAY = new Color(214, 217, 223);
        private static final Color OFF_WHITE = new Color(230, 230, 230);
        private static final Color CORNFLOWER_BLUE = new Color(100, 149, 237);
        private static final Color DARKER_GRAY = new Color(50, 53, 55);
        private static final Color LIGHTER_GRAY = new Color(220, 220, 220);
        private static final Color GRAY = new Color(100, 100, 100);
        private static final Color VERY_LIGHT_GRAY = new Color(240, 240, 240);
        private static final Color BLACK = new Color(0, 0, 0);
        private static final Color DEEP_BLUE = new Color(0, 51, 153);
        private static final Color WHITE = new Color(255, 255, 255);

        public static final Theme DARK_THEME = new Theme(
                        DARK_GRAY,
                        LIGHT_GRAY,
                        DARK_GRAY,
                        OFF_WHITE,
                        CORNFLOWER_BLUE,
                        DARKER_GRAY,
                        LIGHTER_GRAY,
                        GRAY,
                        WHITE);

        public static final Theme LIGHT_THEME = new Theme(
                        VERY_LIGHT_GRAY,
                        BLACK,
                        WHITE,
                        DARK_GRAY,
                        DEEP_BLUE,
                        WHITE,
                        BLACK,
                        GRAY,
                        BLACK);
}
