package com.example.ui.Theme;

import java.net.URL;

import javax.swing.ImageIcon;

import lombok.NonNull;

public class Icon {

    private static final ClassLoader CLASS_LOADER = Icon.class.getClassLoader();

    public static final ImageIcon SETTINGS_BLACK = getIcon("settings_black.png", "Settings Cog");
    public static final ImageIcon SETTINGS_WHITE = getIcon("settings_white.png", "Settings Cog");
    public static final ImageIcon SEARCH_BLACK = getIcon("search_black.png", "Search Magnifying Glass");
    public static final ImageIcon SEARCH_WHITE = getIcon("search_white.png", "Search Magnifying Glass");
    public static final ImageIcon SUN = getIcon("sun.png", "Sun Icon");
    public static final ImageIcon MOON = getIcon("moon.png", "Moon Icon");

    private static ImageIcon getIcon(@NonNull String path, @NonNull String description) {
        URL resource = CLASS_LOADER.getResource("icons/" + path);
        return new ImageIcon(resource, description);
    }

}
