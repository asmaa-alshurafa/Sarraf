package com.sarraf.app.presentation.converter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
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
import com.sarraf.app.core.theme.SurfaceContainer
import com.sarraf.app.core.theme.SurfaceContainerHigh
import com.sarraf.app.core.theme.SurfaceContainerHighest
import com.sarraf.app.core.theme.SurfaceContainerLow
import com.sarraf.app.core.theme.TextPrimary
import com.sarraf.app.presentation.components.CustomKeypad
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun ConverterScreen(
    onNavigateToAlerts: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ConverterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val animatedRotation by animateFloatAsState(
        targetValue = uiState.swapRotation,
        animationSpec = tween(durationMillis = 400),
        label = "SwapRotation"
    )

    LaunchedEffect(uiState.isCopied) {
        if (uiState.isCopied) {
            delay(1800)
            viewModel.resetCopyState()
        }
    }

    if (uiState.showCurrencyPicker) {
        CurrencyPickerDialog(
            title = if (uiState.pickerTargetField == "FROM") {
                stringResource(R.string.select_base_currency)
            } else {
                stringResource(R.string.select_target_currency)
            },
            currencies = viewModel.availableCurrencies,
            onSelect = { viewModel.selectCurrencyFromPicker(it) },
            onDismiss = { viewModel.closeCurrencyPicker() }
        )
    }

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            ConverterTopBar()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Spacer(modifier = Modifier.height(2.dp))

                MidMarketStatusBadge()

                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        CurrencyFromCard(
                            currency = uiState.fromCurrency,
                            amount = uiState.inputAmountStr,
                            onPillClick = { viewModel.openCurrencyPicker("FROM") }
                        )

                        CurrencyToCard(
                            currency = uiState.toCurrency,
                            convertedAmount = uiState.calculatedResult,
                            isCopied = uiState.isCopied,
                            onPillClick = { viewModel.openCurrencyPicker("TO") },
                            onCopyClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText(
                                    "Sarraf Converted Amount",
                                    String.format(Locale.US, "%.2f", uiState.calculatedResult)
                                )
                                clipboard.setPrimaryClip(clip)
                                viewModel.onCopyResult()
                            }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                            .clickable { viewModel.swapCurrencies() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = "Swap Currencies",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .size(24.dp)
                                .rotate(animatedRotation)
                        )
                    }
                }

                LiveQuoteSubCard(
                    fromCode = uiState.fromCurrency,
                    toCode = uiState.toCurrency,
                    exchangeRate = uiState.exchangeRate,
                    inverseRate = uiState.inverseRate,
                    changeToday = uiState.rateChangeToday
                )

                QuickAddPills(
                    onAdd = { viewModel.onQuickAdd(it) }
                )

                CustomKeypad(
                    onDigitClick = { viewModel.onDigitPressed(it) },
                    onBackspaceClick = { viewModel.onBackspacePressed() },
                    isHapticEnabled = uiState.isHapticEnabled
                )

                Button(
                    onClick = onNavigateToAlerts,
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
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.set_price_alert),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                RecentConversionsSection(
                    items = uiState.recentConversions
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLow)
                        .border(1.dp, OutlineBorder.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.converter_rate_disclaimer),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.outline,
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        AnimatedVisibility(
            visible = uiState.isCopied,
            enter = fadeIn(tween(150)),
            exit = fadeOut(tween(200)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(SurfaceContainerHighest)
                    .border(1.dp, EmeraldLight.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = EmeraldLight,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.copied_to_clipboard),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = EmeraldLight
                )
            }
        }
    }
}

@Composable
fun CurrencyPickerDialog(
    title: String,
    currencies: List<CurrencyPickerOption>,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.outline)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                currencies.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceContainerHigh)
                            .clickable { onSelect(item.code) }
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = item.flag, fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = item.code, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text(text = getCurrencyName(item.code), fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                            }
                        }
                        Text(text = item.symbol, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {},
        containerColor = SurfaceContainer,
        shape = RoundedCornerShape(18.dp)
    )
}

@Composable
fun ConverterTopBar() {
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
                    text = stringResource(R.string.converter_title),
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
                onClick = {},
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
}

