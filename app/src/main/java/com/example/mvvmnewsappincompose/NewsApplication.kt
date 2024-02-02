package com.example.mvvmnewsappincompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mvvmnewsappincompose.breakingnews.BreakingNewsScreen
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
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MVVMNewsAppInComposeTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "breaking_news_screen"){

                    composable("breaking_news_screen"){
                        BreakingNewsScreen(navController = navController)
                    }

                }

                val items = listOf(
                    BottomNavigationItem(
                        title = "Breaking News",
                        selectedIcon = ImageVector.vectorResource(R.drawable.ic_breaking_news),
                        unSelectedIcon = ImageVector.vectorResource(R.drawable.ic_breaking_news),
                        hasNews = false
                    ),
                    BottomNavigationItem(
                        title = "Saved News",
                        selectedIcon = ImageVector.vectorResource(R.drawable.ic_favorite),
                        unSelectedIcon = ImageVector.vectorResource(R.drawable.ic_favorite),
                        hasNews = false,
                    ),
                    BottomNavigationItem(
                        title = "Search News",
                        selectedIcon = ImageVector.vectorResource(R.drawable.ic_breaking_news),
                        unSelectedIcon = ImageVector.vectorResource(R.drawable.ic_breaking_news),
                        hasNews = false,
                    ),

                )

                var selectedItemIndex by rememberSaveable {
                    mutableStateOf(0)
                }

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(bottomBar = {
                        NavigationBar {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    selected = selectedItemIndex == index,
                                    onClick = {
                                        selectedItemIndex = index
                                    },
                                    label = {
                                        Text(text = item.title)
                                    },
                                    icon = {
                                        BadgedBox(
                                            badge = {
                                                if(item.badgeCount != null) {
                                                    Badge {
                                                        Text(text = item.badgeCount.toString())
                                                    }
                                                } else if(item.hasNews) {
                                                    Badge()
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = if(index == selectedItemIndex){
                                                    item.selectedIcon
                                                } else item.unSelectedIcon,
                                                contentDescription = item.title
                                            )
                                        }
                                    })
                            }
                        }
                    }

                    ) {
                        it.toString()
                    }
                }
            }
        }
    }
}