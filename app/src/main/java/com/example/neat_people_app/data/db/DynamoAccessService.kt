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

class DynamoAccessService(context: Context) {
    fun testNetwork() {
        try {
            val url = URL("https://www.google.com")
            val connection = url.openConnection()
            connection.connect()
            Log.d("NetworkTest", "Connected to Google successfully")
        } catch (e: Exception) {
            Log.e("NetworkTest", "Failed to connect: ${e.message}")
        }
    }

    private val credentialsProvider = CognitoCachingCredentialsProvider(
        context,
        Secrets.COGNITO_IDENTITY_POOL_ID,
        Regions.fromName(Secrets.DYNAMO_DB_REGION)
    )

    private val dynamoDBClient = AmazonDynamoDBClient(credentialsProvider).apply {
//        testNetwork()
        setRegion(Region.getRegion(Regions.fromName(Secrets.DYNAMO_DB_REGION)))
        Log.d("DynamoAccessService", "Using region: ${Secrets.DYNAMO_DB_REGION}")
    }

    val dynamoDBMapper = DynamoDBMapper(dynamoDBClient)

    fun createItem(item: Item) {
        dynamoDBMapper.save(item)
    }

    fun getItem(type: String, tableName: String, itemId: String, storeName: String, storeId: String): Item? {
        validateInput(type, tableName)
        return dynamoDBMapper.load(Item::class.java, itemId)
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
                if (lastEvalKey != null) {
                    // pagination logic when in prod (parse lastEvalKey for exclusiveStartKey)
                }
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

    fun updateItem(item: Item) {
        dynamoDBMapper.save(item)
    }

    fun deleteItem(id: Int) {
        val item = Item().apply { this.id = id }
        dynamoDBMapper.delete(item)
    }

    private fun validateInput(type: String, tableName: String) {
        val validPattern = Regex("^[a-zA-Z0-9_-]+$")
        if (!validPattern.matches(type)) throw IllegalArgumentException("Invalid type parameter")
        if (!validPattern.matches(tableName)) throw IllegalArgumentException("Invalid tableName parameter")
    }
}