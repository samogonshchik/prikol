package com.example.prikol

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

enum class PrikolScreens() {
    Home,
    Game,
    Rules,
    Win,
    LearnedWords,
    Statistics
}

@Composable
fun PrikolNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = PrikolScreens.Home.name,
        modifier = modifier
    ) {
        composable(route = PrikolScreens.Home.name) {
            HomeScreen(
                navigateGame = { navController.navigate(PrikolScreens.Game.name) },
                navigateRules = { navController.navigate(PrikolScreens.Rules.name) }
            )
        }
        composable(route = PrikolScreens.Game.name) {
            CrosswordScreen(
                navigateHome = { navController.navigate(PrikolScreens.Home.name) },
                navigateWin = { navController.navigate(PrikolScreens.Win.name) }
            )
        }
        composable(route = PrikolScreens.Rules.name) {
            RulesScreen(
                navigateHome = { navController.navigate(PrikolScreens.Home.name) }
            )
        }
    }
}