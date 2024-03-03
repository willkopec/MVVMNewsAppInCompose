package com.willkopec.mvvmnewsappincompose.savednews

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.willkopec.mvvmnewsappincompose.breakingnews.SavedNewsListScreenWithSnackBar

@Composable
fun SavedNewsScreen(
    navController: NavController,
    name: String,
    onClick: () -> Unit
) {

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        SavedNewsListScreenWithSnackBar(navController)
    }

}