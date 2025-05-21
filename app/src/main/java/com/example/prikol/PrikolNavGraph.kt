package com.example.prikol

import CrosswordScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.prikol.ui.HomeScreen
import com.example.prikol.ui.LearnedWordsScreen
import com.example.prikol.ui.RulesScreen
import com.example.prikol.ui.WinScreen
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URLEncoder

enum class PrikolScreens() {
    Home,
    Game,
    Rules,
    Win,
    LearnedWords,
    Test
}

@Composable
fun PrikolNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = PrikolScreens.Home.name,
//        startDestination = PrikolScreens.Home.name,
        modifier = modifier
    ) {
        composable(route = PrikolScreens.Home.name) {
            HomeScreen(
                navigateResumeGame = { navController.navigate(PrikolScreens.Game.name + "/false") },
                navigateNewGame = { navController.navigate(PrikolScreens.Game.name + "/true") },
//                navigateRules = { navController.navigate(PrikolScreens.Rules.name) },
                navigateLearnedWords = { navController.navigate(PrikolScreens.LearnedWords.name) }
            )
        }
        composable(
            route = "${PrikolScreens.Game.name}/{newGameQ}",
            arguments = listOf(
                navArgument("newGameQ") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val newGameQ = backStackEntry.arguments?.getString("newGameQ").toBoolean()
            CrosswordScreen(
                newGameQ = newGameQ
//                navigateHome = { navController.navigate(PrikolScreens.Home.name) },
//                navigateWin = { wordList ->
//                    // Serialize the list to JSON
//                    val jsonList = Gson().toJson(wordList)
//                    // Encode to handle special characters
//                    val encodedJson = URLEncoder.encode(jsonList, "UTF-8")
//                    navController.navigate("${PrikolScreens.Win.name}/$encodedJson")
//                }
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
        composable(
            route = PrikolScreens.LearnedWords.name
        ) {
            LearnedWordsScreen(
                navigateHome = { navController.navigate(PrikolScreens.Home.name) }
            )
        }
//        composable(
//            route = PrikolScreens.Test.name
//        ) {
//            Test(
////                navigateHome = { navController.navigate(PrikolScreens.Home.name) }
//            )
//        }
    }
}