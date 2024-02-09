package com.example.mvvmnewsappincompose

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mvvmnewsappincompose.breakingnews.NewsViewModel
import com.example.mvvmnewsappincompose.models.Article

@SuppressLint("SetJavaScriptEnabled", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WebViewScreen(
    article: Article,
    viewModel: NewsViewModel = hiltViewModel()
){

    var backEnabled by remember { mutableStateOf(true) }
    var webView: WebView? = null

    // Adding a WebView inside AndroidView
    // with layout as full screen
    Scaffold(
        topBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.saveArticle(article)
                },
                modifier = Modifier.size(50.dp)
            ) {
                Icon(Icons.Filled.Add,"")
            }
        }
    ) {

        AndroidView(
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = WebViewClient()

                    // to play video on a web view
                    settings.javaScriptEnabled = true

                    webViewClient = object : WebViewClient() {

                        override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                            backEnabled = view.canGoBack()
                        }

                    }

                    loadUrl(article.url)
                    webView = this
                }
            }, update = {
                webView = it
                //  it.loadUrl(url)
            })


        BackHandler(enabled = backEnabled) {
            webView?.goBack()
        }

    }

}
