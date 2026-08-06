package com.example.util;

import java.net.URL;
import javax.swing.ImageIcon;
import tools.jackson.databind.ObjectMapper;

public final class Resource {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static final <T> T get(String path, Class<T> object)
        throws Exception {
        var resourceStr = new String(
            Resource.class
                .getClassLoader()
                .getResourceAsStream(path)
                .readAllBytes()
        );

        return mapper.readValue(resourceStr, object);
    }

    public static enum Icon {
        SETTINGS("settings.png", "Settings cog");

        public final String path;
        public final String description;

        private Icon(String path, String description) {
            this.path = path;
            this.description = description;
        }
    }

    public static final ImageIcon getIcon(Icon icon) {
        URL resource = Resource.class
            .getClassLoader()
            .getResource("icons/" + icon.path);
        return new ImageIcon(resource, icon.description);
    }
}
