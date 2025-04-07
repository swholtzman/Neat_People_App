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
            // required params for getItems()
            val type = "item"                    // example type; change when actually in prod phase
            val tableName = Secrets.DYNAMO_DB_TABLE_NAME
            val storeName = "exampleStore"       // can access multiple store accounts; placeholder for now
            val storeId = "exampleStoreId"       // can access multiple store accounts; placeholder for now
            items = dynamoAccessService.getItems(type, tableName, storeName, storeId)
        }
    }
}