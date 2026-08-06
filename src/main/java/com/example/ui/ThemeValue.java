package com.example.ui;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.awt.Color;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ThemeValue(String key, Color color) {
    @JsonCreator
    public ThemeValue create(
        @JsonProperty("key") String key,
        @JsonProperty("r") int red,
        @JsonProperty("g") int green,
        @JsonProperty("b") int blue
    ) {
        return new ThemeValue(key, new Color(red, green, blue));
    }
}
