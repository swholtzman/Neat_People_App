package com.example.neat_people_app.ui.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.neat_people_app.data.db.DynamoAccessService
import com.example.neat_people_app.ui.content.components.ButtonRow
import com.example.neat_people_app.ui.content.components.ItemCard

@Composable
fun ContentPage(
    navController: NavController,
    viewModel: ContentViewModel,
    userName: String
) {
    val items = viewModel.items

    PageWithBars(userName = userName) {
        ButtonRow(onCreateClick = { navController.navigate("item_creation/new") })
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()  // Uses all remaining space from the Box in PageWithBars
                .padding(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(items) { item ->
                ItemCard(
                    item = item,
                    onItemClick = { navController.navigate("item_creation/${item.id}") }
                )
            }
        }
    }
}

class ContentViewModelFactory(private val dynamoAccessService: DynamoAccessService) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContentViewModel(dynamoAccessService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}