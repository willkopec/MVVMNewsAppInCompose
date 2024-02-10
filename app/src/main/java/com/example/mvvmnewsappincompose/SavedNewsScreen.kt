package com.example.mvvmnewsappincompose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.mvvmnewsappincompose.breakingnews.SavedNewsListScreen

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
        SavedNewsListScreen(navController)
    }

}