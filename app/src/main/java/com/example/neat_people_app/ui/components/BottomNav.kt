package com.example.neat_people_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neat_people_app.ui.theme.JostFontFamily

@Composable
fun BottomNav(
    currentScreen: String,
    onScreenSelected: (String) -> Unit,
    onComingSoonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        "content" to Icons.Default.Home,
        "Analytics" to Icons.Default.BarChart,
        "Orders" to Icons.Default.ShoppingCart,
        "Profile" to Icons.Default.Person
    )

    val secondaryOpacity = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    drawLine(
                        color = secondaryOpacity,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = strokeWidth
                    )
                }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEach { (label, icon) ->
                val isSelected = label == currentScreen
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            if (isSelected) {
                                // Do nothing or refresh the current screen (optional)
                            } else if (label == "content") {
                                onScreenSelected(label)
                            } else {
                                onComingSoonClick()
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = label,
                        color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onPrimary,
                        fontFamily = JostFontFamily,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}