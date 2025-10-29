package sml.plotlib.core

import java.awt.Color
import java.awt.Font
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.round

/**
 * Font configuration for plot elements.
 */
data class FontTheme(
    val family: String = "SansSerif",
    val titleSize: Int = 16,
    val titleStyle: Int = Font.BOLD,
    val labelSize: Int = 14,
    val labelStyle: Int = Font.BOLD,
    val tickSize: Int = 12,
    val tickStyle: Int = Font.PLAIN,
    val legendSize: Int = 12,
    val legendStyle: Int = Font.PLAIN
) {
    fun titleFont() = Font(family, titleStyle, titleSize)
    fun labelFont() = Font(family, labelStyle, labelSize)
    fun tickFont() = Font(family, tickStyle, tickSize)
    fun legendFont() = Font(family, legendStyle, legendSize)
}

/**
 * Color scheme for plot elements.
 */
data class ColorTheme(
    val background: Color = Color.WHITE,
    val foreground: Color = Color.BLACK,
    val gridMajor: Color = Color(200, 200, 200),
    val gridMinor: Color = Color(230, 230, 230),
    val axisBorder: Color = Color.BLACK,
    // Default color palette for series
    val palette: List<Color> = listOf(
        Color(31, 119, 180),    // Blue
        Color(255, 127, 14),    // Orange
        Color(44, 160, 44),     // Green
        Color(214, 39, 40),     // Red
        Color(148, 103, 189),   // Purple
        Color(140, 86, 75),     // Brown
        Color(227, 119, 194),   // Pink
        Color(127, 127, 127)    // Gray
    )
) {
    /**
     * Get color from palette by index, cycling if needed.
     */
    fun getSeriesColor(index: Int): Color {
        return palette[index % palette.size]
    }
}

/**
 * Margin configuration for plot layout.
 */
data class MarginTheme(
    val left: Int = 80,
    val right: Int = 160,
    val top: Int = 60,
    val bottom: Int = 60
)

/**
 * Grid configuration for plot background.
 */
data class GridTheme(
    val showMajor: Boolean = true,
    val showMinor: Boolean = false,
    val majorLineWidth: Float = 1.0f,
    val minorLineWidth: Float = 0.5f
)

/**
 * Axis formatting configuration.
 */
data class AxisFormatTheme(
    val autoCleanNumbers: Boolean = true,
    val maxDecimalPlaces: Int = 2,
    val scientificNotationThreshold: Double = 1e6,
    val showTrailingZeros: Boolean = false
)

/**
 * Complete theme combining all style elements.
 */
data class Theme(
    val name: String = "default",
    val fonts: FontTheme = FontTheme(),
    val colors: ColorTheme = ColorTheme(),
    val margins: MarginTheme = MarginTheme(),
    val grid: GridTheme = GridTheme(),
    val axisFormat: AxisFormatTheme = AxisFormatTheme()
) {
    companion object {
        /**
         * Default theme with standard styling.
         */
        @JvmStatic
        fun getDefault() = Theme(name = "default")

        /**
         * Theme optimized for presentations (larger fonts, higher contrast).
         */
        @JvmStatic
        fun presentation() = Theme(
            name = "presentation",
            fonts = FontTheme(
                titleSize = 20,
                labelSize = 18,
                tickSize = 14,
                legendSize = 14
            ),
            margins = MarginTheme(
                left = 100,
                right = 180,
                top = 80,
                bottom = 80
            )
        )

        /**
         * Theme optimized for academic papers (smaller, publication-ready).
         */
        @JvmStatic
        fun paper() = Theme(
            name = "paper",
            fonts = FontTheme(
                family = "Serif",
                titleSize = 14,
                labelSize = 12,
                tickSize = 10,
                legendSize = 10
            ),
            colors = ColorTheme(
                background = Color.WHITE,
                palette = listOf(
                    Color.BLACK,
                    Color(100, 100, 100),
                    Color(150, 150, 150)
                )
            ),
            margins = MarginTheme(
                left = 70,
                right = 140,
                top = 50,
                bottom = 50
            )
        )

        /**
         * Dark theme for low-light viewing.
         */
        @JvmStatic
        fun dark() = Theme(
            name = "dark",
            colors = ColorTheme(
                background = Color(30, 30, 30),
                foreground = Color(224, 224, 224),
                gridMajor = Color(80, 80, 80),
                gridMinor = Color(50, 50, 50),
                axisBorder = Color(150, 150, 150),
                palette = listOf(
                    Color(102, 194, 255),   // Light blue
                    Color(255, 170, 102),   // Light orange
                    Color(102, 255, 153),   // Light green
                    Color(255, 102, 102),   // Light red
                    Color(186, 153, 255),   // Light purple
                    Color(255, 204, 153),   // Light brown
                    Color(255, 153, 204),   // Light pink
                    Color(170, 170, 170)    // Light gray
                )
            )
        )

        /**
         * Minimal theme with reduced visual elements.
         */
        @JvmStatic
        fun minimal() = Theme(
            name = "minimal",
            margins = MarginTheme(
                left = 60,
                right = 120,
                top = 40,
                bottom = 50
            ),
            grid = GridTheme(
                showMajor = false,
                showMinor = false
            )
        )
    }
}

