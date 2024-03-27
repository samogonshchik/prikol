package com.example.prikol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.prikol.ui.AddTermScreen
import com.example.prikol.ui.EditTermScreen
import com.example.prikol.ui.HomeScreen
import com.example.prikol.ui.ViewTermScreen

enum class PrikolScreens() {
    Overview,
    Add,
    View,
    Edit
}

@Composable
fun PrikolNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
//    val context: Context = LocalContext.current.applicationContext
//    val repository: TermsRepository by lazy {
//        TermsRepository(TermDatabase.getDatabase(context).termDao())   // Why does it slows app so much?
//    }   // ViewModels's uiStates or else initialized every time navigation is running?

    NavHost(
        navController = navController,
        startDestination = PrikolScreens.Overview.name,
        modifier = modifier
    ) {
        composable(route = PrikolScreens.Overview.name) {
            HomeScreen(
                navigateToAdd = { navController.navigate(PrikolScreens.Add.name) },
                navigateToView = { navController.navigate("${PrikolScreens.View.name}/$it") }
                )
        }
        composable(route = PrikolScreens.Add.name) {
            AddTermScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "${PrikolScreens.View.name}/{termId}",   // Why and how it works
            arguments = listOf(navArgument("termId") { type = NavType.StringType })
        ) {
            ViewTermScreen(
                navigateBack = { navController.popBackStack() },
                navigateToEdit = { navController.navigate("${PrikolScreens.Edit.name}/$it") },
                navigateToView = { navController.navigate("${PrikolScreens.View.name}/$it") },
                navigateToNearest = {
                    navController.popBackStack()
                    navController.navigate("${PrikolScreens.View.name}/$it")
                }
            )
        }
        composable(
            route = "${PrikolScreens.Edit.name}/{termId}",
            arguments = listOf(navArgument("termId") { type = NavType.StringType })
        ) {
            EditTermScreen(
                navigateBack = { navController.popBackStack() },
                navigateHome = { navController.popBackStack(PrikolScreens.Overview.name, inclusive = false) }
            )
        }
    }
}