package com.example.neat_people_app.ui.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.neat_people_app.ui.content.components.MainDisplayBar
import com.example.neat_people_app.ui.content.components.UserInfoPanel

@Composable
fun PageWithBars(userName: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Fixed top bars
        MainDisplayBar()
        UserInfoPanel(userName = userName)

        // Scrollable content takes remaining space
        Column(
            modifier = Modifier
                .weight(1f) // Takes remaining height
                .fillMaxWidth()
        ) {
            content()
        }
    }
}