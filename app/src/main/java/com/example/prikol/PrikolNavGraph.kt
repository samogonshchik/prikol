package com.example.prikol

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
                navigateWin = { wordList ->
                    // Serialize the list to JSON
                    val jsonList = Gson().toJson(wordList)
                    // Encode to handle special characters
                    val encodedJson = java.net.URLEncoder.encode(jsonList, "UTF-8")
                    navController.navigate("${PrikolScreens.Win.name}/$encodedJson")
                }
            )
        }
        composable(route = PrikolScreens.Rules.name) {
            RulesScreen(
                navigateHome = { navController.navigate(PrikolScreens.Home.name) }
            )
        }
        composable(
            route = "${PrikolScreens.Win.name}/{wordList}",
            arguments = listOf(
                navArgument("wordList") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            // Deserialize the JSON back to List<String>
            val jsonList = backStackEntry.arguments?.getString("wordList")
            val wordList = jsonList?.let {
                try {
                    val decodedJson = java.net.URLDecoder.decode(it, "UTF-8")
                    Gson().fromJson(decodedJson, object : TypeToken<List<String>>() {}.type)
                } catch (e: Exception) {
                    emptyList<String>()
                }
            } ?: emptyList()
            WinScreen(
                placedWords = wordList,
                navigateHome = { navController.navigate(PrikolScreens.Home.name) }
            )
        }
    }
}