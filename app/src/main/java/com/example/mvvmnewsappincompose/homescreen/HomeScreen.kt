package com.example.mvvmnewsappincompose.homescreen


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.*
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mvvmnewsappincompose.BottomNavigationItem
import com.example.mvvmnewsappincompose.R
import com.example.mvvmnewsappincompose.util.BottomBarScreen
import com.example.mvvmnewsappincompose.util.Constants.Companion.APP_NAME

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                /*navigationIcon = ,*/
                title = {
                    Text(text = APP_NAME)
                }
            )
        },
        bottomBar = { BottomNavigation(navController) },
    ) {scaffoldPadding->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .consumedWindowInsets(scaffoldPadding)
                .systemBarsPadding(),
            contentAlignment = Alignment.BottomCenter
        ) {

            HomeNavGraph(navController = navController)

        }


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