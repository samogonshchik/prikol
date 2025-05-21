package com.example.crosswordapp

import CrosswordScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.crosswordapp.ui.screens.HomeScreen
import com.example.crosswordapp.ui.screens.LearnedWordsScreen
import com.example.crosswordapp.ui.screens.WinScreen
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URLEncoder

enum class CrosswordAppScreens() {
    Home,
    Game,
    Win,
    LearnedWords,
    Test
}

@Composable
fun CrosswordAppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = CrosswordAppScreens.Home.name,
        modifier = modifier
    ) {
        composable(route = CrosswordAppScreens.Home.name) {
            HomeScreen(
                navigateResumeGame = { navController.navigate(CrosswordAppScreens.Game.name + "/false") },
                navigateNewGame = { navController.navigate(CrosswordAppScreens.Game.name + "/true") },
//                navigateRules = { navController.navigate(CrosswordAppScreens.Rules.name) },
                navigateLearnedWords = { navController.navigate(CrosswordAppScreens.LearnedWords.name) }
            )
        }
        composable(
            route = "${CrosswordAppScreens.Game.name}/{newGameQ}",
            arguments = listOf(
                navArgument("newGameQ") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val newGameQ = backStackEntry.arguments?.getString("newGameQ").toBoolean()
            CrosswordScreen(
                newGameQ = newGameQ,
                navigateToHome = { navController.navigate(CrosswordAppScreens.Home.name) },
                navigateToWin = { wordList ->
                    // Serialize the list to JSON
                    val jsonList = Gson().toJson(wordList)
                    // Encode to handle special characters
                    val encodedJson = URLEncoder.encode(jsonList, "UTF-8")
                    navController.navigate("${CrosswordAppScreens.Win.name}/$encodedJson")
                }
            )
        }
//        composable(route = CrosswordAppScreens.Rules.name) {
//            RulesScreen(
//                navigateHome = { navController.navigate(CrosswordAppScreens.Home.name) }
//            )
//        }
        composable(
            route = "${CrosswordAppScreens.Win.name}/{wordList}",
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
                navigateHome = { navController.navigate(CrosswordAppScreens.Home.name) }
            )
        }
        composable(
            route = CrosswordAppScreens.LearnedWords.name
        ) {
            LearnedWordsScreen(
                navigateHome = { navController.navigate(CrosswordAppScreens.Home.name) }
            )
        }
//        composable(
//            route = CrosswordAppScreens.Test.name
//        ) {
//            Test(
////                navigateHome = { navController.navigate(CrosswordAppScreens.Home.name) }
//            )
//        }
    }
}