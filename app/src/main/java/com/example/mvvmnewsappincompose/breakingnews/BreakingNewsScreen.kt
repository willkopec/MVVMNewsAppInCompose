package com.example.mvvmnewsappincompose.breakingnews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import com.example.mvvmnewsappincompose.BottomNavigationItem
import com.example.mvvmnewsappincompose.R
import com.example.mvvmnewsappincompose.models.Article


@Composable
fun BreakingNewsScreen(
    name: String,
    onClick: () -> Unit
) {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        //BottomNavigation(navController)
        BreakingNewsListScreen()
        //BottomNavigation()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigation(
    navContoller: NavController
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




@Composable
fun BreakingNewsListScreen(
    viewModel: BreakingNewsViewModel = hiltViewModel()
) {

    val breakingNewsList by remember {  viewModel.breakingNews  }


    LazyColumn(contentPadding = PaddingValues(16.dp)) {

        val itemCount = breakingNewsList.size

        items(itemCount) {
            if(it >= itemCount - 1) {

                LaunchedEffect(key1 = true){
                    viewModel.getBreakingNews("us")
                }

            }
            NewsArticleEntry(
                rowIndex = it,
                entry = breakingNewsList,
                modifier = Modifier
            )
            //BottomNavigation(navContoller)
        }

    }


}

@Composable
fun NewsArticleEntry(
    rowIndex: Int,
    entry: List<Article>,
    modifier: Modifier = Modifier,
    viewModel: BreakingNewsViewModel = hiltViewModel()
    ) {
        Column {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .shadow(5.dp, RoundedCornerShape(10.dp))
                    .padding(13.dp)
                /*.clickable {

                }*/
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.TopStart)
                ) {
                    Column {
                        SubcomposeAsyncImage(
                            model = entry[rowIndex].urlToImage,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(),
                            loading = {
                                CircularProgressIndicator(
                                    modifier = Modifier.scale(0.5f)
                                )
                            }
                        )
                    }

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .align(Alignment.TopEnd)
                ) {
                    Column {
                            entry[rowIndex].title?.let { it1 ->
                                Text(
                                    text = it1,
                                    fontSize = 11.sp,
                                    fontWeight = Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                        entry[rowIndex].description?.let { it1 ->
                            Text(
                                text = it1,
                                fontSize = 9.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                    }

                }

                /*Row {

                    Column {
                        it.title?.let { it1 ->
                            Text(
                                text = it1,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                        SubcomposeAsyncImage(
                            model = it.urlToImage,
                            contentDescription = null,
                            modifier = Modifier
                                .size(350.dp)
                                .align(Alignment.Start),
                            loading = {
                                CircularProgressIndicator(
                                    modifier = Modifier.scale(0.5f)
                                )
                            }
                        )
                        Column {
                            it.description?.let { it1 ->
                                Text(
                                    text = it1,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                        }

                    }



                }*/

                /*Column {

                    SubcomposeAsyncImage(
                        model = it.urlToImage,
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.Start),
                        loading = {
                            CircularProgressIndicator(
                                modifier = Modifier.scale(0.5f)
                            )
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Row{
                        it.description?.let { it1 ->
                            Text(
                                text = it1,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }


                }*/

            }
        }
}