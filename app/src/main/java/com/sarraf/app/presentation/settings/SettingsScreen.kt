package com.sarraf.app.presentation.settings

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.sarraf.app.core.theme.SurfaceContainerLowest
import com.sarraf.app.core.theme.TertiaryContainer

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SettingsTopBar()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            // رأس الصفحة مع شارة الاتصال والإصدار
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.settings_title),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(EmeraldLight.copy(alpha = 0.15f))
                        .border(1.dp, EmeraldLight.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
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
                        text = "V1.0.0 • CONNECTED",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldLight
                    )
                }
            }

            Text(
                text = stringResource(R.string.settings_header_desc),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // بطاقة المزامنة المباشرة
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceContainer)
                    .border(1.dp, OutlineBorder.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceContainerHigh)
                            .border(1.dp, OutlineBorder.copy(alpha = 0.25f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudSync,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.live_sync_active),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stringResource(R.string.engine_health_format, "99.9%"),
                            fontSize = 12.sp,
                            color = EmeraldLight
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceContainerHighest)
                        .border(1.dp, OutlineBorder.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .clickable { viewModel.refreshData() }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = stringResource(R.string.btn_refresh),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.btn_refresh),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // القسم الأول: الإعدادات العامة والإقليمية
            SettingsSectionHeader(
                title = stringResource(R.string.general_regional_section),
                icon = Icons.Default.Tune
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceContainer)
                    .border(1.dp, OutlineBorder.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // لغة العرض
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.display_language),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stringResource(R.string.language_settings),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceContainerLow)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        LanguageButton(
                            label = "English",
                            badge = "US",
                            isSelected = uiState.language == "en",
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setLanguage("en") }
                        )
                        LanguageButton(
                            label = "العربية",
                            badge = "AR",
                            isSelected = uiState.language == "ar",
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setLanguage("ar") }
                        )
                    }
                }

                // مظهر التطبيق
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.app_theme),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(text = "OLED Active", fontSize = 12.sp, color = EmeraldLight)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceContainerLow)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ThemeOptionButton(
                            label = stringResource(R.string.theme_system),
                            icon = Icons.Default.Smartphone,
                            isSelected = uiState.themeMode == "SYSTEM",
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setTheme("SYSTEM") }
                        )
                        ThemeOptionButton(
                            label = stringResource(R.string.theme_oled),
                            icon = Icons.Default.DarkMode,
                            isSelected = uiState.themeMode == "OLED_DARK",
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setTheme("OLED_DARK") }
                        )
                        ThemeOptionButton(
                            label = stringResource(R.string.theme_light),
                            icon = Icons.Default.LightMode,
                            isSelected = uiState.themeMode == "LIGHT",
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setTheme("LIGHT") }
                        )
                    }
                }

                HorizontalDivider(color = OutlineVariant.copy(alpha = 0.25f), thickness = 1.dp)

                // العملة الأساسية الافتراضية
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SurfaceContainerHighest),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🇺🇸", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.default_base_currency),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            val isAr = uiState.language == "ar"
                            Text(
                                text = if (isAr) "USD ($) • دولار أمريكي" else "USD ($) • United States Dollar",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainerHigh)
                            .border(1.dp, OutlineBorder.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "USD", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                HorizontalDivider(color = OutlineVariant.copy(alpha = 0.25f), thickness = 1.dp)

                // الدقة العشرية
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.decimal_precision),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stringResource(R.string.decimal_precision_desc, 4),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainerLow)
                            .border(1.dp, OutlineBorder.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = "0.0000", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = EmeraldLight)
                    }
                }
            }

            // القسم الثاني: واجهة برمجة الأسعار (API)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Api,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.api_section_title),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.6.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(EmeraldLight))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Active", fontSize = 11.sp, color = EmeraldLight)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceContainer)
                    .border(1.dp, OutlineBorder.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // مزود الخدمة
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(EmeraldLight.copy(alpha = 0.15f))
                                .border(1.dp, EmeraldLight.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Hub,
                                contentDescription = null,
                                tint = EmeraldLight,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.api_provider_title),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = stringResource(R.string.api_tier_desc),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                    Text(
                        text = "LIVE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldLight,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceContainerHigh)
                            .border(1.dp, EmeraldLight.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                // إعداد مفتاح API
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLow)
                        .border(1.dp, OutlineBorder.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.api_key_config),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(EmeraldLight.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = EmeraldLight, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(R.string.verified_badge),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldLight
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = uiState.apiKeyMasked,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceContainerHigh.copy(alpha = 0.6f))
                                .border(1.dp, OutlineBorder.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        )

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceContainerHighest)
                                .border(1.dp, OutlineBorder.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .clickable { }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.ContentPaste, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(R.string.edit_paste_btn),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // فترة التحديث التلقائي
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.auto_refresh_interval),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceContainerLow)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IntervalButton(
                            label = stringResource(R.string.interval_15_min),
                            isSelected = uiState.autoRefreshInterval == "15M",
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setAutoRefreshInterval("15M") }
                        )
                        IntervalButton(
                            label = stringResource(R.string.interval_1_hour),
                            isSelected = uiState.autoRefreshInterval == "1H",
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setAutoRefreshInterval("1H") }
                        )
                        IntervalButton(
                            label = stringResource(R.string.interval_manual),
                            isSelected = uiState.autoRefreshInterval == "MANUAL",
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setAutoRefreshInterval("MANUAL") }
                        )
                    }
                }
            }

            // القسم الثالث: التخزين والذاكرة المؤقتة
            SettingsSectionHeader(
                title = stringResource(R.string.storage_cache_section),
                icon = Icons.Default.Inventory2
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceContainer)
                    .border(1.dp, OutlineBorder.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(SurfaceContainerHigh)
                                .border(1.dp, OutlineBorder.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Inventory2, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.cached_rates_title),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = stringResource(R.string.cache_stats_format, uiState.cachedRatesCount, "${uiState.cachedSizeKb} KB"),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = stringResource(R.string.last_sync_label),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                        val isAr = uiState.language == "ar"
                        val localizedSyncTime = if (isAr) {
                            uiState.lastSyncTime.replace("Today", "اليوم").replace("Yesterday", "أمس").replace("PM", "م").replace("AM", "ص")
                        } else {
                            uiState.lastSyncTime
                        }
                        Text(text = localizedSyncTime, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }

                Button(
                    onClick = { viewModel.clearCache() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.cacheClearSuccess) EmeraldLight.copy(alpha = 0.2f) else TertiaryContainer.copy(alpha = 0.2f),
                        contentColor = if (uiState.cacheClearSuccess) EmeraldLight else RoseLight
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (uiState.cacheClearSuccess) EmeraldLight.copy(alpha = 0.3f) else RoseLight.copy(alpha = 0.3f)
                    )
                ) {
                    Icon(
                        imageVector = if (uiState.cacheClearSuccess) Icons.Default.Check else Icons.Default.DeleteSweep,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    val isAr = uiState.language == "ar"
                    Text(
                        text = if (uiState.cacheClearSuccess) {
                            if (isAr) "تم مسح الذاكرة المؤقتة بنجاح" else "Cache Cleared Successfully"
                        } else {
                            stringResource(R.string.clear_local_cache)
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // القسم الرابع: التنبيهات والاهتزاز
            SettingsSectionHeader(
                title = stringResource(R.string.alerts_haptics_section),
                icon = Icons.Default.Vibration
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceContainer)
                    .border(1.dp, OutlineBorder.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                ToggleSettingsRow(
                    title = stringResource(R.string.push_notifications),
                    subtitle = stringResource(R.string.push_notifications_desc),
                    isChecked = uiState.pushNotifications,
                    onToggle = { viewModel.togglePushNotifications(!uiState.pushNotifications) }
                )

                HorizontalDivider(color = OutlineVariant.copy(alpha = 0.25f), thickness = 1.dp)

                ToggleSettingsRow(
                    title = stringResource(R.string.haptic_feedback),
                    subtitle = stringResource(R.string.haptic_feedback_desc),
                    isChecked = uiState.hapticFeedback,
                    onToggle = { viewModel.toggleHaptics(!uiState.hapticFeedback) }
                )

                HorizontalDivider(color = OutlineVariant.copy(alpha = 0.25f), thickness = 1.dp)

                val isAr = uiState.language == "ar"
                ToggleSettingsRow(
                    title = stringResource(R.string.background_sync),
                    subtitle = if (isAr) "مزامنة أزواج قائمة المراقبة دون الحاجة لفتح التطبيق" else "Synchronize watchlist pairs without opening app",
                    isChecked = uiState.backgroundSync,
                    onToggle = { viewModel.toggleBackgroundSync(!uiState.backgroundSync) }
                )
            }

            // تذييل الصفحة
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "SARRAF • VERSION 1.0.0 (BUILD 108)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.6.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary))
                }

                val isAr = uiState.language == "ar"
                Text(
                    text = if (isAr) {
                        "أسعار الصرف مقدمة لأغراض إعلامية فقط. أسعار السوق بين البنوك دون هوامش ربح التجار."
                    } else {
                        "Exchange rates provided for informational purposes only. Interbank mid-market rates without dealer spreads."
                    },
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(text = if (isAr) "شروط الخدمة" else "Terms of Service", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    Text(text = "•", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                    Text(text = if (isAr) "سياسة الخصوصية" else "Privacy Policy", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    Text(text = "•", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                    Text(text = if (isAr) "حالة الخادم" else "Server Status", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SettingsTopBar() {
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
                    text = stringResource(R.string.settings_title),
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
fun SettingsSectionHeader(title: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.6.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun LanguageButton(
    label: String,
    badge: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = badge,
                fontSize = 10.sp,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f) else MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun ThemeOptionButton(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) SurfaceContainerHighest else Color.Transparent
            )
            .border(
                1.dp,
                if (isSelected) OutlineBorder.copy(alpha = 0.35f) else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun IntervalButton(
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ToggleSettingsRow(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            Text(text = subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
        }

        val bgColor by animateColorAsState(
            targetValue = if (isChecked) EmeraldLight else SurfaceContainerHighest,
            animationSpec = tween(durationMillis = 200),
            label = "ToggleBg"
        )

        Box(
            modifier = Modifier
                .width(48.dp)
                .height(28.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(bgColor)
                .clickable(onClick = onToggle)
                .padding(3.dp),
            contentAlignment = if (isChecked) Alignment.CenterEnd else Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(SurfaceContainerLowest)
            )
        }
    }
}