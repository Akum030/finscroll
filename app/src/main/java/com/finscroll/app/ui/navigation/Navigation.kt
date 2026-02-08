package com.finscroll.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.finscroll.app.ui.screens.HomeScreen
import com.finscroll.app.ui.screens.ProgressScreen
import com.finscroll.app.ui.screens.SearchScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Progress : Screen("progress")
}

@Composable
fun FinScrollNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToProgress = { navController.navigate(Screen.Progress.route) }
            )
        }
        
        composable(Screen.Search.route) {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Progress.route) {
            ProgressScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
