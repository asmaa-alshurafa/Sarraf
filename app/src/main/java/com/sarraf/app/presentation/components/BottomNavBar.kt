package com.sarraf.app.presentation.components

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sarraf.app.R
import com.sarraf.app.core.theme.OutlineVariant
import com.sarraf.app.presentation.navigation.Screen

sealed class BottomNavItem(
    val route: String,
    @StringRes val titleRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Rates : BottomNavItem(
        route = Screen.Rates.route,
        titleRes = R.string.nav_rates,
        selectedIcon = Icons.Filled.TrendingUp,
        unselectedIcon = Icons.Outlined.TrendingUp
    )
    data object Converter : BottomNavItem(
        route = Screen.Converter.route,
        titleRes = R.string.nav_converter,
        selectedIcon = Icons.Filled.CurrencyExchange,
        unselectedIcon = Icons.Outlined.CurrencyExchange
    )
    data object Alerts : BottomNavItem(
        route = Screen.Alerts.route,
        titleRes = R.string.nav_alerts,
        selectedIcon = Icons.Filled.NotificationsActive,
        unselectedIcon = Icons.Outlined.NotificationsActive
    )
    data object Settings : BottomNavItem(
        route = Screen.Settings.route,
        titleRes = R.string.nav_settings,
        selectedIcon = Icons.Filled.Tune,
        unselectedIcon = Icons.Outlined.Tune
    )
}

@Composable
fun SarrafBottomNavBar(
    currentRoute: String?,
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Rates,
        BottomNavItem.Converter,
        BottomNavItem.Alerts,
        BottomNavItem.Settings
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
            .border(width = 1.dp, color = OutlineVariant.copy(alpha = 0.25f))
            .navigationBarsPadding()
            .height(64.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route
                val tint by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    animationSpec = tween(durationMillis = 200),
                    label = "BottomNavTint"
                )
                val itemTitle = stringResource(item.titleRes)

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onNavigateToRoute(item.route) }
                        )
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = itemTitle,
                        tint = tint,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = itemTitle,
                        color = tint,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}