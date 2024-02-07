package com.example.mvvmnewsappincompose


import android.annotation.SuppressLint
import androidx.compose.material.*
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = { BottomNavigation(navController) },
    ) {
        HomeNavGraph(navController = navController)

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigation(
    navController: NavHostController
) {

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
            selectedIcon = ImageVector.vectorResource(R.drawable.ic_all_news),
            unSelectedIcon = ImageVector.vectorResource(R.drawable.ic_all_news),
            hasNews = false,
        ),

        )

    val screens = listOf(
        BottomBarScreen.BreakingNews,
        BottomBarScreen.SavedNews,
        BottomBarScreen.SearchNews,
    )

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if(bottomBarDestination){
        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    /*selected = selectedItemIndex == index,*/
                    selected = currentDestination?.hierarchy?.any {
                        it.route == screens[index].route
                    } == true,
                    onClick = {
                        navController.navigate(screens[index].route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
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


}