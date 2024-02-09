package com.example.mvvmnewsappincompose

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.mvvmnewsappincompose.breakingnews.BreakingNewsScreen
import com.example.mvvmnewsappincompose.models.Article
import com.squareup.moshi.Moshi
import java.net.URLDecoder
import java.net.URLEncoder

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

        composable(route = "saved_news/{article}",
            arguments = listOf(
                navArgument("article") {
                    type = NavType.StringType
                }
            )
        ) {backStackEntry ->

            val articleJson = backStackEntry.arguments?.getString("article")
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(Article::class.java).lenient()
            URLDecoder.decode(articleJson, "utf-8")

            val currentArticle = jsonAdapter.fromJson(articleJson)
            
            //WebViewScreen(url = "https://www.google.com")
            if (currentArticle != null) {
                WebViewScreen(currentArticle)
            }


            /*val articleUrl = remember {
                it.arguments?.getString("articleUrl")
            }

            //need to pass an "https://" link to not cause an error
            if (articleUrl != null) {
                WebViewScreen(articleUrl)
            }*/

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