package com.sarraf.app.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sarraf.app.core.theme.EmeraldLight
import com.sarraf.app.core.theme.RoseLight

@Composable
fun SparklineChart(
    data: List<Float>,
    isPositive: Boolean,
    modifier: Modifier = Modifier,
    height: Dp = 28.dp,
    strokeWidth: Float = 4f,
    showGradientFill: Boolean = true,
    showEndDot: Boolean = true
) {
    val lineColor = if (isPositive) EmeraldLight else RoseLight

    val gradientBrush = remember(lineColor) {
        Brush.verticalGradient(
            colors = listOf(
                lineColor.copy(alpha = 0.35f),
                lineColor.copy(alpha = 0.05f),
                Color.Transparent
            )
        )
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        if (data.size < 2) return@Canvas

        val width = size.width
        val canvasHeight = size.height

        val minVal = data.minOrNull() ?: 0f
        val maxVal = data.maxOrNull() ?: 1f
        val range = (maxVal - minVal).coerceAtLeast(0.0001f)

        val padding = 6f
        val usableHeight = canvasHeight - (padding * 2)

        val points = data.mapIndexed { index, value ->
            val x = (index.toFloat() / (data.size - 1)) * width
            val normalizedY = 1f - ((value - minVal) / range)
            val y = padding + (normalizedY * usableHeight)
            x to y
        }

        // Build smooth cubic Bezier path
        val strokePath = Path().apply {
            moveTo(points.first().first, points.first().second)
            for (i in 1 until points.size) {
                val prev = points[i - 1]
                val curr = points[i]
                val cpx1 = prev.first + (curr.first - prev.first) / 2f
                val cpy1 = prev.second
                val cpx2 = prev.first + (curr.first - prev.first) / 2f
                val cpy2 = curr.second
                cubicTo(cpx1, cpy1, cpx2, cpy2, curr.first, curr.second)
            }
        }

        // Draw Area Fill Gradient
        if (showGradientFill) {
            val fillPath = Path().apply {
                addPath(strokePath)
                lineTo(points.last().first, canvasHeight)
                lineTo(points.first().first, canvasHeight)
                close()
            }
            drawPath(path = fillPath, brush = gradientBrush)
        }

        // Draw Stroke Line
        drawPath(
            path = strokePath,
            color = lineColor,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Draw End Indicator Dot
        if (showEndDot && points.isNotEmpty()) {
            val lastPoint = points.last()
            drawCircle(
                color = lineColor,
                radius = strokeWidth * 1.5f,
                center = androidx.compose.ui.geometry.Offset(lastPoint.first, lastPoint.second)
            )
        }
    }
}
