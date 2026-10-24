package com.sarraf.app.presentation.alerts

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.SouthEast
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.sarraf.app.core.theme.PrimaryContainer
import com.sarraf.app.core.theme.PrimaryIndigo
import com.sarraf.app.core.theme.RoseLight
import com.sarraf.app.core.theme.SurfaceContainer
import com.sarraf.app.core.theme.SurfaceContainerHigh
import com.sarraf.app.core.theme.SurfaceContainerHighest
import com.sarraf.app.core.theme.SurfaceContainerLow
import com.sarraf.app.core.theme.SurfaceContainerLowest
import com.sarraf.app.core.theme.TextPrimary
import com.sarraf.app.domain.model.AlertConditionType
import com.sarraf.app.domain.model.DeliveryChannel
import com.sarraf.app.domain.model.RateAlert
import java.util.Locale

@Composable
fun AlertsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AlertsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    if (uiState.showCreateDialog) {
        CreateAlertModalDialog(
            base = uiState.dialogBase,
            target = uiState.dialogTarget,
            price = uiState.dialogTargetPrice,
            condition = uiState.dialogCondition,
            onBaseChange = { viewModel.updateDialogBase(it) },
            onTargetChange = { viewModel.updateDialogTarget(it) },
            onPriceChange = { viewModel.updateDialogTargetPrice(it) },
            onConditionChange = { viewModel.updateDialogCondition(it) },
            onDismiss = { viewModel.showCreateAlert(false) },
            onSave = { viewModel.saveNewAlert() }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AlertsTopBar()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            SmartPriceAlertsHeroCard(
                activeCount = uiState.activeAlerts.count { it.isEnabled },
                selectedChannels = uiState.selectedChannels,
                onToggleChannel = { viewModel.toggleDeliveryChannel(it) }
            )

            CreateNewAlertCtaButton(
                onClick = { viewModel.showCreateAlert(true) }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Radar,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.active_watchlist_alerts),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = stringResource(R.string.real_time_feeds),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.6.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                uiState.activeAlerts.forEach { alert ->
                    ActiveAlertCard(
                        alert = alert,
                        onToggle = { viewModel.toggleAlert(alert.id, alert.isEnabled) },
                        onCardClick = { onNavigateToDetail(alert.baseCode, alert.targetCode) }
                    )
                }
            }

            TriggeredAlertsHistorySection(
                items = uiState.triggeredHistory,
                onClear = { viewModel.clearHistory() }
            )

            ZeroLatencyFeedCard()

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CreateAlertModalDialog(
    base: String,
    target: String,
    price: String,
    condition: AlertConditionType,
    onBaseChange: (String) -> Unit,
    onTargetChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onConditionChange: (AlertConditionType) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AddAlert,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.create_alert_title),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.select_pair_desc),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("ILS" to "USD", "EUR" to "USD", "ILS" to "JOD", "SAR" to "USD").forEach { (b, t) ->
                        val isSel = base == b && target == t
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) MaterialTheme.colorScheme.primary else SurfaceContainerHigh)
                                .clickable {
                                    onBaseChange(b)
                                    onTargetChange(t)
                                }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$b/$t",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = price,
                    onValueChange = onPriceChange,
                    label = { Text(stringResource(R.string.target_exchange_rate_label)) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceContainerHigh,
                        unfocusedContainerColor = SurfaceContainerLow,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = OutlineBorder.copy(alpha = 0.3f),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        AlertConditionType.RISES_ABOVE to stringResource(R.string.condition_rises_above),
                        AlertConditionType.FALLS_BELOW to stringResource(R.string.condition_falls_below),
                        AlertConditionType.VOLATILITY_SWING to stringResource(R.string.condition_swing)
                    ).forEach { (cond, label) ->
                        val isSel = condition == cond
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) MaterialTheme.colorScheme.primaryContainer else SurfaceContainerLow)
                                .clickable { onConditionChange(cond) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onSave,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = stringResource(R.string.save_alert), fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = stringResource(R.string.cancel_action))
            }
        },
        containerColor = SurfaceContainer,
        shape = RoundedCornerShape(18.dp)
    )
}

@Composable
fun AlertsTopBar() {
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
                    text = stringResource(R.string.alerts_title),
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
fun SmartPriceAlertsHeroCard(
    activeCount: Int,
    selectedChannels: Set<DeliveryChannel>,
    onToggleChannel: (DeliveryChannel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainerHigh)
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
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.smart_price_alerts),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(EmeraldLight.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(EmeraldLight)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.active_alerts_count, activeCount),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldLight
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.alerts_hero_desc),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.delivery_channels_label),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 4.dp)
            )

            ChannelPill(
                title = stringResource(R.string.channel_push),
                icon = Icons.Default.NotificationsActive,
                isSelected = selectedChannels.contains(DeliveryChannel.PUSH),
                onClick = { onToggleChannel(DeliveryChannel.PUSH) }
            )

            ChannelPill(
                title = stringResource(R.string.channel_sms),
                icon = Icons.Default.Sms,
                isSelected = selectedChannels.contains(DeliveryChannel.SMS),
                onClick = { onToggleChannel(DeliveryChannel.SMS) }
            )

            ChannelPill(
                title = stringResource(R.string.channel_email),
                icon = Icons.Default.Email,
                isSelected = selectedChannels.contains(DeliveryChannel.EMAIL),
                onClick = { onToggleChannel(DeliveryChannel.EMAIL) }
            )
        }
    }
}

