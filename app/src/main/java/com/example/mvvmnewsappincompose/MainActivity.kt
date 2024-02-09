package com.example.mvvmnewsappincompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mvvmnewsappincompose.breakingnews.NewsViewModel
import com.example.mvvmnewsappincompose.ui.theme.MVVMNewsAppInComposeTheme
import dagger.hilt.android.AndroidEntryPoint

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
    )

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel:NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MVVMNewsAppInComposeTheme {

                /*val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "breaking_news_screen")
                {

                    composable("breaking_news_screen") {
                        BreakingNewsScreen(navController = navController)
                    }

                }*/

                RootNavigationGraph(navController = rememberNavController())


            }
        }
    }
}

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.HOME
    ) {
        //authNavGraph(navController = navController)
        composable(route = Graph.HOME) {
            HomeScreen()
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val HOME = "home_graph"
    const val DETAILS = "details_graph"
}