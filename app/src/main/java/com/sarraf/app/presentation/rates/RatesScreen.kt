package com.sarraf.app.presentation.rates

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarraf.app.R
import com.sarraf.app.core.theme.EmeraldLight
import com.sarraf.app.core.theme.OutlineBorder
import com.sarraf.app.core.theme.OutlineVariant
import com.sarraf.app.core.theme.RoseLight
import com.sarraf.app.core.theme.SurfaceContainer
import com.sarraf.app.core.theme.SurfaceContainerHigh
import com.sarraf.app.core.theme.SurfaceContainerHighest
import com.sarraf.app.core.theme.SurfaceContainerLow
import com.sarraf.app.core.theme.TextPrimary
import com.sarraf.app.core.theme.TextTertiary
import com.sarraf.app.domain.model.CurrencyRate
import com.sarraf.app.presentation.components.SparklineChart
import java.util.Locale

@Composable
fun RatesScreen(
    onNavigateToDetail: (String, String) -> Unit,
    onNavigateToConverter: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RatesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isSearching = uiState.searchQuery.isNotBlank()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // الشريط العلوي
        RatesTopBar(
            onSearchClick = {},
            onProfileClick = {}
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                LiveStatusStrip()
            }

            item {
                BaseCurrencySegmentSection(
                    selectedBase = uiState.baseCurrency,
                    options = viewModel.baseCurrencies,
                    onSelect = { viewModel.setBaseCurrency(it) }
                )
            }

            if (!isSearching) {
                item {
                    KeyWatchlistGridSection(
                        baseCurrency = uiState.baseCurrency,
                        watchlistRates = uiState.watchlistRates,
                        onItemClick = { targetCode ->
                            onNavigateToDetail(uiState.baseCurrency, targetCode)
                        }
                    )
                }
            }

            item {
                RatesSearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChanged(it) },
                    onClearQuery = { viewModel.clearSearch() }
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isSearching) {
                            stringResource(R.string.matching_currencies, uiState.regionalAndGlobalRates.size)
                        } else {
                            stringResource(R.string.regional_global_rates)
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.against_currency_format, uiState.baseCurrency),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (uiState.regionalAndGlobalRates.isEmpty()) {
                item {
                    NoCurrenciesFoundEmptyState(
                        query = uiState.searchQuery,
                        onClear = { viewModel.clearSearch() }
                    )
                }
            } else {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfaceContainer)
                            .border(1.dp, OutlineBorder.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                    ) {
                        uiState.regionalAndGlobalRates.forEachIndexed { index, rateItem ->
                            RateListRow(
                                rate = rateItem,
                                onRowClick = {
                                    onNavigateToDetail(uiState.baseCurrency, rateItem.code)
                                },
                                onConvertClick = {
                                    onNavigateToConverter(uiState.baseCurrency, rateItem.code)
                                }
                            )
                            if (index < uiState.regionalAndGlobalRates.lastIndex) {
                                HorizontalDivider(
                                    color = SurfaceContainerHighest.copy(alpha = 0.4f),
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (!isSearching) {
                item {
                    AiInsightCard()
                    Spacer(modifier = Modifier.height(24.dp))
                }
            } else {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun RatesTopBar(
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(64.dp)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = " | ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = OutlineVariant
                )
                Text(
                    text = stringResource(R.string.rates_title),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(EmeraldLight)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "LIVE • SYNCED",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    color = EmeraldLight
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable(onClick = onProfileClick),
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
}

@Composable
fun LiveStatusStrip() {
    val infiniteTransition = rememberInfiniteTransition(label = "SyncRotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceContainerLow)
            .border(1.dp, OutlineBorder.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
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
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.live_updates_format, 3),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = EmeraldLight
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Sync,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(rotation)
            )
            Text(
                text = stringResource(R.string.auto_sync),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun BaseCurrencySegmentSection(
    selectedBase: String,
    options: List<BaseCurrencyOption>,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.base_comparison_currency),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.6.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(R.string.switch_basket),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceContainerLow)
                .border(1.dp, OutlineBorder.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            options.forEach { option ->
                val isSelected = selectedBase.equals(option.code, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else SurfaceContainerHigh.copy(alpha = 0.6f)
                        )
                        .clickable { onSelect(option.code) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = option.flag, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = option.code,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = " ${option.symbolFormatted}",
                            fontSize = 11.sp,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KeyWatchlistGridSection(
    baseCurrency: String,
    watchlistRates: List<CurrencyRate>,
    onItemClick: (String) -> Unit
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
                text = stringResource(R.string.key_watchlist_pairs),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.6.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(R.string.active_volume),
                fontSize = 12.sp,
                color = TextTertiary
            )
        }

        val itemsToDisplay = watchlistRates.take(4)
        val row1 = itemsToDisplay.take(2)
        val row2 = itemsToDisplay.drop(2).take(2)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            row1.forEach { item ->
                WatchlistTile(
                    rate = item,
                    baseCurrency = baseCurrency,
                    modifier = Modifier.weight(1f),
                    onClick = { onItemClick(item.code) }
                )
            }
        }

        if (row2.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row2.forEach { item ->
                    WatchlistTile(
                        rate = item,
                        baseCurrency = baseCurrency,
                        modifier = Modifier.weight(1f),
                        onClick = { onItemClick(item.code) }
                    )
                }
            }
        }
    }
}

@Composable
fun WatchlistTile(
    rate: CurrencyRate,
    baseCurrency: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isPositive = rate.change24h >= 0

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainer)
            .border(1.dp, OutlineBorder.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = rate.flag, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "${rate.code} / $baseCurrency",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = getLocalizedCurrencyName(rate.code, rate.name),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isPositive) EmeraldLight.copy(alpha = 0.15f)
                        else RoseLight.copy(alpha = 0.15f)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isPositive) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = if (isPositive) EmeraldLight else RoseLight,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = String.format(Locale.US, "%+.2f%%", rate.change24h),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPositive) EmeraldLight else RoseLight
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = String.format(Locale.US, "%.4f", rate.rate),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = rate.symbol,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.outline
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        SparklineChart(
            data = if (rate.sparklineData.isNotEmpty()) rate.sparklineData else listOf(0f, 1f, 0.5f, 0.8f),
            isPositive = isPositive,
            height = 28.dp,
            strokeWidth = 3.5f
        )
    }
}

