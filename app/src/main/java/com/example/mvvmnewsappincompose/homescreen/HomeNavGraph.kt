package com.example.mvvmnewsappincompose.homescreen

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.mvvmnewsappincompose.Graph
import com.example.mvvmnewsappincompose.ScreenContent
import com.example.mvvmnewsappincompose.breakingnews.BreakingNewsScreen
import com.example.mvvmnewsappincompose.models.Article
import com.example.mvvmnewsappincompose.newsarticle.WebViewScreen
import com.example.mvvmnewsappincompose.savednews.SavedNewsScreen
import com.example.mvvmnewsappincompose.searchnews.SearchNewsScreen
import com.example.mvvmnewsappincompose.util.BottomBarScreen
import com.squareup.moshi.Moshi
import java.net.URLDecoder

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
                navController = navController,
                onClick = { }
            )
        }
        composable(route = BottomBarScreen.SearchNews.route) {
            SearchNewsScreen(
                navController = navController,
                name = BottomBarScreen.SearchNews.route,
                onClick = { },
                true
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
            val currentArticle = jsonAdapter.fromJson(articleJson)
            
            //WebViewScreen(url = "https://www.google.com")
            if (currentArticle != null) {
                WebViewScreen(currentArticle)
            }

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