package sml.plotlib.core

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.io.File
import kotlin.math.abs

/**
 * Renders a Plot to SVG format without any third-party dependencies.
 * Generates clean, standards-compliant SVG XML.
 */
class SVGRenderer(private val plot: Plot) {

    private val marginLeft = 80
    private val marginRight = 160
    private val marginTop = 60
    private val marginBottom = 60

    fun save(path: String, width: Int = 800, height: Int = 600) {
        val svg = buildSVG(width, height)
        File(path).writeText(svg)
        println("Saved plot to $path")
    }

    private fun buildSVG(width: Int, height: Int): String {
        val sb = StringBuilder()

        // SVG header
        sb.appendLine("""<?xml version="1.0" encoding="UTF-8"?>""")
        sb.appendLine("""<svg xmlns="http://www.w3.org/2000/svg" width="$width" height="$height" viewBox="0 0 $width $height">""")
        sb.appendLine("""  <defs>""")

        // Add clip path for plot area
        val plotW = width - marginLeft - marginRight
        val plotH = height - marginTop - marginBottom
        sb.appendLine("""    <clipPath id="plotClip">""")
        sb.appendLine("""      <rect x="$marginLeft" y="$marginTop" width="$plotW" height="$plotH"/>""")
        sb.appendLine("""    </clipPath>""")
        sb.appendLine("""  </defs>""")

        // Background
        sb.appendLine("""  <rect x="0" y="0" width="$width" height="$height" fill="white"/>""")

        if (plotW <= 0 || plotH <= 0) {
            sb.appendLine("</svg>")
            return sb.toString()
        }

        val xMin = plot.xAxis.min
        val xMax = plot.xAxis.max
        val yMin = plot.yAxis.min
        val yMax = plot.yAxis.max
        val originX = marginLeft
        val originY = height - marginBottom

        // Draw axes and labels
        drawAxes(sb, width, height, plotW, plotH, originX, originY, xMin, xMax, yMin, yMax)

        // Coordinate transform
        fun toScreen(x: Double, y: Double): Pair<Double, Double> {
            val sx = (x - xMin) / (xMax - xMin) * plotW + marginLeft
            val sy = height - marginBottom - (y - yMin) / (yMax - yMin) * plotH
            return Pair(sx, sy)
        }

        // Draw all data series
        for (series in plot.getSeries()) {
            val xs = series.x
            val ys = series.y
            val style = series.style

            // Error region (clipped)
            if (series.hasErrorRegion) {
                val yUpper = series.yUpper!!
                val yLower = series.yLower!!

                val pathData = StringBuilder()
                val first = toScreen(xs.first(), yUpper.first())
                pathData.append("M ${first.first} ${first.second}")

                for (i in 1 until xs.size) {
                    val p = toScreen(xs[i], yUpper[i])
                    pathData.append(" L ${p.first} ${p.second}")
                }

                for (i in xs.indices.reversed()) {
                    val p = toScreen(xs[i], yLower[i])
                    pathData.append(" L ${p.first} ${p.second}")
                }
                pathData.append(" Z")

                val fillColor = colorToRGBA(style.color, 60)
                sb.appendLine("""  <path d="$pathData" fill="$fillColor" clip-path="url(#plotClip)"/>""")
            }

            // Line
            if (style.lineWidth > 0) {
                val strokeColor = colorToRGB(style.color)
                val strokeWidth = style.lineWidth
                val dashArray = strokeToDashArray(style.toStroke())

                val pathData = StringBuilder()
                if (xs.isNotEmpty()) {
                    val first = toScreen(xs[0], ys[0])
                    pathData.append("M ${first.first} ${first.second}")

                    for (i in 1 until xs.size) {
                        val p = toScreen(xs[i], ys[i])
                        pathData.append(" L ${p.first} ${p.second}")
                    }
                }

                val dashAttr = if (dashArray.isNotEmpty()) """ stroke-dasharray="$dashArray"""" else ""
                sb.appendLine("""  <path d="$pathData" fill="none" stroke="$strokeColor" stroke-width="$strokeWidth"$dashAttr stroke-linecap="round" stroke-linejoin="round"/>""")
            }

            // Points
            if (style.showPoints) {
                val fillColor = colorToRGB(style.color)
                val r = style.pointRadius.toDouble()
                for (i in xs.indices) {
                    val p = toScreen(xs[i], ys[i])
                    sb.appendLine("""  <circle cx="${p.first}" cy="${p.second}" r="$r" fill="$fillColor"/>""")
                }
            }
        }

        // Legend
        drawLegend(sb, width, height)

        sb.appendLine("</svg>")
        return sb.toString()
    }

