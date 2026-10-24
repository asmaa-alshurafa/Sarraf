package com.sarraf.app.presentation.detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarraf.app.core.theme.EmeraldLight
import com.sarraf.app.core.theme.OutlineBorder
import com.sarraf.app.core.theme.OutlineVariant
import com.sarraf.app.core.theme.PrimaryIndigo
import com.sarraf.app.core.theme.RoseLight
import com.sarraf.app.core.theme.SurfaceContainer
import com.sarraf.app.core.theme.SurfaceContainerHigh
import com.sarraf.app.core.theme.SurfaceContainerHighest
import com.sarraf.app.core.theme.SurfaceContainerLow
import com.sarraf.app.core.theme.TextPrimary
import com.sarraf.app.core.theme.TextSecondary
import com.sarraf.app.core.theme.TextTertiary
import java.util.Locale

@Composable
fun PairDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToConverter: (String, String) -> Unit,
    onNavigateToAlerts: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PairDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Pair Detail Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(64.dp)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                .border(width = 1.dp, color = OutlineVariant.copy(alpha = 0.2f))
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pair Detail",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            // Pair Header Overview Card
            PairHeaderOverviewCard(
                base = uiState.baseCurrency,
                target = uiState.targetCurrency,
                spotRate = uiState.spotRate,
                changePercent = uiState.changePercent24h,
                changeAbsolute = uiState.changeAbsolute24h,
                low24h = uiState.low24h,
                high24h = uiState.high24h,
                isFavorite = uiState.isFavorite,
                onToggleFavorite = { viewModel.toggleFavorite() }
            )

            // Interactive Timeframe Selector
            TimeframeSelector(
                selected = uiState.selectedTimeframe,
                onSelect = { viewModel.selectTimeframe(it) }
            )

            // Main Interactive Bezier Chart Card
            InteractiveDetailChartCard(
                timeframe = uiState.selectedTimeframe,
                dataPoints = uiState.chartDataPoints,
                currentRate = uiState.spotRate,
                targetCurrency = uiState.targetCurrency
            )

            // Market Statistics Grid (2x2)
            MarketStatisticsGrid(
                volume = uiState.volume24h,
                volumeChange = uiState.volumeChange,
                range52w = uiState.range52w,
                previousClose = uiState.previousClose,
                rsi = uiState.rsiValue,
                rsiStatus = uiState.rsiStatus,
                targetCode = uiState.targetCurrency
            )

            // Fast Mini Instant Conversion Widget
            InstantConversionWidget(
                baseCode = uiState.baseCurrency,
                targetCode = uiState.targetCurrency,
                inputAmount = uiState.instantConvertInput,
                resultAmount = uiState.instantConvertResult
            )

            // Bottom CTA Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { onNavigateToConverter(uiState.baseCurrency, uiState.targetCurrency) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.CurrencyExchange,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Convert ${uiState.baseCurrency} to ${uiState.targetCurrency} Now",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = {
                        viewModel.toggleAlert()
                        if (!uiState.isAlertSet) {
                            onNavigateToAlerts()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder.copy(alpha = 0.35f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (uiState.isAlertSet) EmeraldLight else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(
                        imageVector = if (uiState.isAlertSet) Icons.Default.NotificationsActive else Icons.Default.AddAlert,
                        contentDescription = null,
                        tint = if (uiState.isAlertSet) EmeraldLight else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (uiState.isAlertSet) "Target Alert Active" else "Set Price Target Alert",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun PairHeaderOverviewCard(
    base: String,
    target: String,
    spotRate: Double,
    changePercent: Double,
    changeAbsolute: Double,
    low24h: Double,
    high24h: Double,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    val isPositive = changePercent >= 0
    val targetSymbol = if (target == "ILS") "₪" else if (target == "EUR") "€" else "$"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainer)
            .border(1.dp, OutlineBorder.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(modifier = Modifier.padding(end = 8.dp)) {
                    Text(text = "🇺🇸", fontSize = 20.sp)
                    Text(text = "🇵🇸", fontSize = 20.sp)
                }
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$base / $target",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "LIVE MARKET",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldLight,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(EmeraldLight.copy(alpha = 0.15f))
                                .border(1.dp, EmeraldLight.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = "US Dollar vs Israeli Shekel",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SurfaceContainerHigh)
                    .border(1.dp, OutlineBorder.copy(alpha = 0.3f), CircleShape)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) EmeraldLight else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {                Text(
                    text = String.format(Locale.US, "%.4f", spotRate),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = " $targetSymbol",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isPositive) EmeraldLight.copy(alpha = 0.15f)
                        else RoseLight.copy(alpha = 0.15f)
                    )
                    .border(
                        1.dp,
                        if (isPositive) EmeraldLight.copy(alpha = 0.3f) else RoseLight.copy(alpha = 0.3f),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = if (isPositive) EmeraldLight else RoseLight,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = String.format(Locale.US, "%+.2f%% (%+.4f %s Today)", changePercent, changeAbsolute, targetSymbol),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isPositive) EmeraldLight else RoseLight
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 24h High / Low Bar Tracker
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceContainerLow.copy(alpha = 0.7f))
                .border(1.dp, OutlineBorder.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Text(text = "Low: ", fontSize = 12.sp, color = RoseLight)
                    Text(text = String.format(Locale.US, "%.4f", low24h), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                }
                Text(
                    text = "24H RANGE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.6.sp,
                    color = MaterialTheme.colorScheme.outline
                )
                Row {
                    Text(text = "High: ", fontSize = 12.sp, color = EmeraldLight)
                    Text(text = String.format(Locale.US, "%.4f", high24h), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(SurfaceContainerHighest)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.72f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(EmeraldLight, PrimaryIndigo)
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun TimeframeSelector(
    selected: String,
    onSelect: (String) -> Unit
) {
    val options = listOf("1D", "1W", "1M", "1Y", "ALL")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceContainerLow)
            .border(1.dp, OutlineBorder.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        options.forEach { tf ->
            val isSelected = selected == tf
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    )
                    .clickable { onSelect(tf) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tf,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun InteractiveDetailChartCard(
    timeframe: String,
    dataPoints: List<Float>,
    currentRate: Double,
    targetCurrency: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainer)
            .border(1.dp, OutlineBorder.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(EmeraldLight)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Live $timeframe Movement",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceContainerHighest)
                    .border(1.dp, OutlineBorder.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "${String.format(Locale.US, "%.4f", currentRate)} ₪ at 14:30 GMT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EmeraldLight
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Large Detail Canvas
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            if (dataPoints.size < 2) return@Canvas
            val w = size.width
            val h = size.height

            val minVal = dataPoints.minOrNull() ?: 0f
            val maxVal = dataPoints.maxOrNull() ?: 1f
            val range = (maxVal - minVal).coerceAtLeast(0.0001f)

            // Grid lines
            for (level in listOf(0.25f, 0.5f, 0.75f)) {
                val yLevel = h * level
                drawLine(
                    color = OutlineVariant.copy(alpha = 0.25f),
                    start = androidx.compose.ui.geometry.Offset(0f, yLevel),
                    end = androidx.compose.ui.geometry.Offset(w, yLevel),
                    strokeWidth = 1f
                )
            }

            val points = dataPoints.mapIndexed { index, value ->
                val x = (index.toFloat() / (dataPoints.size - 1)) * w
                val normalizedY = 1f - ((value - minVal) / range)
                val y = 10f + (normalizedY * (h - 20f))
                x to y
            }

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

            val fillPath = Path().apply {
                addPath(strokePath)
                lineTo(points.last().first, h)
                lineTo(points.first().first, h)
                close()
            }

            // Fill gradient
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        EmeraldLight.copy(alpha = 0.38f),
                        EmeraldLight.copy(alpha = 0.08f),
                        Color.Transparent
                    )
                )
            )

            // Stroke line
            drawPath(
                path = strokePath,
                brush = Brush.horizontalGradient(
                    listOf(PrimaryIndigo, EmeraldLight)
                ),
                style = Stroke(width = 6f, cap = StrokeCap.Round)
            )

            // Active end cursor dot
            val last = points.last()
            drawCircle(
                color = EmeraldLight,
                radius = 7f,
                center = androidx.compose.ui.geometry.Offset(last.first, last.second)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // X-axis timeline labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Today").forEach { label ->
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = if (label == "Today") FontWeight.Bold else FontWeight.Normal,
                    color = if (label == "Today") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
fun MarketStatisticsGrid(
    volume: String,
    volumeChange: String,
    range52w: String,
    previousClose: Double,
    rsi: Double,
    rsiStatus: String,
    targetCode: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Market Statistics",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "AUTO-UPDATED",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.6.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MarketStatCard(
                title = "24h Volume",
                value = volume,
                subtitle = volumeChange,
                isPositive = true,
                icon = Icons.Default.BarChart,
                modifier = Modifier.weight(1f)
            )
            MarketStatCard(
                title = "52-Week Range",
                value = range52w,
                subtitle = "$targetCode per USD",
                isPositive = null,
                icon = Icons.Default.DateRange,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MarketStatCard(
                title = "Previous Close",
                value = String.format(Locale.US, "%.4f", previousClose),
                subtitle = "Yesterday 22:00 GMT",
                isPositive = null,
                icon = Icons.Default.History,
                modifier = Modifier.weight(1f)
            )
            MarketStatCard(
                title = "RSI Momentum (14)",
                value = "$rsi",
                subtitle = "Balanced market conditions",
                isPositive = null,
                badge = rsiStatus,
                icon = Icons.Default.Speed,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun MarketStatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    isPositive: Boolean? = null,
    badge: String? = null
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainer)
            .border(1.dp, OutlineBorder.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (badge != null) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = badge,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Text(
            text = subtitle,
            fontSize = 11.sp,
            color = if (isPositive == true) EmeraldLight else MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun InstantConversionWidget(
    baseCode: String,
    targetCode: String,
    inputAmount: Double,
    resultAmount: Double
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainerLow)
            .border(1.dp, OutlineBorder.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Instant Conversion",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(EmeraldLight)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Live rate applied",
                    fontSize = 11.sp,
                    color = EmeraldLight
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceContainer)
                    .border(1.dp, OutlineBorder.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🇺🇸", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$ ${inputAmount.toInt()}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(text = "Input", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceContainer)
                    .border(1.dp, OutlineBorder.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🇵🇸", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = String.format(Locale.US, "%.2f", resultAmount),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldLight
                    )
                }
                Text(text = "ILS ₪", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}