@Composable
fun MidMarketStatusBadge() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceContainerLow)
            .border(1.dp, OutlineBorder.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = EmeraldLight,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.real_mid_market_rate),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = stringResource(R.string.zero_commission),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = EmeraldLight,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(EmeraldLight.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun CurrencyFromCard(
    currency: String,
    amount: String,
    onPillClick: () -> Unit
) {
    val flag = getCurrencyFlag(currency)
    val symbol = getCurrencySymbol(currency)
    val name = getCurrencyName(currency)

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
            Text(
                text = stringResource(R.string.convert_from_base),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.6.sp,
                color = MaterialTheme.colorScheme.outline
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceContainerHighest)
                    .border(1.dp, OutlineBorder.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .clickable(onClick = onPillClick)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = currency, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = flag, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$symbol ",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = amount,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = (-0.02).sp
                )
                Box(
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .width(2.dp)
                        .height(26.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = name, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = currency, fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}

@Composable
fun CurrencyToCard(
    currency: String,
    convertedAmount: Double,
    isCopied: Boolean,
    onPillClick: () -> Unit,
    onCopyClick: () -> Unit
) {
    val flag = getCurrencyFlag(currency)
    val symbol = getCurrencySymbol(currency)
    val name = getCurrencyName(currency)

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
            Text(
                text = stringResource(R.string.convert_to_target),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.6.sp,
                color = MaterialTheme.colorScheme.outline
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceContainerHighest)
                    .border(1.dp, OutlineBorder.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .clickable(onClick = onPillClick)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = currency, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = flag, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = String.format(Locale.US, "%,.2f", convertedAmount),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EmeraldLight,
                    letterSpacing = (-0.02).sp
                )
                Text(
                    text = " $symbol",
                    fontSize = 20.sp,
                    color = EmeraldLight,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                IconButton(
                    onClick = onCopyClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainerHigh)
                        .border(1.dp, OutlineBorder.copy(alpha = 0.25f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = if (isCopied) EmeraldLight else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainerHigh)
                        .border(1.dp, OutlineBorder.copy(alpha = 0.25f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LiveQuoteSubCard(
    fromCode: String,
    toCode: String,
    exchangeRate: Double,
    inverseRate: Double,
    changeToday: Double
) {
    val isAr = Locale.getDefault().language == "ar"
    val fromName = if (isAr) getCurrencyName(fromCode) else fromCode
    val toName = if (isAr) getCurrencyName(toCode) else toCode

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceContainerLow)
            .border(1.dp, OutlineBorder.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = null,
                    tint = EmeraldLight,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "1 $fromCode = ${String.format(Locale.US, "%.4f", exchangeRate)} $toCode",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "${String.format(Locale.US, "%+.2f%%", changeToday)} ${stringResource(R.string.today_suffix)}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldLight,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(EmeraldLight.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${stringResource(R.string.inverse_rate_label)} 1 $toName = ${String.format(Locale.US, "%.4f", inverseRate)} $fromName",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = stringResource(R.string.updated_just_now),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun QuickAddPills(
    onAdd: (Double) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = stringResource(R.string.quick_add_amount),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.6.sp,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(100.0, 250.0, 500.0, 1000.0).forEach { amount ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceContainer)
                        .border(1.dp, OutlineBorder.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                        .clickable { onAdd(amount) }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+$${amount.toInt()}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun RecentConversionsSection(
    items: List<RecentConversionItem>
) {
    val isAr = Locale.getDefault().language == "ar"

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.recent_conversions),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.view_all_history),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { }
            )
        }

        items.forEach { item ->
            val localizedSubtitle = if (isAr) {
                val fromName = getCurrencyName(item.fromCode)
                val toName = getCurrencyName(item.toCode)
                "$fromName إلى $toName"
            } else {
                item.subtitle
            }

            val localizedTime = if (isAr) {
                item.timeLabel
                    .replace("Today", "اليوم")
                    .replace("Yesterday", "أمس")
                    .replace("AM", "ص")
                    .replace("PM", "م")
            } else {
                item.timeLabel
            }

            val localizedStatus = if (isAr && item.status.equals("Success", ignoreCase = true)) {
                "ناجحة"
            } else {
                item.status
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceContainer)
                    .border(1.dp, OutlineBorder.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
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
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(SurfaceContainerHighest)
                            .border(1.dp, OutlineBorder.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = item.flag, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${item.fromCode} ${item.fromAmount.toInt()}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier
                                    .padding(horizontal = 6.dp)
                                    .size(14.dp)
                            )
                            Text(
                                text = "${item.toCode} ${String.format(Locale.US, "%.2f", item.toAmount)}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldLight
                            )
                        }
                        Text(
                            text = localizedSubtitle,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = localizedTime,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = localizedStatus,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldLight
                    )
                }
            }
        }
    }
}

@Composable
private fun getCurrencyName(code: String): String {
    val isAr = Locale.getDefault().language == "ar"
    return when (code.uppercase()) {
        "USD" -> if (isAr) "دولار أمريكي" else "US Dollar"
        "EUR" -> if (isAr) "يورو" else "Euro"
        "ILS" -> if (isAr) "شيكل" else "Israeli Shekel"
        "JOD" -> if (isAr) "دينار أردني" else "Jordanian Dinar"
        "SAR" -> if (isAr) "ريال سعودي" else "Saudi Riyal"
        "AED" -> if (isAr) "درهم إماراتي" else "UAE Dirham"
        "EGP" -> if (isAr) "جنيه مصري" else "Egyptian Pound"
        "KWD" -> if (isAr) "دينار كويتي" else "Kuwaiti Dinar"
        "QAR" -> if (isAr) "ريال قطري" else "Qatari Riyal"
        "GBP" -> if (isAr) "جنيه إسترليني" else "British Pound"
        "TRY" -> if (isAr) "ليرة تركية" else "Turkish Lira"
        else -> code
    }
}

private fun getCurrencyFlag(currency: String): String = when (currency.uppercase()) {
    "USD" -> "🇺🇸"
    "EUR" -> "🇪🇺"
    "ILS" -> "🇵🇸"
    "JOD" -> "🇯🇴"
    "SAR" -> "🇸🇦"
    "AED" -> "🇦🇪"
    "EGP" -> "🇪🇬"
    "KWD" -> "🇰🇼"
    "QAR" -> "🇶🇦"
    "GBP" -> "🇬🇧"
    "TRY" -> "🇹🇷"
    else -> "🌐"
}

private fun getCurrencySymbol(currency: String): String = when (currency.uppercase()) {
    "USD" -> "$"
    "EUR" -> "€"
    "ILS" -> "₪"
    "JOD" -> "د.أ"
    "SAR" -> "ر.س"
    "AED" -> "د.إ"
    "EGP" -> "ج.م"
    "KWD" -> "د.ك"
    "QAR" -> "ر.ق"
    "GBP" -> "£"
    "TRY" -> "₺"
    else -> ""
}