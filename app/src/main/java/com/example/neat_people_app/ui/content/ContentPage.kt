package com.example.neat_people_app.ui.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.neat_people_app.data.db.DynamoAccessService
import com.example.neat_people_app.ui.content.components.ButtonRow
import com.example.neat_people_app.ui.content.components.ItemCard
import com.example.neat_people_app.ui.content.components.MainDisplayBar
import com.example.neat_people_app.ui.content.components.UserInfoPanel

@Composable
fun ContentPage(dynamoAccessService: DynamoAccessService, userName: String = "Unnamed User") {
    val viewModel: ContentViewModel = viewModel(factory = ContentViewModelFactory(dynamoAccessService))
    val items = viewModel.items

    Column(modifier = Modifier.fillMaxSize()) {
        MainDisplayBar()
        UserInfoPanel(userName = userName)
        ButtonRow()
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items) { item ->
                ItemCard(
                    item = item,
                    onItemClick = { /* TODO: Navigate to create item page with item data */ }
                )
            }
        }
    }
}

class ContentViewModelFactory(private val dynamoAccessService: DynamoAccessService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContentViewModel(dynamoAccessService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}