/**
 * Utility for formatting axis tick labels with clean numbers.
 */
object AxisFormatter {
    /**
     * Format a tick value to a clean string representation.
     * Handles floating point artifacts (e.g., 1999.96 -> "2000").
     *
     * @param value The tick value to format
     * @param format The formatting configuration
     * @return Clean formatted string
     */
    fun format(value: Double, format: AxisFormatTheme = AxisFormatTheme()): String {
        if (!format.autoCleanNumbers) {
            return "%.${format.maxDecimalPlaces}f".format(value)
        }

        // Handle special cases
        if (value.isNaN()) return "NaN"
        if (value.isInfinite()) return if (value > 0) "∞" else "-∞"

        // Use scientific notation for very large/small numbers
        if (abs(value) >= format.scientificNotationThreshold) {
            return "%.${format.maxDecimalPlaces}e".format(value)
        }

        // Check if value is effectively an integer (within floating point tolerance)
        val rounded = round(value)
        if (abs(value - rounded) < 1e-9 * maxOf(1.0, abs(value))) {
            return rounded.toInt().toString()
        }

        // Determine appropriate decimal places based on magnitude
        val magnitude = if (value != 0.0) log10(abs(value)).toInt() else 0
        val decimalPlaces = when {
            abs(value) >= 1000 -> 0
            abs(value) >= 100 -> 1
            abs(value) >= 10 -> 2
            abs(value) >= 1 -> 2
            else -> minOf(format.maxDecimalPlaces, 3)
        }

        // Format with appropriate decimal places
        val formatted = "%.${decimalPlaces}f".format(value)

        // Remove trailing zeros unless requested
        return if (!format.showTrailingZeros) {
            formatted.trimEnd('0').trimEnd('.')
        } else {
            formatted
        }
    }

    /**
     * Generate clean tick values that avoid floating point artifacts.
     *
     * @param min Minimum axis value
     * @param max Maximum axis value
     * @param count Desired number of ticks (actual count may vary slightly)
     * @return List of clean tick values
     */
    fun generateCleanTicks(min: Double, max: Double, count: Int = 6): List<Double> {
        if (min == max) return listOf(min)

        val range = max - min

        // Calculate a "nice" step size
        val roughStep = range / (count - 1)
        val magnitude = 10.0.pow(log10(roughStep).toInt())

        // Round step to nice values (1, 2, 5, 10, 20, 50, etc.)
        val normalizedStep = roughStep / magnitude
        val niceStep = when {
            normalizedStep <= 1.0 -> 1.0
            normalizedStep <= 2.0 -> 2.0
            normalizedStep <= 5.0 -> 5.0
            else -> 10.0
        } * magnitude

        // Generate ticks starting from a clean value
        val startTick = (min / niceStep).let {
            if (it < 0) it.toInt().toDouble() else it.toInt().toDouble()
        } * niceStep

        val ticks = mutableListOf<Double>()
        var tick = startTick
        while (tick <= max + niceStep * 0.01) {  // Small tolerance for floating point
            if (tick >= min - niceStep * 0.01) {
                // Round to avoid floating point artifacts
                val cleanTick = round(tick / magnitude) * magnitude
                ticks.add(cleanTick)
            }
            tick += niceStep
        }

        return ticks.ifEmpty { listOf(min, max) }
    }
}
