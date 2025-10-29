package examples;

import sml.plotlib.core.Plot;
import sml.plotlib.core.PlotStyle;
import sml.plotlib.core.Series;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple example of using kplotlib from Java.
 * Demonstrates basic plotting functionality with clean, readable Java code.
 */
public class SimpleJavaPlot {

    public static void main(String[] args) {
        System.out.println("=== Simple Java Plot Example ===\n");

        // Example 1: Basic plot with sine and cosine
        System.out.println("Creating sine and cosine plot...");
        createBasicPlot();

        // Example 2: Plot with custom colors
        System.out.println("Creating plot with custom colors...");
        createCustomColorPlot();

        System.out.println("\nAll plots saved successfully!");
        System.out.println("Note: Plots will display in separate windows.");
    }

    /**
     * Creates a basic plot with sine and cosine curves.
     */
    private static void createBasicPlot() {
        // Create a new plot
        Plot plot = new Plot(
                "Trigonometric Functions",  // title
                "x (radians)",                // x-label
                "f(x)"                        // y-label
        );

        // Generate x values from 0 to 2π
        List<Double> x = new ArrayList<>();
        for (int i = 0; i <= 100; i++) {
            x.add(i * 2 * Math.PI / 100.0);
        }

        // Generate sine values
        List<Double> ySin = new ArrayList<>();
        for (double xi : x) {
            ySin.add(Math.sin(xi));
        }

        // Generate cosine values
        List<Double> yCos = new ArrayList<>();
        for (double xi : x) {
            yCos.add(Math.cos(xi));
        }

        // Add series to plot (library will auto-assign colors)
        plot.addSeries("sin(x)", x, ySin, null);
        plot.addSeries("cos(x)", x, yCos, null);

        // Save the plot
        plot.save("java_basic_plot.png", 800, 600);
        System.out.println("  ✓ Saved: java_basic_plot.png");

        // Uncomment to display plot in a window
        // plot.show();
    }

    /**
     * Creates a plot with custom colors and styling.
     */
    private static void createCustomColorPlot() {
        Plot plot = new Plot(
                "Exponential Growth",
                "Time",
                "Value"
        );

        // Generate data
        List<Double> x = new ArrayList<>();
        List<Double> yLinear = new ArrayList<>();
        List<Double> yExponential = new ArrayList<>();

        for (int i = 0; i <= 50; i++) {
            double xi = i / 5.0;  // 0 to 10
            x.add(xi);
            yLinear.add(xi * 10);
            yExponential.add(Math.exp(xi / 3.0));
        }

        // Create series with custom colors
        PlotStyle blueStyle = new PlotStyle(
                Color.BLUE,      // color
                3.0f,           // line width
                false,          // show points
                3               // point radius
        );

        PlotStyle redStyle = new PlotStyle(
                new Color(220, 50, 50),  // custom red color
                3.0f,
                true,           // show points
                4
        );

        // Add series with explicit styles
        plot.addSeries(new Series(
                "Linear",
                toMutableList(x),
                toMutableList(yLinear),
                blueStyle,
                null,
                null
        ));

        plot.addSeries(new Series(
                "Exponential",
                toMutableList(x),
                toMutableList(yExponential),
                redStyle,
                null,
                null
        ));

        // Save the plot
        plot.save("java_custom_colors.png", 800, 600);
        System.out.println("  ✓ Saved: java_custom_colors.png");
    }

    /**
     * Helper method to convert List<Double> to MutableList (required by Kotlin API).
     */
    private static List<Double> toMutableList(List<Double> list) {
        return new ArrayList<>(list);
    }
}
