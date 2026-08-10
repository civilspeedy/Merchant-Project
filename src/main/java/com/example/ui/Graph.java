package com.example.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.Arrays;
import javax.swing.JPanel;
import lombok.NonNull;
import lombok.val;

/**
 * Based off https://stackoverflow.com/a/8693635
 * Graph
 */
public class Graph extends JPanel {

    private static final Dimension SIZE = new Dimension(800, 650);
    private static final int GAP = 30;
    private static final int GRAPH_POINT_WIDTH = 12;
    private static final int Y_HATCH_COUNT = 10;

    private double[] values;
    private double maxValue;
    private int numOfVals;

    public Graph(@NonNull double[] values) {
        this.values = values;
        this.maxValue = Arrays.stream(values).max().getAsDouble();
        this.numOfVals = values.length;
        this.setPreferredSize(SIZE);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        val xScale =
            ((double) this.getWidth() - 2 * GAP) / (this.numOfVals - 1);
        val yScale =
            ((double) this.getHeight() - 2 * GAP) / (this.maxValue - 1);

        val graphPoints = new Point[this.numOfVals];
        for (int i = 0; i < this.numOfVals; i++) {
            int x = (int) (i * xScale * GAP);
            int y = (int) ((this.maxValue - this.values[i]) * yScale + GAP);
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

        // y hatches
        for (int i = 1; i <= Y_HATCH_COUNT; i++) {
            int x = GRAPH_POINT_WIDTH + GAP;
            int y =
                this.getHeight() -
                ((i * (this.getHeight() - GAP * 2)) / Y_HATCH_COUNT + GAP);
            graphics2D.drawLine(x, y, GAP, y);
        }

        // x hatches
        for (int i = 1; i <= this.numOfVals - 1; i++) {
            int x =
                (i * (this.getWidth() - GAP * 2)) / (this.numOfVals - 1) + GAP;
            int y1 = this.getHeight() - GAP;
            int y2 = y1 - GRAPH_POINT_WIDTH;
            graphics2D.drawLine(x, y1, x, y2);
        }

        // plot points
        for (int i = 0; i < graphPoints.length - 1; i++) {
            int x1 = graphPoints[i].x;
            int y1 = graphPoints[i].y;
            int x2 = graphPoints[i + 1].x;
            int y2 = graphPoints[i + 1].y;
            graphics2D.drawLine(x1, y1, x2, y2);
        }

        for (int i = 0; i < this.numOfVals; i++) {
            int x = graphPoints[i].x - GRAPH_POINT_WIDTH / 2;
            int y = graphPoints[i].y - GRAPH_POINT_WIDTH / 2;
            graphics2D.fillOval(x, y, GRAPH_POINT_WIDTH, GRAPH_POINT_WIDTH);
        }
    }
}
