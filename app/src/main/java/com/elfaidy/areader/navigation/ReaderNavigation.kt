package com.elfaidy.areader.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.elfaidy.areader.ui.screens.ReaderSplashScreen
import com.elfaidy.areader.ui.screens.details.BookDetailsScreen
import com.elfaidy.areader.ui.screens.details.BookDetailsViewModel
import com.elfaidy.areader.ui.screens.home.Home
import com.elfaidy.areader.viewmodel.MainViewModel
import com.elfaidy.areader.ui.screens.login.ReaderLoginScreen
import com.elfaidy.areader.ui.screens.search.BookSearchViewModel
import com.elfaidy.areader.ui.screens.search.SearchScreen
import com.elfaidy.areader.ui.screens.stats.ReaderStatsScreen
import com.elfaidy.areader.ui.screens.update.BookUpdateScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = ReaderScreens.SplashScreen.name
    ){

        composable(route = ReaderScreens.SplashScreen.name){
            ReaderSplashScreen(navController)
        }

        composable(route = ReaderScreens.HomeScreen.name){
            val viewModel: MainViewModel = hiltViewModel()
            Home(navController, viewModel)
        }

        composable(route = ReaderScreens.LoginScreen.name){
            ReaderLoginScreen(navController)
        }

        composable(route = ReaderScreens.SearchScreen.name){
            val viewModel: BookSearchViewModel = hiltViewModel()
            SearchScreen(navController, viewModel)
        }

        composable(
            route = ReaderScreens.UpdateScreen.name + "/{bookItemId}", 
            arguments = listOf(
                navArgument("bookItemId"){
                    type = NavType.StringType
                }
            )
        ){ navBackStackEntry ->

            navBackStackEntry.arguments?.getString("bookItemId").let {
                BookUpdateScreen(navController, mainViewModel, bookId = it!!)
            }
        }

        composable(route = ReaderScreens.StatsScreen.name){
            ReaderStatsScreen(navController, mainViewModel)
        }

        composable(
            route = "${ReaderScreens.DetailsScreen.name}/{bookId}",
            arguments = listOf(
                navArgument("bookId"){
                    type = NavType.StringType
                }
            )
        ){ navBackStackEntry ->
            val viewModel: BookDetailsViewModel = hiltViewModel()
            navBackStackEntry.arguments?.getString("bookId").let {
                BookDetailsScreen(navController, it, viewModel)
            }
        }
    }
}