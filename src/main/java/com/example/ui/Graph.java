package com.example.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import javax.swing.JPanel;
import lombok.NonNull;
import lombok.val;

/**
 * Based off https://stackoverflow.com/a/8693635
 * Graph
 */
public class Graph extends JPanel {

    private static final DateTimeFormatter timeFormatter =
        DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter dateFormatter =
        DateTimeFormatter.ofPattern("MM-dd");
    private static final DateTimeFormatter dateTimeFormatter =
        DateTimeFormatter.ofPattern("MM-dd HH:mm");

    private static final Dimension SIZE = new Dimension(400, 650);
    private static final int GAP = 30;
    private static final int GRAPH_POINT_WIDTH = 12;
    private static final int Y_HATCH_COUNT = 10;

    private double[] values;
    private double maxValue;
    private double minValue;
    private int numOfVals;
    private LocalDateTime[] times;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    private boolean allSameDay;
    private boolean allDifferentDays;

    public Graph(@NonNull double[] values, @NonNull LocalDateTime[] times) {
        this.values = values;
        this.times = times;

        this.maxValue = Arrays.stream(values).max().getAsDouble();
        this.minValue = Arrays.stream(values).min().getAsDouble();
        this.numOfVals = values.length;

        if (times.length != values.length) {
            throw new IllegalArgumentException(
                "values and times arrays must be same length"
            );
        }
        this.checkTimes(times);

        this.setPreferredSize(SIZE);
    }

    private void checkTimes(LocalDateTime[] times) {
        this.allSameDay = true;
        this.allDifferentDays = true;

        this.minTime = times[0];
        this.maxTime = times[times.length - 1];

        for (int i = 0; i < times.length; i++) {
            // Check if chronological
            if (i > 0 && times[i].isBefore(times[i - 1])) {
                throw new IllegalArgumentException(
                    "Times must be in chronological order"
                );
            }

            // Check if all on same day
            if (!times[i].toLocalDate().equals(minTime.toLocalDate())) {
                this.allSameDay = false;
            }

            // Check if all on different days
            if (this.allDifferentDays) {
                for (int j = 0; j < i; j++) {
                    if (times[i].toLocalDate().equals(times[j].toLocalDate())) {
                        this.allDifferentDays = false;
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Handle edge case where all values are the same
        double valueRange = maxValue - minValue;
        if (valueRange == 0) {
            valueRange = 1; // Avoid division by zero
        }

        long timeRange = Duration.between(minTime, maxTime).getSeconds();

        if (timeRange == 0) {
            timeRange = 1;
        }

        val xScale = ((double) this.getWidth() - 2 * GAP) / timeRange;
        val yScale = ((double) this.getHeight() - 2 * GAP) / valueRange;

        val graphPoints = new Point[this.numOfVals];
        for (int i = 0; i < this.numOfVals; i++) {
            long secondsFromStart = Duration.between(
                minTime,
                times[i]
            ).getSeconds();
            int x = (int) (secondsFromStart * xScale) + GAP;
            int y = (int) ((this.maxValue - this.values[i]) * yScale) + GAP;

            // Clamp points to stay within panel bounds
            x = Math.max(GAP, Math.min(this.getWidth() - GAP, x));
            y = Math.max(GAP, Math.min(this.getHeight() - GAP, y));

            graphPoints[i] = new Point(x, y);
        }

        // x axis
        graphics2D.drawLine(GAP, this.getHeight() - GAP, GAP, GAP);

        // y axis
        graphics2D.drawLine(
            GAP,
            this.getHeight() - GAP,
            getWidth() - GAP,
            getHeight() - GAP
        );

        // Set font for axis labels
        graphics2D.setFont(new Font("Arial", Font.PLAIN, 12));
        graphics2D.setColor(Color.BLACK);

        // y hatches and labels
        for (int i = 0; i <= Y_HATCH_COUNT; i++) {
            int x = GRAPH_POINT_WIDTH + GAP;
            int y =
                this.getHeight() -
                ((i * (this.getHeight() - GAP * 2)) / Y_HATCH_COUNT + GAP);
            graphics2D.drawLine(x, y, GAP, y);

            // Add Y-axis label
            double value = minValue + (valueRange * i) / Y_HATCH_COUNT;
            String label = String.format("%.1f", value);
            graphics2D.drawString(
                label,
                GAP - 5 - graphics2D.getFontMetrics().stringWidth(label),
                y + 4
            );
        }

        // x hatches, labels, and plot points
        for (int i = 0; i < this.numOfVals; i++) {
            long secondsFromStart = java.time.Duration.between(
                minTime,
                times[i]
            ).getSeconds();
            int x = (int) (secondsFromStart * xScale) + GAP;
            int y1 = this.getHeight() - GAP;
            int y2 = y1 - GRAPH_POINT_WIDTH;

            // Draw x hatch
            graphics2D.drawLine(x, y1, x, y2);

            // Add X-axis label with appropriate format
            String label;
            if (allSameDay) {
                label = times[i].format(timeFormatter);
            } else if (allDifferentDays) {
                label = times[i].format(dateFormatter);
            } else {
                label = times[i].format(dateTimeFormatter);
            }
            graphics2D.drawString(
                label,
                x - graphics2D.getFontMetrics().stringWidth(label) / 2,
                this.getHeight() - GAP + 20
            );

            // Draw connecting line (except for last point)
            if (i > 0) {
                graphics2D.drawLine(
                    graphPoints[i - 1].x,
                    graphPoints[i - 1].y,
                    graphPoints[i].x,
                    graphPoints[i].y
                );
            }

            // Draw point
            int pointX = graphPoints[i].x - GRAPH_POINT_WIDTH / 2;
            int pointY = graphPoints[i].y - GRAPH_POINT_WIDTH / 2;
            graphics2D.fillOval(
                pointX,
                pointY,
                GRAPH_POINT_WIDTH,
                GRAPH_POINT_WIDTH
            );
        }
    }
}
