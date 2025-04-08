package com.example.neat_people_app.data.model

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBDocument
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable

@DynamoDBTable(tableName = "comp3717")
data class Item(
    @DynamoDBHashKey(attributeName = "table_area")
    var tableArea: String = "item",
    @DynamoDBRangeKey(attributeName = "id")
    var id: Int = 0,
    @DynamoDBAttribute(attributeName = "item")
    var details: ItemDetails = ItemDetails()
)

@DynamoDBDocument
data class ItemDetails(
    @DynamoDBAttribute(attributeName = "name")
    var name: String = "",
    @DynamoDBAttribute(attributeName = "description")
    var description: String = "",
    @DynamoDBAttribute(attributeName = "price")
    var price: String = "",
    @DynamoDBAttribute(attributeName = "buyPrice")
    var buyPrice: String = "",
    @DynamoDBAttribute(attributeName = "inStore")
    var inStore: Boolean = true,
    @DynamoDBAttribute(attributeName = "online")
    var online: Boolean = false,
    @DynamoDBAttribute(attributeName = "sellerId")
    var sellerId: String = "",
    @DynamoDBAttribute(attributeName = "uploadDate")
    var uploadDate: String = "",
    @DynamoDBAttribute(attributeName = "tags")
    var tags: List<String> = emptyList(),
    @DynamoDBAttribute(attributeName = "numInStock")
    var numInStock: List<StockItem> = emptyList()
)

@DynamoDBDocument
data class StockItem(
    @DynamoDBAttribute(attributeName = "id")
    var id: String = "",
    @DynamoDBAttribute(attributeName = "amount")
    var amount: String = "",
    @DynamoDBAttribute(attributeName = "colour")
    var colour: String = "",
    @DynamoDBAttribute(attributeName = "size")
    var size: String = ""
)