    private fun drawAxes(
        sb: StringBuilder,
        width: Int,
        height: Int,
        plotW: Int,
        plotH: Int,
        originX: Int,
        originY: Int,
        xMin: Double,
        xMax: Double,
        yMin: Double,
        yMax: Double
    ) {
        // Border
        sb.appendLine("""  <rect x="$originX" y="$marginTop" width="$plotW" height="$plotH" fill="none" stroke="black" stroke-width="1"/>""")

        // X-axis ticks
        for (tx in plot.xAxis.ticks()) {
            val sx = (tx - xMin) / (xMax - xMin) * plotW + marginLeft
            sb.appendLine("""  <line x1="$sx" y1="$originY" x2="$sx" y2="${originY + 5}" stroke="black" stroke-width="1"/>""")
            val label = "%.2f".format(tx)
            sb.appendLine("""  <text x="$sx" y="${originY + 20}" text-anchor="middle" font-family="SansSerif" font-size="12">$label</text>""")
        }

        // Y-axis ticks
        for (ty in plot.yAxis.ticks()) {
            val sy = height - marginBottom - (ty - yMin) / (yMax - yMin) * plotH
            sb.appendLine("""  <line x1="${originX - 5}" y1="$sy" x2="$originX" y2="$sy" stroke="black" stroke-width="1"/>""")
            val label = "%.2f".format(ty)
            sb.appendLine("""  <text x="${originX - 10}" y="$sy" text-anchor="end" dominant-baseline="middle" font-family="SansSerif" font-size="12">$label</text>""")
        }

        // X-axis label
        val xLabelX = originX + plotW / 2
        val xLabelY = height - 20
        sb.appendLine("""  <text x="$xLabelX" y="$xLabelY" text-anchor="middle" font-family="SansSerif" font-size="14" font-weight="bold">${escapeXML(plot.xlabel)}</text>""")

        // Y-axis label (rotated)
        val yLabelX = 20
        val yLabelY = height / 2
        sb.appendLine("""  <text x="$yLabelX" y="$yLabelY" text-anchor="middle" font-family="SansSerif" font-size="14" font-weight="bold" transform="rotate(-90 $yLabelX $yLabelY)">${escapeXML(plot.ylabel)}</text>""")

        // Title
        val titleX = originX + plotW / 2
        val titleY = 30
        sb.appendLine("""  <text x="$titleX" y="$titleY" text-anchor="middle" font-family="SansSerif" font-size="16" font-weight="bold">${escapeXML(plot.title)}</text>""")
    }

    private fun drawLegend(sb: StringBuilder, width: Int, height: Int) {
        val series = plot.getSeries()
        if (series.isEmpty()) return

        val legendX = width - marginRight + 20
        val legendY = marginTop + 30
        val entryH = 20

        sb.appendLine("""  <text x="$legendX" y="${legendY - 10}" font-family="SansSerif" font-size="12" fill="black">Legend</text>""")

        for ((i, s) in series.withIndex()) {
            val y = legendY + i * entryH
            val strokeColor = colorToRGB(s.style.color)
            val strokeWidth = s.style.lineWidth
            val dashArray = strokeToDashArray(s.style.toStroke())

            // Line sample
            val dashAttr = if (dashArray.isNotEmpty()) """ stroke-dasharray="$dashArray"""" else ""
            sb.appendLine("""  <line x1="$legendX" y1="${y - 5}" x2="${legendX + 30}" y2="${y - 5}" stroke="$strokeColor" stroke-width="$strokeWidth"$dashAttr stroke-linecap="round"/>""")

            // Point sample
            if (s.style.showPoints) {
                sb.appendLine("""  <circle cx="${legendX + 15}" cy="${y - 5}" r="3" fill="$strokeColor"/>""")
            }

            // Label
            sb.appendLine("""  <text x="${legendX + 40}" y="${y + 2}" font-family="SansSerif" font-size="12" fill="black">${escapeXML(s.name)}</text>""")
        }
    }

    // Helper functions

    private fun colorToRGB(color: Color): String {
        return "rgb(${color.red},${color.green},${color.blue})"
    }

    private fun colorToRGBA(color: Color, alpha: Int): String {
        return "rgba(${color.red},${color.green},${color.blue},${alpha / 255.0})"
    }

    private fun strokeToDashArray(stroke: BasicStroke): String {
        val dashArray = stroke.dashArray ?: return ""
        return dashArray.joinToString(" ")
    }

    private fun escapeXML(text: String): String {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }
}
