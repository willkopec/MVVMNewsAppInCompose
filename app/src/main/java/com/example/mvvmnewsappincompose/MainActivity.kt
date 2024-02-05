package com.example.mvvmnewsappincompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.mvvmnewsappincompose.breakingnews.BreakingNewsScreen
import com.example.mvvmnewsappincompose.breakingnews.BreakingNewsViewModel
import com.example.mvvmnewsappincompose.db.ArticleDatabase
import com.example.mvvmnewsappincompose.repository.NewsRepository
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

    /*private val viewModel by viewModels<BreakingNewsViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return BreakingNewsViewModel() as T
                }
            }
        }
    )*/



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

