package com.example.mvvmnewsappincompose

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SavedNewsScreen(
                    name: String,
                    onClick: () -> Unit
) {
    
    Surface {
        Text(text = "Saved Screen here!")
    }
    
}