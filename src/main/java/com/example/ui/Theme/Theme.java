package com.example.ui.Theme;

import java.awt.Color;
import lombok.NonNull;

/**
 * Theme
 */
public record Theme(
    @NonNull Color control,
    @NonNull Color text,
    @NonNull Color buttonBackground,
    @NonNull Color buttonForeground,
    @NonNull Color buttonFocus,
    @NonNull Color textfieldBackground,
    @NonNull Color textfieldForeground,
    Color border
) {}
