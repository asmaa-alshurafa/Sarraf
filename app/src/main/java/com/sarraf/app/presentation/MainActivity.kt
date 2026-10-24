package com.sarraf.app.presentation

import android.app.LocaleManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sarraf.app.core.theme.AppThemeMode
import com.sarraf.app.core.theme.SarrafTheme
import com.sarraf.app.presentation.components.SarrafBottomNavBar
import com.sarraf.app.presentation.navigation.SarrafNavGraph
import com.sarraf.app.presentation.navigation.Screen
import com.sarraf.app.presentation.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
            val context = LocalContext.current

            // 1. تطبيق لغة التطبيق رسمياً عبر LocaleManager عند تغييرها بأمان
            LaunchedEffect(uiState.language) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val localeManager = context.getSystemService(LocaleManager::class.java)
                    localeManager?.applicationLocales = LocaleList.forLanguageTags(uiState.language)
                } else {
                    val locale = Locale(uiState.language)
                    Locale.setDefault(locale)
                    val resources = context.resources
                    val config = resources.configuration
                    config.setLocale(locale)
                    @Suppress("DEPRECATION")
                    resources.updateConfiguration(config, resources.displayMetrics)
                }
            }

            // 2. تحويل الثيم
            val currentThemeMode = when (uiState.themeMode.lowercase()) {
                "light" -> AppThemeMode.LIGHT
                "oled_dark", "dark", "oled" -> AppThemeMode.OLED_DARK
                else -> AppThemeMode.SYSTEM
            }

            // 3. تحديد الاتجاه
            val layoutDirection = if (uiState.language == "ar") {
                LayoutDirection.Rtl
            } else {
                LayoutDirection.Ltr
            }

            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                SarrafTheme(themeMode = currentThemeMode) {
                    SarrafAppContent()
                }
            }
        }
    }
}

@Composable
fun SarrafAppContent() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Rates.route,
        Screen.Converter.route,
        Screen.Alerts.route,
        Screen.Settings.route
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        bottomBar = {
            if (showBottomBar) {
                SarrafBottomNavBar(
                    currentRoute = currentRoute,
                    onNavigateToRoute = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Rates.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        SarrafNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}