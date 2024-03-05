package com.example.prikol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.prikol.ui.AddTermScreen
import com.example.prikol.ui.EditTermScreen
import com.example.prikol.ui.HomeScreen
import com.example.prikol.ui.ViewTermScreen

//enum class TermsViewScreens(@StringRes val routeName: Int) {
//enum class TermsAppScreens(val routeName: String) {
//    Overview(routeName = "overview"),
//    Add(routeName = "add"),
//    View(routeName = "view"),
//    Edit(routeName = "edit")
//}

@Composable
fun PrikolNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
//    val context: Context = LocalContext.current.applicationContext
//    val repository: TermsRepository by lazy {
//        TermsRepository(TermDatabase.getDatabase(context).termDao()) // why does it slows app so much?
//    } // probably because viewModels's uiStates or smth else initiated every time navigation is running

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable(route = "home") {
            HomeScreen(
                navigateToAdd = { navController.navigate("add") },
                navigateToView = { navController.navigate("viewTerm/$it") }
                )
        }
        composable(route = "add") {
            AddTermScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(route = "viewTerm/{termId}", // Why and how it works
//            arguments = listOf(navArgument("termId"))
            ) {
            ViewTermScreen(
                navigateBack = { navController.popBackStack() },
                navigateToEdit = { navController.navigate("editTerm/$it") },
                navigateToNext = {
                    navController.popBackStack()
                    navController.navigate("viewTerm/$it")
                }
            )
        }
        composable(route = "editTerm/{termId}") {
            EditTermScreen(
                navigateBack = { navController.popBackStack() },
                navigateHome = { navController.popBackStack("home", inclusive = false) }
            )
        }
    }
}