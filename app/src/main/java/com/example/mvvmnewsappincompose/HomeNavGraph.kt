package com.example.mvvmnewsappincompose

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.navigation
import com.example.mvvmnewsappincompose.breakingnews.BreakingNewsScreen

@Composable
fun HomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomBarScreen.BreakingNews.route
    ) {
        composable(route = BottomBarScreen.BreakingNews.route) {
            BreakingNewsScreen(
                navController,
                name = BottomBarScreen.BreakingNews.route,
                onClick = { /*TODO*/ }
            )
        }
        composable(route = BottomBarScreen.SavedNews.route) {
            /*ScreenContent(
                name = BottomBarScreen.Profile.route,
                onClick = { navController.navigate(Graph.DETAILS) }
            )*/
            SavedNewsScreen(
                name = BottomBarScreen.SavedNews.route,
                onClick = { }
            )
            //WebViewScreen("https://www.google.com")
        }
        composable(route = BottomBarScreen.SearchNews.route) {
            ScreenContent(
                name = BottomBarScreen.SearchNews.route,
                onClick = { }
            )
        }

        composable(route = "saved_news/{articleUrl}",
            arguments = listOf(
                navArgument("articleUrl") {
                    type = NavType.StringType
                }
            )
        ) {
            val articleUrl = remember {
                it.arguments?.getString("articleUrl")
            }
            //WebViewScreen("https://www.google.com")
            /*val articleUrl = remember {
                it.arguments?.getString("articleUrl")
            }
            Log.d("Saved News WebView Navigation", "HomeNavGraph: ${articleUrl}")
            if (articleUrl != null) {*/

            if (articleUrl != null) {
                WebViewScreen(articleUrl)
            }
            //}
        }

        detailsNavGraph(navController = navController)
    }
}

fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.DETAILS,
        startDestination = DetailsScreen.Information.route
    ) {
        composable(route = DetailsScreen.Information.route) {
            ScreenContent(name = DetailsScreen.Information.route) {
                navController.navigate(DetailsScreen.Overview.route)
            }
        }
        composable(route = DetailsScreen.Overview.route) {
            ScreenContent(name = DetailsScreen.Overview.route) {
                navController.popBackStack(
                    route = DetailsScreen.Information.route,
                    inclusive = false
                )
            }
        }
    }
}

sealed class DetailsScreen(val route: String) {
    object Information : DetailsScreen(route = "INFORMATION")
    object Overview : DetailsScreen(route = "OVERVIEW")
}