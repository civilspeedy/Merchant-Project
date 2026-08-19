package com.example.ui;

import lombok.NonNull;

/**
 * DataPoint
 */
public record DataPoint(
    @NonNull double[] dependentVariables,
    @NonNull Object[] independentVariables
) {}
