package sml.plotlib.examples

import sml.plotlib.core.Plot
import sml.plotlib.core.Theme

/**
 * Demonstrates:
 * 1. Clean axis formatting (no more 1999.96 instead of 2000)
 * 2. Theme system for easy style customization
 */
fun main() {
    println("=== Example 1: Clean Axis Formatting ===")
    println("Data range: 0 to 2000")
    println("Old behavior: axis would show 1999.96")
    println("New behavior: axis shows clean 2000\n")

    // Create plot with data from 0 to 2000
    val plot1 = Plot(
        title = "Clean Axis Formatting Demo",
        xlabel = "X Values (0 to 2000)",
        ylabel = "Y Values",
        theme = Theme.getDefault()
    )

    // Generate data from 0 to 2000
    val x1 = (0..100).map { it * 20.0 }  // 0, 20, 40, ..., 2000
    val y1 = x1.map { it * it / 1000.0 }  // Quadratic

    plot1.addSeries("f(x) = x²/1000", x1, y1)
    plot1.save("docs/img/plot1_clean_axes.svg")
    println("✓ Saved docs/img/plot1_clean_axes.svg")
    println("  Notice: X-axis shows 0, 500, 1000, 1500, 2000 (not 1999.96!)\n")

    // ===== Example 2: Compare Different Themes =====
    println("=== Example 2: Theme Comparison ===")

    val themes = listOf(
        Theme.getDefault() to "default",
        Theme.presentation() to "presentation",
        Theme.paper() to "paper",
        Theme.dark() to "dark",
        Theme.minimal() to "minimal"
    )

    // Data for demonstration
    val x = (0..50).map { it * 2.0 }  // 0 to 100
    val y1Series = x.map { it }          // Linear
    val y2Series = x.map { it * 1.5 }    // Linear scaled
    val y3Series = x.map { it * it / 50.0 }  // Quadratic

    for ((theme, name) in themes) {
        val plot = Plot(
            title = "${name.capitalize()} Theme Example",
            xlabel = "X Values",
            ylabel = "Y Values",
            theme = theme
        )

        plot.addSeries("Linear", x, y1Series)
        plot.addSeries("Linear ×1.5", x, y2Series)
        plot.addSeries("Quadratic", x, y3Series)

        plot.save("docs/img/plot_theme_$name.svg")
        println("✓ Saved docs/img/plot_theme_$name.svg")
    }

    println("\n=== Example 3: Custom Theme ===")

    // Create a custom theme
    val customTheme = Theme.getDefault().copy(
        name = "custom",
        fonts = Theme.getDefault().fonts.copy(
            titleSize = 22,
            labelSize = 16
        ),
        colors = Theme.getDefault().colors.copy(
            background = java.awt.Color(250, 250, 245),  // Cream background
            palette = listOf(
                java.awt.Color(0, 100, 200),      // Custom blue
                java.awt.Color(200, 50, 50),      // Custom red
                java.awt.Color(50, 150, 50)       // Custom green
            )
        ),
        margins = Theme.getDefault().margins.copy(
            left = 90,
            right = 170
        )
    )

    val plot3 = Plot(
        title = "Custom Theme with Large Fonts",
        xlabel = "Time (seconds)",
        ylabel = "Value",
        theme = customTheme
    )

    val time = (0..100).map { it.toDouble() }
    val signal = time.map { kotlin.math.sin(it * 0.1) * 50 + 50 }

    plot3.addSeries("Signal", time, signal)
    plot3.save("docs/img/plot_custom_theme.svg")
    println("✓ Saved docs/img/plot_custom_theme.svg")

    println("\n=== Example 4: Large Number Range ===")
    println("Testing clean formatting with large numbers")

    val plot4 = Plot(
        title = "Large Number Formatting",
        xlabel = "X (thousands)",
        ylabel = "Y (millions)",
        theme = Theme.getDefault()
    )

    val xLarge = (0..20).map { it * 100.0 }  // 0 to 2000
    val yLarge = xLarge.map { it * it }      // 0 to 4,000,000

    plot4.addSeries("Growth", xLarge, yLarge)
    plot4.save("docs/img/plot_large_numbers.svg")
    println("✓ Saved docs/img/plot_large_numbers.svg")
    println("  Notice: Clean integer formatting throughout\n")

    println("=== All examples completed! ===")
    println("\nKey improvements:")
    println("1. ✓ Axis labels show clean numbers (2000, not 1999.96)")
    println("2. ✓ Theme system allows easy customization")
    println("3. ✓ Multiple predefined themes available")
    println("4. ✓ Create custom themes by copying and modifying")
}
