package sml.plotlib.examples

import sml.plotlib.core.PlotStyle
import sml.plotlib.core.Series
import sml.plotlib.core.line
import sml.plotlib.core.plot
import sml.plotlib.core.scatter
import kotlin.math.*

/**
 * Comprehensive example demonstrating SVG export with all plot features:
 * - Multiple line series
 * - Scatter plots
 * - Error regions
 * - Custom colors and styles
 */
fun main() {
    println("Generating comprehensive SVG examples...")

    // Example 1: Multiple line series
    val x1 = (0..100).map { it / 10.0 }
    val y1 = x1.map { sin(it) }
    val y2 = x1.map { cos(it) }
    val y3 = x1.map { sin(it * 2) / 2 }

    val plot1 = plot("Multiple Series - SVG Export") {
        xlabel = "Time (s)"
        ylabel = "Amplitude"
        line("sin(x)", x1, y1)
        line("cos(x)", x1, y2)
        line("sin(2x)/2", x1, y3)
    }

    plot1.save("./docs/img/example-multi-series.svg")
    println("✓ Saved: ./docs/img/example-multi-series.svg")

    // Example 2: Scatter plot
    val x2 = (0..50).map { it / 5.0 }
    val y4 = x2.map { sin(it) + (Math.random() - 0.5) * 0.3 }

    val plot2 = plot("Scatter Plot - SVG Export") {
        xlabel = "x"
        ylabel = "y"
        scatter("noisy data", x2, y4)
    }

    plot2.save("./docs/img/example-scatter.svg")
    println("✓ Saved: ./docs/img/example-scatter.svg")

    // Example 3: Error regions
    val x3 = (0..100).map { it / 10.0 }
    val y5 = x3.map { sin(it) }
    val yLower = x3.map { sin(it) - 0.2 - 0.1 * abs(cos(it)) }
    val yUpper = x3.map { sin(it) + 0.2 + 0.1 * abs(cos(it)) }

    val plot3 = plot("Error Regions - SVG Export") {
        xlabel = "x"
        ylabel = "y ± error"
        addSeries(
            Series(
                name = "sin(x) with uncertainty",
                x = x3,
                y = y5,
                yLower = yLower,
                yUpper = yUpper,
                style = PlotStyle(showPoints = false)
            )
        )
    }

    plot3.save("./docs/img/example-error-regions.svg")
    println("✓ Saved: ./docs/img/example-error-regions.svg")

    // Example 4: Mixed line and points
    val x4 = (0..50).map { it / 5.0 }
    val y6 = x4.map { exp(-it / 5.0) * sin(it) }

    val plot4 = plot("Damped Oscillation - SVG Export") {
        xlabel = "Time"
        ylabel = "Amplitude"
        addSeries(
            Series(
                name = "exp(-t/5) * sin(t)",
                x = x4,
                y = y6,
                style = PlotStyle(showPoints = true, pointRadius = 3)
            )
        )
    }

    plot4.save("./docs/img/example-damped-oscillation.svg")
    println("✓ Saved: ./docs/img/example-damped-oscillation.svg")

    println("\n✅ All SVG examples generated successfully!")
    println("\nFeatures demonstrated:")
    println("  • Multiple line series with legend")
    println("  • Scatter plots with points")
    println("  • Shaded error regions with transparency")
    println("  • Mixed line and point styles")
    println("  • Automatic color cycling")
    println("  • Axis labels and titles")
    println("\n📁 Open the .svg files in a browser or vector editor to view!")
}
