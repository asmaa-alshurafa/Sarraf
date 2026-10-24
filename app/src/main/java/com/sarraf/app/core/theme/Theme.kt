package com.sarraf.app.core.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// تعريف أنماط الثيم الثلاثة المتوافقة مع أزرار الإعدادات
enum class AppThemeMode {
    SYSTEM,
    OLED_DARK,
    LIGHT
}

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryIndigoLight,
    onPrimary = OnPrimaryIndigo,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = EmeraldLight,
    onSecondary = SecondaryContainer,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = RoseLight,
    onTertiary = TertiaryContainer,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    background = BackgroundOled,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceContainerHighest,
    onSurfaceVariant = TextSecondary,
    outline = OutlineMuted,
    outlineVariant = OutlineVariant,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,
    surfaceBright = SurfaceBright
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryIndigo,
    onPrimary = TextPrimary,
    primaryContainer = PrimaryIndigoLight,
    onPrimaryContainer = OnPrimaryIndigo,
    secondary = EmeraldPositive,
    onSecondary = TextPrimary,
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceContainerHighLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = OutlineMuted,
    outlineVariant = OutlineVariant
)

@Composable
fun SarrafTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // تحديد ما إذا كان المظهر داكناً بناءً على اختيار المستخدم
    val isDark = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.OLED_DARK -> true
        AppThemeMode.LIGHT -> false
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        isDark -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                // جعل لون الشريطين يتغير تلقائياً مع لون خلفية الثيم المحدد
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()

                val windowInsetsController = WindowCompat.getInsetsController(window, view)
                // إذا كان الثيم فاتح، الأيقونات تكون داكنة والعكس
                windowInsetsController.isAppearanceLightStatusBars = !isDark
                windowInsetsController.isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SarrafTypography,
        content = content
    )
}