package com.example.neat_people_app.data.db

import android.content.Context
import android.util.Log
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.example.neat_people_app.data.model.Item
import com.example.neat_people_app.resources.Secrets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.security.SecureRandom

class DynamoAccessService(context: Context) {

    private val credentialsProvider = CognitoCachingCredentialsProvider(
        context,
        Secrets.COGNITO_IDENTITY_POOL_ID,
        Regions.fromName(Secrets.DYNAMO_DB_REGION)
    )

    private val dynamoDBClient = AmazonDynamoDBClient(credentialsProvider).apply {
        setRegion(Region.getRegion(Regions.fromName(Secrets.DYNAMO_DB_REGION)))
        Log.d("DynamoAccessService", "Using region: ${Secrets.DYNAMO_DB_REGION}")
    }

    private val dynamoDBMapper = DynamoDBMapper(dynamoDBClient)

    suspend fun createItem(item: Item) {
        withContext(Dispatchers.IO) {
            try {
                dynamoDBMapper.save(item)
            } catch (e: Exception) {
                Log.e("DynamoAccessService", "Failed to save item: ${e.message}", e)
                throw e
            }
        }
    }

    suspend fun updateItem(item: Item) {
        withContext(Dispatchers.IO) {
            dynamoDBMapper.save(item)
        }
    }

    suspend fun deleteItem(id: Int) {
        withContext(Dispatchers.IO) {
            val item = Item().apply { this.tableArea = "item"; this.id = id }
            dynamoDBMapper.delete(item)
        }
    }

    fun getItem(
        type: String,
        tableName: String,
        itemId: Int,
        storeName: String,
        storeId: String
    ): Item? {
        validateInput(type, tableName)
        return dynamoDBMapper.load(Item::class.java, type, itemId) // Use tableArea and id
    }

    suspend fun getItems(
        type: String,
        tableName: String,
        storeName: String,
        storeId: String,
        search: String = "",
        lastEvalKey: String? = null,
        limit: Int = 100
    ): List<Item> = withContext(Dispatchers.IO) {
        try {
            validateInput(type, tableName)
            val scanExpression = DynamoDBScanExpression().apply {
                setLimit(limit)
//                if (lastEvalKey != null) {
//                    // pagination logic when in prod (parse lastEvalKey for exclusiveStartKey)
//                }
                if (search.isNotEmpty()) {
                    filterExpression = "contains(searchName, :searchVal)"
                    withExpressionAttributeValues(mapOf(":searchVal" to AttributeValue(search)))
                }
            }
            dynamoDBMapper.scan(Item::class.java, scanExpression)
        } catch (e: Exception) {
            Log.e("DynamoAccessService", "Error fetching items: ${e.message}", e)
            emptyList()
        }
    }

    private fun validateInput(type: String, tableName: String) {
        val validPattern = Regex("^[a-zA-Z0-9_-]+$")
        if (!validPattern.matches(type))
            throw IllegalArgumentException("Invalid type parameter")
        if (!validPattern.matches(tableName))
            throw IllegalArgumentException("Invalid tableName parameter")
    }

    suspend fun getNextId(tableArea: String): Int = withContext(Dispatchers.IO) {
        val random = SecureRandom()
        var newId: Int
        do {
            newId = random.nextInt(900000000) + 100000000 // 9-digit range
            val existingItem = dynamoDBMapper.load(Item::class.java, tableArea, newId)
        } while (existingItem != null) // Repeat if ID is taken
        newId
    }


}