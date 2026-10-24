package com.sarraf.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sarraf.app.presentation.alerts.AlertsScreen
import com.sarraf.app.presentation.converter.ConverterScreen
import com.sarraf.app.presentation.detail.PairDetailScreen
import com.sarraf.app.presentation.rates.RatesScreen
import com.sarraf.app.presentation.settings.SettingsScreen

sealed class Screen(val route: String) {
    data object Rates : Screen("rates")
    data object Converter : Screen("converter?base={base}&target={target}") {
        fun createRoute(base: String = "USD", target: String = "ILS") = "converter?base=$base&target=$target"
    }
    data object Alerts : Screen("alerts")
    data object Settings : Screen("settings")
    data object PairDetail : Screen("pair_detail/{base}/{target}") {
        fun createRoute(base: String, target: String) = "pair_detail/$base/$target"
    }
}

@Composable
fun SarrafNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Rates.route,
        modifier = modifier
    ) {
        composable(Screen.Rates.route) {
            RatesScreen(
                onNavigateToDetail = { base, target ->
                    navController.navigate(Screen.PairDetail.createRoute(base, target))
                },
                onNavigateToConverter = { base, target ->
                    navController.navigate(Screen.Converter.createRoute(base, target))
                }
            )
        }

        composable(
            route = Screen.Converter.route,
            arguments = listOf(
                navArgument("base") {
                    type = NavType.StringType
                    defaultValue = "USD"
                },
                navArgument("target") {
                    type = NavType.StringType
                    defaultValue = "ILS"
                }
            )
        ) {
            ConverterScreen(
                onNavigateToAlerts = {
                    navController.navigate(Screen.Alerts.route)
                }
            )
        }

        composable(Screen.Alerts.route) {
            AlertsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { base, target ->
                    navController.navigate(Screen.PairDetail.createRoute(base, target))
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        composable(
            route = Screen.PairDetail.route,
            arguments = listOf(
                navArgument("base") { type = NavType.StringType },
                navArgument("target") { type = NavType.StringType }
            )
        ) {
            PairDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToConverter = { base, target ->
                    navController.navigate(Screen.Converter.createRoute(base, target))
                },
                onNavigateToAlerts = {
                    navController.navigate(Screen.Alerts.route)
                }
            )
        }
    }
}
