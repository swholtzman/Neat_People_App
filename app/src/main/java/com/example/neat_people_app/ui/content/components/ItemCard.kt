package com.example.neat_people_app.ui.content.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.neat_people_app.data.model.Item
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ItemCard(item: Item, onItemClick: (Item) -> Unit) {
    val sellerName = "Unnamed Seller" // need to fetch name based on id; deal with later
    val userTag = getUserTag(sellerName)
    val truncatedName = truncateName(item.details.name)
    val price = formatPrice(item.details.price)
    val date = formatDate(item.details.uploadDate)

    val secondaryOpacity = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
    val tertiary = MaterialTheme.colorScheme.tertiary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(item) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp), // padding above card
            horizontalArrangement = Arrangement.Start
        ) {

            Row (
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = userTag,
                    color = secondaryOpacity,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = truncatedName,
                    color = tertiary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Row (
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = price,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = date,
                    color = secondaryOpacity,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(14.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Navigate to item details",
                    tint = tertiary,
                    modifier = Modifier.clickable { onItemClick(item) }
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp)) // padding between text and bottom border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .drawBehind {
                    val strokeWidth = 0.75.dp.toPx()
                    drawLine(
                        color = secondaryOpacity,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
        )
    }
}

// helper to generate user tag from seller name
// until linked, we use placeholder "Unnamed Seller"
private fun getUserTag(name: String): String {
    val parts = name.split(" ")
    return if (parts.size >= 2) {
        val first = parts[0].take(2)
        val last = parts[1].take(2)
        first + last
    } else {
        name.take(4)
    }
}

// helper to truncate item name to 16 characters
private fun truncateName(name: String): String {
    return if (name.length > 16) name.take(14) + "..." else name
}

// helper to format price as dollar amount with two decimal places
private fun formatPrice(price: String): String {
    return try {
        "$%.2f".format(price.toDouble())
    } catch (e: NumberFormatException) {
        "$0.00"
    }
}

// helper to format upload date to "MMM dd, yyyy"
private fun formatDate(dateStr: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateStr)
        outputFormat.format(date ?: return "Invalid Date")
    } catch (e: Exception) {
        "Invalid Date"
    }
}