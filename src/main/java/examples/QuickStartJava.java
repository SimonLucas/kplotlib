package examples;

import sml.plotlib.core.Plot;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal "Quick Start" example for Java developers.
 * Shows the simplest way to create and save a plot from Java.
 */
public class QuickStartJava {

    public static void main(String[] args) {
        // Create a new plot
        Plot plot = new Plot("My First Plot", "X", "Y");

        // Prepare some data
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();

        for (int i = 0; i <= 100; i++) {
            double xi = i / 10.0;  // 0.0 to 10.0
            x.add(xi);
            y.add(xi * xi);  // y = x²
        }

        // Add data to plot
        plot.addSeries("x²", x, y, null);

        // Save to file
        plot.save("quickstart_java.svg");

        System.out.println("✓ Plot saved to: quickstart_java.png");
        System.out.println("  That's it! Just 3 steps: create, add data, save.");

        // To display in a window instead, use:
        plot.show();
    }
}