@Composable
fun RatesSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = stringResource(R.string.search_currency_hint),
                fontSize = 14.sp,
                color = TextTertiary
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = TextTertiary
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearQuery) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear search",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice",
                    tint = TextTertiary
                )
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SurfaceContainer,
            unfocusedContainerColor = SurfaceContainerLow,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = OutlineBorder.copy(alpha = 0.25f),
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    )
}

@Composable
fun NoCurrenciesFoundEmptyState(
    query: String,
    onClear: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainer)
            .border(1.dp, OutlineBorder.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(SurfaceContainerHigh),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.no_currencies_found),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = stringResource(R.string.no_currencies_found_desc, query),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )
        Button(
            onClick = onClear,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text(text = stringResource(R.string.clear_search), fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun RateListRow(
    rate: CurrencyRate,
    onRowClick: () -> Unit,
    onConvertClick: () -> Unit
) {
    val isPositive = rate.change24h >= 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onRowClick)
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(SurfaceContainerHigh)
                    .border(1.dp, OutlineBorder.copy(alpha = 0.25f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = rate.flag, fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = rate.code,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = " · ${getLocalizedCurrencyName(rate.code, rate.name)}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                Text(
                    text = when (rate.subtitle) {
                        "Pegged rate" -> stringResource(R.string.pegged_rate)
                        "Parallel market spread" -> stringResource(R.string.parallel_market_spread)
                        else -> rate.subtitle.ifEmpty { stringResource(R.string.mid_market_default_subtitle) }
                    },
                    fontSize = 12.sp,
                    color = if (rate.change24h < -0.5) RoseLight else MaterialTheme.colorScheme.outline
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = String.format(Locale.US, "%.4f", rate.rate),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = String.format(Locale.US, "%+.2f%%", rate.change24h),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPositive) EmeraldLight else RoseLight
                )
            }

            IconButton(
                onClick = onConvertClick,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(SurfaceContainerHighest)
                    .border(1.dp, OutlineBorder.copy(alpha = 0.3f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = "Convert",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun AiInsightCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    listOf(SurfaceContainer, SurfaceContainerHigh)
                )
            )
            .border(1.dp, OutlineBorder.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                .border(1.dp, OutlineBorder.copy(alpha = 0.25f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Insights,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.ai_insight_title),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.recommendation_badge),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldLight,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(EmeraldLight.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
            Text(
                text = stringResource(R.string.ai_insight_content),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// دالة مساعدة لتعريب أسماء العملات تلقائياً
@Composable
private fun getLocalizedCurrencyName(code: String, defaultName: String): String {
    val isAr = Locale.getDefault().language == "ar"
    if (!isAr) return defaultName
    return when (code.uppercase()) {
        "USD" -> "دولار أمريكي"
        "EUR" -> "يورو"
        "ILS" -> "شيكل"
        "JOD" -> "دينار أردني"
        "SAR" -> "ريال سعودي"
        "AED" -> "درهم إماراتي"
        "EGP" -> "جنيه مصري"
        "KWD" -> "دينار كويتي"
        "QAR" -> "ريال قطري"
        "GBP" -> "جنيه إسترليني"
        "TRY" -> "ليرة تركية"
        else -> defaultName
    }
}