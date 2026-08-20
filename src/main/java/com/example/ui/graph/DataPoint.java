package com.example.ui.graph;

import lombok.NonNull;

/**
 * DataPoint
 */
public record DataPoint(
        @NonNull double[] dependentVariables,
        @NonNull Object[] independentVariables) {
}
