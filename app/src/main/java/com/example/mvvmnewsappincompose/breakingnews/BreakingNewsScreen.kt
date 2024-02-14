package com.example.mvvmnewsappincompose.breakingnews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.mvvmnewsappincompose.models.Article
import com.squareup.moshi.Moshi
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.net.URLEncoder

@Composable
fun BreakingNewsScreen(navController: NavController, name: String, onClick: () -> Unit) {
    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        BreakingNewsListScreen(navController)
    }
}

@Composable
fun BreakingNewsListScreen(
    navController: NavController,
    viewModel: NewsViewModel = hiltViewModel()
) {

    val breakingNewsList by remember { viewModel.breakingNews }

    LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) {
        val itemCount = breakingNewsList.size

        items(itemCount) {

            if (it >= itemCount - 1) {
                LaunchedEffect(key1 = true) { viewModel.getBreakingNews("us") }
            }

            NewsArticleEntry(
                navController,
                rowIndex = it,
                entry = breakingNewsList,
                modifier = Modifier
            )

        }

    }
}

@Composable
fun SavedNewsListScreen(navController: NavController, viewModel: NewsViewModel = hiltViewModel()) {

    val savedNews by remember { viewModel.savedNews }

    LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) {

        val itemCount = savedNews.size

        items(itemCount) {
            //no need to paginate as savedNews already contains all of the saved news from the Article database
            //so all is loaded at once
            SavedNewsEntry(navController, rowIndex = it, entry = savedNews, modifier = Modifier)
        }
    }
}

@Composable
fun SearchNewsResults(navController: NavController, viewModel: NewsViewModel = hiltViewModel()) {

    val searchNews by remember { viewModel.searchNews }
    val isSearching by remember { viewModel.isSearching }
    val endReached by remember {  viewModel.endReached  }
    val loadError by remember {  viewModel.loadError  }
    val isLoading by remember {  viewModel.isLoading  }

    LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) {
        val itemCount = searchNews.size

        items(itemCount) {
            if (it >= itemCount - 1 && !endReached && !isLoading && !isSearching) {

                //LaunchedEffect(key1 = true) { viewModel.searchNews("") }

            }

            NewsArticleEntry(
                navController,
                rowIndex = it,
                entry = searchNews,
                modifier = Modifier
            )
        }

    }
}

@Composable
fun NewsArticleEntry(
    navController: NavController,
    rowIndex: Int,
    entry: List<Article>,
    modifier: Modifier = Modifier,
    viewModel: NewsViewModel = hiltViewModel()
) {

    val moshi = Moshi.Builder().build()
    val jsonAdapter = moshi.adapter(Article::class.java).lenient()
    val currentArticle = jsonAdapter.toJson(entry[rowIndex])

    Column {
        Box(
            modifier =
            modifier
                .fillMaxWidth()
                .shadow(1.dp, RoundedCornerShape(1.dp))
                /*.padding(13.dp)*/
                .clickable {
                    val encodedUrl = URLEncoder.encode(currentArticle, "utf-8")
                    navController.navigate("saved_news/$encodedUrl")
                }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.3f)
                /*.align(Alignment.TopStart)*/
            ) {
                Column {
                    SubcomposeAsyncImage(
                        model = entry[rowIndex].urlToImage,
                        contentDescription = null,
                        modifier = Modifier.fillMaxHeight(),
                        loading = { CircularProgressIndicator(modifier = Modifier.scale(0.5f)) }
                    )
                }
            }

            val modifierForText: Modifier

            if (entry[rowIndex].urlToImage == null) {
                modifierForText = Modifier.fillMaxWidth(0.7f).align(Alignment.Center)
            } else {
                modifierForText = Modifier.fillMaxWidth(0.7f).align(Alignment.TopEnd)
            }

            Row(modifier = modifierForText) {
                Column {
                    if (entry[rowIndex].title != "[Removed]") {
                        entry[rowIndex].title?.let { it1 ->
                            Text(
                                text = it1.replace('+', ' '),
                                fontSize = 12.sp,
                                fontWeight = Bold,
                                lineHeight = 15.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2
                            )
                        }

                        if (entry[rowIndex].description != null) {
                            entry[rowIndex].description?.let { it1 ->
                                Text(
                                    text = it1.replace('+', ' '),
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
                                    text = it1.replace('+', ' '),
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

@Composable
fun SavedNewsEntry(
    navController: NavController,
    rowIndex: Int,
    entry: List<Article>,
    modifier: Modifier = Modifier,
    viewModel: NewsViewModel = hiltViewModel()
) {

    var delete = SwipeAction(
        onSwipe = {
            viewModel.deleteArticle(entry[rowIndex])
        },
        icon = {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete chat",
                modifier = Modifier.padding(16.dp),
                tint = Color.White
            )
        },
        background = Color.Red.copy(alpha = 0.5f),
        isUndo = true
    )

    val moshi = Moshi.Builder().build()
    val jsonAdapter = moshi.adapter(Article::class.java).lenient()
    val currentArticle = jsonAdapter.toJson(entry[rowIndex])

    Column {
        SwipeableActionsBox(
            modifier = modifier
                .fillMaxWidth()
                .shadow(1.dp, RoundedCornerShape(1.dp))
                /*.padding(13.dp)*/
                .clickable {
                    val encodedUrl = URLEncoder.encode(currentArticle, "utf-8")
                    navController.navigate("saved_news/$encodedUrl")
                },
                swipeThreshold = 100.dp,
                endActions = listOf(delete)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.3f)
                /*.align(Alignment.TopStart)*/
            ) {
                Column {
                    SubcomposeAsyncImage(
                        model = entry[rowIndex].urlToImage,
                        contentDescription = null,
                        modifier = Modifier.fillMaxHeight(),
                        loading = { CircularProgressIndicator(modifier = Modifier.scale(0.5f)) }
                    )
                }
            }

            val modifierForText: Modifier

            if (entry[rowIndex].urlToImage == null) {
                modifierForText = Modifier.align(Alignment.Center)
            } else {
                modifierForText = Modifier.fillMaxWidth().align(Alignment.TopEnd)
                    .padding(start = 150.dp)
            }

            Row(modifier = modifierForText) {
                Column {
                    if (entry[rowIndex].title != "[Removed]") {
                        entry[rowIndex].title?.let { it1 ->
                            Text(
                                text = it1.replace('+', ' '),
                                fontSize = 12.sp,
                                fontWeight = Bold,
                                lineHeight = 15.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2
                            )
                        }

                        if (entry[rowIndex].description != null) {
                            entry[rowIndex].description?.let { it1 ->
                                Text(
                                    text = it1.replace('+', ' '),
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
                                    text = it1.replace('+', ' '),
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
