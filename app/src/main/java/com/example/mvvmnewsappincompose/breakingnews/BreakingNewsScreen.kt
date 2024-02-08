package com.example.mvvmnewsappincompose.breakingnews

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import com.example.mvvmnewsappincompose.BottomNavigationItem
import com.example.mvvmnewsappincompose.R
import com.example.mvvmnewsappincompose.models.Article
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun BreakingNewsScreen(
    navController: NavController,
    name: String,
    onClick: () -> Unit
) {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        //BottomNavigation(navController)
        BreakingNewsListScreen(navController)
        //BottomNavigation()
    }

}

@Composable
fun BreakingNewsListScreen(
    navController: NavController,
    viewModel: BreakingNewsViewModel = hiltViewModel()
) {

    val breakingNewsList by remember {  viewModel.breakingNews  }


    LazyColumn(contentPadding = PaddingValues(
        //16.dp,
        bottom = 100.dp
    ))
    {

        val itemCount = breakingNewsList.size

        items(itemCount) {
            if(it >= itemCount - 1) {

                LaunchedEffect(key1 = true){
                    viewModel.getBreakingNews("us")
                }

            }
            NewsArticleEntry(
                navController,
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
    navController: NavController,
    rowIndex: Int,
    entry: List<Article>,
    modifier: Modifier = Modifier,
    viewModel: BreakingNewsViewModel = hiltViewModel()
) {

    Column {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .shadow(1.dp, RoundedCornerShape(1.dp))
                /*.padding(13.dp)*/
                .clickable {
                    //
                    val encodedUrl = URLEncoder.encode(entry[rowIndex].url, StandardCharsets.UTF_8.toString())
                    navController.navigate(
                        "saved_news/$encodedUrl"
                    )
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    /*.align(Alignment.TopStart)*/
            ) {
                Column {
                    SubcomposeAsyncImage(
                        model = entry[rowIndex].urlToImage,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight(),
                        loading = {
                            CircularProgressIndicator(
                                modifier = Modifier.scale(0.5f)
                            )
                        }
                    )
                }

            }

            val modifierForText: Modifier

            if(entry[rowIndex].urlToImage == null){
                modifierForText = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.Center)
            } else {
                modifierForText = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.TopEnd)
            }

            Row(
                modifier = modifierForText
            ) {
                Column {

                    if(entry[rowIndex].title != "[Removed]"){
                        entry[rowIndex].title?.let { it1 ->
                            Text(
                                text = it1,
                                fontSize = 12.sp,
                                fontWeight = Bold,
                                lineHeight = 15.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2
                            )
                        }

                        if(entry[rowIndex].description != null){
                            entry[rowIndex].description?.let { it1 ->
                                Text(
                                    text = it1,
                                    fontSize = 10.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 12.sp,
                                    modifier = Modifier.fillMaxWidth(),
                                    maxLines = 4
                                )
                            }
                        } else {
                            entry[rowIndex].title?.let { it1 ->
                                Text(
                                    text = it1,
                                    fontSize = 9.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 11.sp,
                                    modifier = Modifier.fillMaxWidth(),
                                    maxLines = 4
                                )
                            }
                        }

                    }


                }

            }

        }
    }
}