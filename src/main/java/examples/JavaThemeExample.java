package examples;

import sml.plotlib.core.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates using themes from Java to customize plot appearance.
 * Shows how to use predefined themes and create custom themes.
 */
public class JavaThemeExample {

    public static void main(String[] args) {
        System.out.println("=== Java Theme Examples ===\n");

        // Generate sample data once
        List<Double> x = generateXData(0, 100, 100);
        List<Double> y1 = computeQuadratic(x, 0.5);
        List<Double> y2 = computeQuadratic(x, 1.0);
        List<Double> y3 = computeQuadratic(x, 1.5);

        // Example 1: Using predefined themes
        System.out.println("1. Creating plots with predefined themes:");
        createPlotWithTheme("Default Theme", Theme.getDefault(), x, y1, y2, y3);
        createPlotWithTheme("Presentation Theme", Theme.presentation(), x, y1, y2, y3);
        createPlotWithTheme("Paper Theme", Theme.paper(), x, y1, y2, y3);
        createPlotWithTheme("Dark Theme", Theme.dark(), x, y1, y2, y3);
        createPlotWithTheme("Minimal Theme", Theme.minimal(), x, y1, y2, y3);

        // Example 2: Creating a custom theme
        System.out.println("\n2. Creating plot with custom theme:");
        createCustomThemeExample(x, y1, y2, y3);

        // Example 3: Demonstrating clean axis formatting (0 to 2000)
        System.out.println("\n3. Demonstrating clean axis formatting:");
        demonstrateCleanAxisFormatting();

        System.out.println("\n=== All examples completed! ===");
    }

    /**
     * Creates a plot using a specific theme.
     */
    private static void createPlotWithTheme(String name, Theme theme,
                                           List<Double> x,
                                           List<Double> y1,
                                           List<Double> y2,
                                           List<Double> y3) {
        Plot plot = new Plot(
                name,
                "X Values",
                "Y Values",
                theme
        );

        plot.addSeries("Series 1", x, y1, null);
        plot.addSeries("Series 2", x, y2, null);
        plot.addSeries("Series 3", x, y3, null);

        String filename = "java_theme_" + name.toLowerCase().replace(" ", "_") + ".png";
        plot.save(filename, 800, 600);
        System.out.println("  ✓ Saved: " + filename);
    }

    /**
     * Creates a custom theme by modifying an existing theme.
     */
    private static void createCustomThemeExample(List<Double> x,
                                                 List<Double> y1,
                                                 List<Double> y2,
                                                 List<Double> y3) {
        // Start with default theme
        Theme defaultTheme = Theme.getDefault();

        // Create custom font settings
        FontTheme customFonts = defaultTheme.getFonts().copy(
                "Serif",          // family
                20,               // titleSize
                16,               // labelSize
                14,               // tickSize
                13,               // legendSize
                1,                // titleStyle (Font.BOLD)
                0,                // labelStyle (Font.PLAIN)
                0,                // tickStyle
                0                 // legendStyle
        );

        // Create custom color palette
        List<Color> customPalette = new ArrayList<>();
        customPalette.add(new Color(0, 120, 180));      // Deep blue
        customPalette.add(new Color(180, 60, 60));      // Deep red
        customPalette.add(new Color(60, 140, 60));      // Deep green

        ColorTheme customColors = defaultTheme.getColors().copy(
                new Color(245, 245, 240),  // cream background
                Color.BLACK,                // foreground
                new Color(200, 200, 200),  // gridMajor
                new Color(230, 230, 230),  // gridMinor
                Color.BLACK,                // axisBorder
                customPalette               // custom palette
        );

        // Create custom margins for more space
        MarginTheme customMargins = defaultTheme.getMargins().copy(
                100,  // left
                180,  // right
                70,   // top
                70    // bottom
        );

        // Build the custom theme
        Theme customTheme = defaultTheme.copy(
                "custom",
                customFonts,
                customColors,
                customMargins,
                defaultTheme.getGrid(),
                defaultTheme.getAxisFormat()
        );

        // Create plot with custom theme
        Plot plot = new Plot(
                "Custom Java Theme",
                "X Axis",
                "Y Axis",
                customTheme
        );

        plot.addSeries("Data A", x, y1, null);
        plot.addSeries("Data B", x, y2, null);
        plot.addSeries("Data C", x, y3, null);

        plot.save("java_custom_theme.png", 900, 650);
        System.out.println("  ✓ Saved: java_custom_theme.png (with custom fonts, colors, and margins)");
    }

    /**
     * Demonstrates clean axis formatting with data from 0 to 2000.
     * Shows that axes display "2000" instead of "1999.96".
     */
    private static void demonstrateCleanAxisFormatting() {
        Plot plot = new Plot(
                "Clean Axis Formatting Demo",
                "X Range: 0 to 2000",
                "Y Values",
                Theme.getDefault()
        );

        // Generate data from 0 to 2000
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();

        for (int i = 0; i <= 100; i++) {
            double xi = i * 20.0;  // 0, 20, 40, ..., 2000
            x.add(xi);
            y.add(xi * xi / 1000.0);  // Some interesting function
        }

        plot.addSeries("f(x) = x²/1000", x, y, null);

        plot.save("java_clean_axes.png", 800, 600);
        System.out.println("  ✓ Saved: java_clean_axes.png");
        System.out.println("    Note: X-axis shows clean values like '0', '500', '1000', '1500', '2000'");
        System.out.println("          (not floating-point artifacts like '1999.96')");
    }

    // ============ Helper Methods ============

    /**
     * Generate x data from min to max with specified number of points.
     */
    private static List<Double> generateXData(double min, double max, int points) {
        List<Double> x = new ArrayList<>();
        for (int i = 0; i <= points; i++) {
            x.add(min + (max - min) * i / points);
        }
        return x;
    }

    /**
     * Compute y = a * x^2 for each x value.
     */
    private static List<Double> computeQuadratic(List<Double> x, double a) {
        List<Double> y = new ArrayList<>();
        for (double xi : x) {
            y.add(a * xi * xi / 100.0);
        }
        return y;
    }
}
