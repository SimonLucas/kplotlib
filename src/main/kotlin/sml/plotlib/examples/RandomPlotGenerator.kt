package sml.plotlib.examples

import sml.plotlib.core.*
import kotlin.math.*
import kotlin.random.Random

/**
 * Generates a random plot with different wave parameters each time it runs.
 * Used for the interactive README example.
 */
fun main() {
    val random = Random(System.currentTimeMillis())
    val x = (0..100).map { it / 10.0 }

    // Generate random wave parameters
    val amp1 = random.nextDouble(0.5, 2.0)
    val freq1 = random.nextDouble(0.5, 2.0)
    val phase1 = random.nextDouble(0.0, 2 * PI)
    val y1 = x.map { amp1 * sin(freq1 * it + phase1) }

    val amp2 = random.nextDouble(0.5, 2.0)
    val freq2 = random.nextDouble(0.5, 2.0)
    val phase2 = random.nextDouble(0.0, 2 * PI)
    val y2 = x.map { amp2 * cos(freq2 * it + phase2) }

    val amp3 = random.nextDouble(0.3, 1.5)
    val freq3 = random.nextDouble(0.3, 1.5)
    val y3 = x.map { amp3 * sin(freq3 * it) * cos(freq3 * it / 2) }

    val p = plot("Random Waves (click ▶ to regenerate)") {
        xlabel = "x"
        ylabel = "y"
        line("Wave 1 (A=%.2f, f=%.2f)".format(amp1, freq1), x, y1)
        line("Wave 2 (A=%.2f, f=%.2f)".format(amp2, freq2), x, y2)
        line("Wave 3 (A=%.2f, f=%.2f)".format(amp3, freq3), x, y3)
    }

    p.saveSVG("./docs/img/random_plot.svg")
    println("✓ Generated random_plot.svg with random parameters:")
    println("  Wave 1: amplitude=%.2f, frequency=%.2f".format(amp1, freq1))
    println("  Wave 2: amplitude=%.2f, frequency=%.2f".format(amp2, freq2))
    println("  Wave 3: amplitude=%.2f, frequency=%.2f".format(amp3, freq3))
}
