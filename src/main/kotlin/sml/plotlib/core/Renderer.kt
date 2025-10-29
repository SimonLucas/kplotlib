package sml.plotlib.core

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import java.awt.geom.Path2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel

/**
 * Handles drawing a Plot on a Graphics2D context.
 * Supports antialiasing, shaded error regions (clipped to frame),
 * double-precision drawing, and live resizing.
 */
class Renderer(private val plot: Plot) {

    private val theme = plot.theme
    private val marginLeft get() = theme.margins.left
    private val marginRight get() = theme.margins.right
    private val marginTop get() = theme.margins.top
    private val marginBottom get() = theme.margins.bottom

    // ---------------- Entry points ----------------

    fun display(initialWidth: Int = 800, initialHeight: Int = 600) {
        val frame = JFrame(plot.title)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.contentPane.add(object : JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                // dynamically use current size for resizing
                draw(g as Graphics2D, width, height)
            }
        })
        frame.setSize(initialWidth, initialHeight)
        frame.isVisible = true
    }

    fun save(path: String, width: Int = 800, height: Int = 600) {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g2d = image.createGraphics()
        draw(g2d, width, height)
        g2d.dispose()
        ImageIO.write(image, "png", File(path))
        println("Saved plot to $path")
    }

    // ---------------- Core drawing ----------------

    fun draw(g2d: Graphics2D, width: Int, height: Int) {
        // --- Anti-aliasing and quality hints ---
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

        // --- Background ---
        g2d.color = theme.colors.background
        g2d.fillRect(0, 0, width, height)

        // --- Compute bounds ---
        val plotW = width - marginLeft - marginRight
        val plotH = height - marginTop - marginBottom
        if (plotW <= 0 || plotH <= 0) return

        val xMin = plot.xAxis.min
        val xMax = plot.xAxis.max
        val yMin = plot.yAxis.min
        val yMax = plot.yAxis.max
        val originX = marginLeft
        val originY = height - marginBottom

        // --- Draw axes and labels ---
        drawAxes(g2d, width, height, plotW, plotH, originX, originY, xMin, xMax, yMin, yMax)

        // --- Coordinate transform (double precision) ---
        fun toScreen(x: Double, y: Double): Point2D.Double {
            val sx = ((x - xMin) / (xMax - xMin) * plotW + marginLeft)
            val sy = (height - marginBottom - (y - yMin) / (yMax - yMin) * plotH)
            return Point2D.Double(sx, sy)
        }

        // --- Plot clipping region ---
        val plotClip = Rectangle2D.Double(marginLeft.toDouble(), marginTop.toDouble(), plotW.toDouble(), plotH.toDouble())

        // --- Draw all data series ---
        for (series in plot.getSeries()) {
            val xs = series.x
            val ys = series.y
            val style = series.style

            // ---- Error region (clipped) ----
            if (series.hasErrorRegion) {
                val yUpper = series.yUpper!!
                val yLower = series.yLower!!

                val path = Path2D.Double()
                val first = toScreen(xs.first(), yUpper.first())
                path.moveTo(first.x, first.y)
                for (i in 1 until xs.size) {
                    val p = toScreen(xs[i], yUpper[i])
                    path.lineTo(p.x, p.y)
                }
                for (i in xs.indices.reversed()) {
                    val p = toScreen(xs[i], yLower[i])
                    path.lineTo(p.x, p.y)
                }
                path.closePath()

                val fillColor = Color(
                    style.color.red,
                    style.color.green,
                    style.color.blue,
                    60
                )

                val oldClip = g2d.clip
                g2d.clip(plotClip)
                g2d.color = fillColor
                g2d.fill(path)
                g2d.clip = oldClip
            }

            // ---- Line ----
            g2d.color = style.color
            g2d.stroke = style.toStroke()
            for (i in 1 until xs.size) {
                val p1 = toScreen(xs[i - 1], ys[i - 1])
                val p2 = toScreen(xs[i], ys[i])
                g2d.draw(Line2D.Double(p1, p2))
            }

            // ---- Points ----
            if (style.showPoints) {
                val r = style.pointRadius.toDouble()
                for (i in xs.indices) {
                    val p = toScreen(xs[i], ys[i])
                    g2d.fill(Ellipse2D.Double(p.x - r, p.y - r, 2.0 * r, 2.0 * r))
                }
            }
        }

        // --- Legend ---
        drawLegend(g2d, width, height)
    }

    // ---------------- Axis drawing ----------------

    private fun drawAxes(
        g2d: Graphics2D,
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
        g2d.color = theme.colors.axisBorder
        g2d.stroke = BasicStroke(1f)
        g2d.draw(Rectangle2D.Double(originX.toDouble(), marginTop.toDouble(), plotW.toDouble(), plotH.toDouble()))

        // X-axis ticks
        g2d.color = theme.colors.foreground
        g2d.font = theme.fonts.tickFont()
        val fm = g2d.fontMetrics
        for (tx in plot.xAxis.ticks()) {
            val sx = ((tx - xMin) / (xMax - xMin) * plotW + marginLeft)
            g2d.draw(Line2D.Double(sx, originY.toDouble(), sx, originY + 5.0))
            val label = AxisFormatter.format(tx, theme.axisFormat)
            g2d.drawString(label, (sx - fm.stringWidth(label) / 2).toFloat(), (originY + 20).toFloat())
        }

        // Y-axis ticks
        for (ty in plot.yAxis.ticks()) {
            val sy = (height - marginBottom - (ty - yMin) / (yMax - yMin) * plotH)
            g2d.draw(Line2D.Double(originX - 5.0, sy, originX.toDouble(), sy))
            val label = AxisFormatter.format(ty, theme.axisFormat)
            g2d.drawString(label, (originX - fm.stringWidth(label) - 10).toFloat(), (sy + fm.ascent / 2).toFloat())
        }

        // Axis labels
        g2d.font = theme.fonts.labelFont()
        val fml = g2d.fontMetrics
        g2d.drawString(plot.xlabel, (originX + plotW / 2 - fml.stringWidth(plot.xlabel) / 2).toFloat(), (height - 20).toFloat())
        g2d.rotate(-Math.PI / 2)
        g2d.drawString(plot.ylabel, (-height / 2 - fml.stringWidth(plot.ylabel) / 2).toFloat(), 20f)
        g2d.rotate(Math.PI / 2)

        // Title
        g2d.font = theme.fonts.titleFont()
        val fmt = g2d.fontMetrics
        g2d.drawString(plot.title, (originX + plotW / 2 - fmt.stringWidth(plot.title) / 2).toFloat(), 30f)
    }

    // ---------------- Legend drawing ----------------

    private fun drawLegend(g2d: Graphics2D, width: Int, height: Int) {
        val series = plot.getSeries()
        if (series.isEmpty()) return

        val legendX = width - marginRight + 20
        val legendY = marginTop + 30
        val entryH = 20

        g2d.font = theme.fonts.legendFont()
        val fm = g2d.fontMetrics
        g2d.color = theme.colors.foreground
        g2d.drawString("Legend", legendX.toFloat(), (legendY - 10).toFloat())

        for ((i, s) in series.withIndex()) {
            val y = legendY + i * entryH
            g2d.color = s.style.color
            g2d.stroke = s.style.toStroke()
            g2d.draw(Line2D.Double(legendX.toDouble(), (y - 5).toDouble(), (legendX + 30).toDouble(), (y - 5).toDouble()))
            if (s.style.showPoints) {
                g2d.fill(Ellipse2D.Double((legendX + 12).toDouble(), (y - 8).toDouble(), 6.0, 6.0))
            }
            g2d.color = theme.colors.foreground
            g2d.drawString(s.name, (legendX + 40).toFloat(), (y - 2 + fm.ascent / 2).toFloat())
        }
    }
}