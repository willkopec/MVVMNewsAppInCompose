package com.example.mvvmnewsappincompose.breakingnews

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import com.example.mvvmnewsappincompose.SortType
import com.example.mvvmnewsappincompose.getAllTypes
import com.example.mvvmnewsappincompose.getSortType
import com.example.mvvmnewsappincompose.homescreen.RetrySection
import com.example.mvvmnewsappincompose.models.Article
import com.squareup.moshi.Moshi
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.net.URLEncoder

/*
*
* BreakingNewsScreen: This file contains all of the needed composables for all
* of the different sections in the main screen (Includes BreakingNewsScreen,
* BreakingNewsListScreen, NewsArticleEntry, and the Chips for sorting news)
* 
 */

@Composable
fun BreakingNewsScreen(navController: NavController, name: String, onClick: () -> Unit, viewModel: NewsViewModel = hiltViewModel()) {

    val currentSortType by remember { viewModel.currentSortType }

    Column {

        ChipGroup(
            chips = getAllTypes(),
            selectedType = currentSortType,
            onSelectedChanged = {
                viewModel.currentSortType.value = getSortType(it)
                viewModel.updateCurrentNews()
                viewModel.updateScrollToTop(true)
            }
        )

        Surface(modifier = Modifier.fillMaxSize()) {
            BreakingNewsListScreen(navController)
        }
    }

}

@Composable
fun BreakingNewsListScreen(
    navController: NavController,
    viewModel: NewsViewModel = hiltViewModel()
) {

    val scrollState = rememberLazyListState()
    val scrollToTop by viewModel.scrollToTop.observeAsState()
    val currentNews by remember { viewModel.currentNews }
    val loadError by remember { viewModel.loadError }

    LaunchedEffect(
        key1 = scrollToTop,
    ) {
        if (scrollToTop == true) {
            scrollState.scrollToItem(0)
            viewModel.updateScrollToTop(false)
        }
    }

    if(loadError == ""){
        LazyColumn (state = scrollState){

            val itemCount = currentNews.size

            items(itemCount) {

                NewsArticleEntry(
                    navController,
                    rowIndex = it,
                    entry = currentNews,
                    modifier = Modifier
                )

            }

        }
    } else {
        RetrySection(error = loadError) {
            viewModel.getAllNewsLists()
        }
    }


}

@Composable
fun Chip(
    name: String = "Chip",
    isSelected: Boolean = false,
    onSelectionChanged: (String) -> Unit = {},
) {
    Surface(
        modifier = Modifier.padding(1.dp),
        shape = MaterialTheme.shapes.small,
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    ) {
        Row(modifier = Modifier
            .toggleable(
                value = isSelected,
                onValueChange = {
                    onSelectionChanged(name)
                }
            )
        ) {
            Text(
                text = name,
                /*color = MaterialTheme.colorScheme.,*/
                fontSize = 14.sp,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun ChipGroup(
    chips: List<SortType> = getAllTypes(),
    selectedType: SortType? = null,
    viewModel: NewsViewModel = hiltViewModel(),
    onSelectedChanged: (String) -> Unit = {},
) {

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        LazyRow(modifier = Modifier) {
            items(chips) {
                Chip(
                    name = it.value,
                    isSelected = selectedType == it,
                    onSelectionChanged = {
                        onSelectedChanged(it)
                    },
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SavedNewsListScreenWithSnackBar(
    navController: NavController,
    viewModel: NewsViewModel = hiltViewModel()
){

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        SavedNewsListScreen(navController, snackbarHostState)
    }

}


@Composable
fun SavedNewsListScreen(navController: NavController, snackbarHostState: SnackbarHostState, viewModel: NewsViewModel = hiltViewModel()) {

    val savedNews by remember { viewModel.savedNews }


    LazyColumn(/*contentPadding = PaddingValues(bottom = 100.dp)*/) {

        val itemCount = savedNews.size

        items(itemCount) {
            //no need to paginate as savedNews already contains all of the saved news from the Article database
            //so all is loaded at once
            SavedNewsEntry(navController, rowIndex = it, entry = savedNews, modifier = Modifier, snackbarHostState = snackbarHostState)
        }
    }
}

@Composable
fun SearchNewsResults(navController: NavController, viewModel: NewsViewModel = hiltViewModel()) {

    val searchNews by remember { viewModel.searchNews }
    val isSearching by remember { viewModel.isSearching }
    val endReached by remember {  viewModel.endReached  }
    //val loadError by remember {  viewModel.loadError  }
    val isLoading by remember {  viewModel.isLoading  }

    if(searchNews.size == 0 && isSearching){

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "No Results Found!",
                textAlign = TextAlign.Center
            )

    } else {

        LazyColumn() {
            val itemCount = searchNews.size

            items(itemCount) {
                //if (it >= itemCount - 1 && !endReached && !isLoading && !isSearching) {

                    //LaunchedEffect(key1 = true) { viewModel.searchNews("") }

                //}

                NewsArticleEntry(
                    navController,
                    rowIndex = it,
                    entry = searchNews,
                    modifier = Modifier
                )

            }

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
            modifier = Modifier
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
                        modifier = Modifier
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(5.dp)),
                        loading = { CircularProgressIndicator(modifier = Modifier.scale(0.5f)) }
                    )
                }
            }

            val modifierForText: Modifier

            if (entry[rowIndex].urlToImage == null) {
                modifierForText = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.Center)
            } else {
                modifierForText = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.TopEnd)
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
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: NewsViewModel = hiltViewModel()
) {

    val deletedArticle by viewModel.articleDeleted.observeAsState()

    LaunchedEffect(key1 = deletedArticle) {

        if(deletedArticle == true){
            snackbarHostState.showSnackbar("Article deleted", duration = SnackbarDuration.Long)
            viewModel.updateArticleDeleted(false)
        }

    }

    var delete = SwipeAction(
        onSwipe = {
            val deletedArticle: Article = entry[rowIndex]
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
                modifierForText = Modifier.fillMaxWidth()
            } else {
                modifierForText = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopEnd)
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