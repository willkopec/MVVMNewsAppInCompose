package com.example.mvvmnewsappincompose.breakingnews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.mvvmnewsappincompose.BottomNavigationItem
import com.example.mvvmnewsappincompose.R
import com.example.mvvmnewsappincompose.models.Article
import com.example.mvvmnewsappincompose.models.NewsResponse
import com.example.mvvmnewsappincompose.util.Resource

@Composable
fun BreakingNewsScreen(
    navController: NavController
) {
    Surface(
        color = Color.Red,
        modifier = Modifier.fillMaxSize()
    ) {

        //BottomNavigation(navController)
        BreakingNewsListScreen(navContoller = navController)
        //BottomNavigation()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigation(
    navContoller: NavController,
    viewModel: BreakingNewsViewModel = hiltViewModel()
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

@Composable
fun BreakingNewsListScreen(
    navContoller: NavController,
    viewModel: BreakingNewsViewModel = hiltViewModel()
) {

    val breakingNewsList by remember {  viewModel.breakingNews  }


    LazyColumn(contentPadding = PaddingValues(16.dp)) {

        val itemCount = if(breakingNewsList.size % 2 == 0){
            breakingNewsList.size / 2
        } else {
            breakingNewsList.size / 2 + 1
        }

        items(itemCount) {
            if(it >= itemCount - 1) {

                LaunchedEffect(key1 = true){
                    viewModel.getBreakingNews("us")
                }

            }
            NewsArticleEntry(
                entry = breakingNewsList,
                navContoller = navContoller,
                modifier = Modifier
            )
        }

    }

    /*Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()) {
        if(isLoading) {
            CircularProgressIndicator(color = Color.Blue)
        }
    }*/
    Text(
        text = "debug purposes",
        textAlign = TextAlign.Center
    )
    //BottomNavigation(navContoller)
}

@Composable
fun NewsArticleEntry(
    entry: List<Article>,
    navContoller: NavController,
    modifier: Modifier = Modifier,
    viewModel: BreakingNewsViewModel = hiltViewModel()
    ) {

    entry.forEach {
        it.title?.let { it1 ->

            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .shadow(5.dp, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .aspectRatio(1f)
                /*.clickable {

                }*/
            ) {
                Column {

                    Text(
                        text = it1,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                }

            }
        }
    }

}