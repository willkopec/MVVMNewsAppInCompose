package com.example.mvvmnewsappincompose.breakingnews

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun BreakingNewsScreen(
    navController: NavController,
    viewModel: BreakingNewsViewModel = hiltViewModel()
) {
    Surface(
        color = Color.Red,
        modifier = Modifier.fillMaxSize()
    ) {

    }

}