@Composable
fun ChannelPill(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) SurfaceContainerHighest else SurfaceContainer
            )
            .border(
                1.dp,
                if (isSelected) OutlineBorder.copy(alpha = 0.4f) else OutlineBorder.copy(alpha = 0.2f),
                RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun CreateNewAlertCtaButton(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        PrimaryContainer,
                        Color(0xFF494BD6),
                        Color(0xFF2F2EBE)
                    )
                )
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AddAlert,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = stringResource(R.string.create_new_price_alert),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Text(
                    text = stringResource(R.string.create_new_alert_sub),
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun ActiveAlertCard(
    alert: RateAlert,
    onToggle: () -> Unit,
    onCardClick: () -> Unit
) {
    val accentColor = when (alert.conditionType) {
        AlertConditionType.RISES_ABOVE -> EmeraldLight
        AlertConditionType.FALLS_BELOW -> PrimaryIndigo
        AlertConditionType.VOLATILITY_SWING -> RoseLight
    }

    val iconVector = when (alert.conditionType) {
        AlertConditionType.RISES_ABOVE -> Icons.Default.TrendingUp
        AlertConditionType.FALLS_BELOW -> Icons.Default.TrendingDown
        AlertConditionType.VOLATILITY_SWING -> Icons.Default.Vibration
    }

    val titleCondition = when (alert.conditionType) {
        AlertConditionType.RISES_ABOVE -> stringResource(R.string.alert_rises_above, "${String.format(Locale.US, "%.4f", alert.targetRate)} ₪")
        AlertConditionType.FALLS_BELOW -> stringResource(R.string.alert_falls_below, "$${String.format(Locale.US, "%.4f", alert.targetRate)}")
        AlertConditionType.VOLATILITY_SWING -> stringResource(R.string.alert_swing)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainer)
            .border(1.dp, OutlineBorder.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .clickable(onClick = onCardClick)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = alert.baseCode,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(14.dp)
                        )
                        Text(
                            text = alert.targetCode,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = titleCondition,
                        fontSize = 13.sp,
                        color = accentColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            val switchBgColor by animateColorAsState(
                targetValue = if (alert.isEnabled) accentColor else SurfaceContainerHighest,
                animationSpec = tween(durationMillis = 200),
                label = "SwitchBg"
            )

            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(28.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(switchBgColor)
                    .clickable(onClick = onToggle)
                    .padding(3.dp),
                contentAlignment = if (alert.isEnabled) Alignment.CenterEnd else Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainerLowest)
                )
            }
        }

        if (alert.conditionType != AlertConditionType.VOLATILITY_SWING) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceContainerLow)
                    .border(1.dp, OutlineBorder.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = stringResource(R.string.current_label), fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = "${String.format(Locale.US, "%.4f", alert.currentRate)} ₪",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = stringResource(R.string.remaining_format, "0.0155 ₪", 96),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(SurfaceContainerHighest)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.96f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(accentColor)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.reached_format, 96), fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                    Text(text = stringResource(R.string.target_format, "${String.format(Locale.US, "%.4f", alert.targetRate)} ₪"), fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceContainerLow)
                    .border(1.dp, OutlineBorder.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.baseline_label), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "${String.format(Locale.US, "%.4f", alert.currentRate)} ₪",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = stringResource(R.string.volatility_high),
                    fontSize = 11.sp,
                    color = RoseLight,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerHighest)
                        .border(1.dp, RoseLight.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (alert.conditionType == AlertConditionType.VOLATILITY_SWING) Icons.Default.Bolt else Icons.Default.Notifications,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (alert.conditionType == AlertConditionType.VOLATILITY_SWING) {
                        stringResource(R.string.urgent_alert_enabled)
                    } else {
                        stringResource(R.string.trigger_once)
                    },
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { }
            ) {
                Text(text = stringResource(R.string.edit_action), fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun TriggeredAlertsHistorySection(
    items: List<TriggeredAlertHistoryItem>,
    onClear: () -> Unit
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.triggered_alerts_history),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = stringResource(R.string.clear_log),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable(onClick = onClear)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceContainerLow)
                .border(1.dp, OutlineBorder.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                val localizedTitle = when (item.title) {
                    "Target Price Hit" -> if (isAr) "تم الوصول للسعر المستهدف" else item.title
                    "Dip Alert Triggered" -> if (isAr) "تم تفعيل تنبيه الهبوط" else item.title
                    else -> item.title
                }

                val localizedDate = when (item.dateLabel) {
                    "Today" -> if (isAr) "اليوم" else item.dateLabel
                    "Yesterday" -> if (isAr) "أمس" else item.dateLabel
                    else -> item.dateLabel
                }

                val localizedMessage = if (isAr) {
                    when {
                        item.message.startsWith("Crossed target") ->
                            item.message.replace("Crossed target", "تجاوز الهدف").replace("and reached", "ووصل إلى")
                        item.message.startsWith("Fell below") ->
                            item.message.replace("Fell below", "هبط عن").replace("to", "إلى")
                        else -> item.message
                    }
                } else {
                    item.message
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainer)
                        .border(1.dp, OutlineBorder.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(
                                    if (item.isPositive) EmeraldLight.copy(alpha = 0.15f)
                                    else RoseLight.copy(alpha = 0.15f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (item.isPositive) Icons.Default.CheckCircle else Icons.Default.SouthEast,
                                contentDescription = null,
                                tint = if (item.isPositive) EmeraldLight else RoseLight,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = localizedTitle,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = item.pairBadge,
                                    fontSize = 10.sp,
                                    color = if (item.isPositive) EmeraldLight else PrimaryIndigo,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(SurfaceContainerHighest)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = localizedMessage,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = localizedDate, fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                        Text(text = item.timeLabel, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun ZeroLatencyFeedCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainerLowest)
            .border(1.dp, OutlineBorder.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.VerifiedUser,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = stringResource(R.string.zero_latency_title),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.zero_latency_desc),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}