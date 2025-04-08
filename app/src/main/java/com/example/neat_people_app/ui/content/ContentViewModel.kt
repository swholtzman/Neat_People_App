package com.example.neat_people_app.ui.content

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neat_people_app.data.db.DynamoAccessService
import com.example.neat_people_app.data.model.Item
import com.example.neat_people_app.resources.Secrets
import kotlinx.coroutines.launch

class ContentViewModel(private val dynamoAccessService: DynamoAccessService) : ViewModel() {
    var items by mutableStateOf<List<Item>>(emptyList())
        private set

    init {
        loadItems()
    }

    private fun loadItems() {
        viewModelScope.launch {
            val type = "item"
            val tableName = Secrets.DYNAMO_DB_TABLE_NAME
            val storeName = "exampleStore"
            val storeId = "exampleStoreId"
            items = dynamoAccessService.getItems(type, tableName, storeName, storeId)
                .sortedByDescending { it.details.uploadDate }
        }
    }

    fun createItem(newItem: Item) {
        viewModelScope.launch {
            dynamoAccessService.createItem(newItem)
            items = (items + newItem).sortedByDescending { it.details.uploadDate }
        }
    }

    fun updateItem(updatedItem: Item) {
        viewModelScope.launch {
            dynamoAccessService.updateItem(updatedItem)
            items = items.map { if (it.id == updatedItem.id) updatedItem else it }
        }
    }

    suspend fun getNextId(tableArea: String): Int {
        return dynamoAccessService.getNextId(tableArea)
    }
}