package com.example.neat_people_app.ui.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.neat_people_app.data.model.Item
import com.example.neat_people_app.data.model.ItemDetails
import com.example.neat_people_app.ui.content.components.UnderlinedTextField
import com.example.neat_people_app.ui.theme.JostFontFamily
import com.example.neat_people_app.utilities.NullableSellerSaver
import java.text.SimpleDateFormat
import java.util.*

data class Seller(val id: String, val name: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemCreationPage(
    viewModel: ContentViewModel,
    item: Item?,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    userName: String
) {
    // State variables declared at the top
    var coverPhotoSelected by remember { mutableStateOf(false) }
    var itemName by rememberSaveable { mutableStateOf(item?.details?.name ?: "") }
    var sellPrice by rememberSaveable { mutableStateOf(item?.details?.price ?: "") }
    var buyPrice by rememberSaveable { mutableStateOf(item?.details?.buyPrice ?: "") }
    var currency by rememberSaveable { mutableStateOf("CAD") }
    var inStore by rememberSaveable { mutableStateOf(item?.details?.inStore ?: false) }
    var online by rememberSaveable { mutableStateOf(item?.details?.online ?: false) }
    val sellers = listOf(Seller("1", "Seller One"), Seller("2", "Seller Two")) // Mock data
    var selectedSeller by rememberSaveable(stateSaver = NullableSellerSaver) {
        mutableStateOf(
            item?.details?.sellerId?.let { id -> sellers.find { it.id == id } } ?: sellers.firstOrNull()
        )
    }
    var description by rememberSaveable { mutableStateOf(item?.details?.description ?: "") }
    val predefinedTags = listOf(
        "Vintage", "Street", "Sneakers", "Femme", "Tees", "Sweaters",
        "Bottoms", "Accessories", "Jackets", "Jerseys", "Button-Up"
    )
    var selectedTags by rememberSaveable { mutableStateOf(item?.details?.tags?.toSet() ?: emptySet()) }
    var customTagInput by rememberSaveable { mutableStateOf("") }

    // Validation
    val isFormValid = itemName.isNotBlank() && sellPrice.isNotBlank() && (inStore || online) && selectedTags.isNotEmpty()

    var newItemId by remember { mutableStateOf(item?.id ?: 0) }

    // Fetch next ID for new items
    LaunchedEffect(Unit) {
        if (item == null) { // Only generate a new ID for new items
            newItemId = viewModel.getNextId("item")
        }
    }

    PageWithBars(userName = userName) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Cover Photo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .border(
                        BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)),
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable { coverPhotoSelected = !coverPhotoSelected },
                contentAlignment = Alignment.Center
            ) {
                if (!coverPhotoSelected) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add cover photo",
                        tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                        modifier = Modifier.size(48.dp)
                    )
                } else {
                    Text("Photo Selected", fontFamily = JostFontFamily)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Add cover photo",
                    fontFamily = JostFontFamily,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Item Name
            Text("Item Name *", fontFamily = JostFontFamily, style = MaterialTheme.typography.bodyLarge)
            UnderlinedTextField(
                value = itemName,
                onValueChange = { itemName = it },
                placeholder = "Item Name",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Prices Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Sell Price *", fontFamily = JostFontFamily, style = MaterialTheme.typography.bodyLarge)
                    UnderlinedTextField(
                        value = sellPrice,
                        onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) sellPrice = it },
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Buy Price", fontFamily = JostFontFamily, style = MaterialTheme.typography.bodyLarge)
                    UnderlinedTextField(
                        value = buyPrice,
                        onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) buyPrice = it },
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                var currencyExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = currencyExpanded,
                    onExpandedChange = { currencyExpanded = !currencyExpanded }
                ) {
                    Button(onClick = { /*currencyExpanded = true*/ }) {
                        Text(currency, fontFamily = JostFontFamily)
                    }
                    ExposedDropdownMenu(
                        expanded = currencyExpanded,
                        onDismissRequest = { currencyExpanded = false }
                    ) {
                        listOf("CAD", "USD", "EUR").forEach { curr ->
                            DropdownMenuItem(
                                text = { Text(curr, fontFamily = JostFontFamily) },
                                onClick = {
                                    currency = curr
                                    currencyExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // In Store, Online, Seller Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = inStore,
                        onClick = { inStore = !inStore },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.tertiary,
                            unselectedColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
                        )
                    )
                    Text("In Store", fontFamily = JostFontFamily)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = online,
                        onClick = { online = !online },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.tertiary,
                            unselectedColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
                        )
                    )
                    Text("Online", fontFamily = JostFontFamily)
                }
                var sellerExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = sellerExpanded,
                    onExpandedChange = { sellerExpanded = !sellerExpanded }
                ) {
                    Button(onClick = { sellerExpanded = true }) {
                        Text(selectedSeller?.name ?: "Select Seller", fontFamily = JostFontFamily)
                    }
                    ExposedDropdownMenu(
                        expanded = sellerExpanded,
                        onDismissRequest = { sellerExpanded = false }
                    ) {
                        sellers.forEach { seller ->
                            DropdownMenuItem(
                                text = { Text(seller.name, fontFamily = JostFontFamily) },
                                onClick = {
                                    selectedSeller = seller
                                    sellerExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Separator
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
            Spacer(modifier = Modifier.height(16.dp))

            // Item Description
            Text("Item Description", fontFamily = JostFontFamily, style = MaterialTheme.typography.bodyLarge)
            UnderlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = "Enter description",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Tags Section
            Text("Tags *", fontFamily = JostFontFamily, style = MaterialTheme.typography.bodyLarge)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                predefinedTags.forEach { tag ->
                    val isSelected = selectedTags.contains(tag)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedTags = if (isSelected) selectedTags - tag else selectedTags + tag
                        },
                        label = { Text(tag, fontFamily = JostFontFamily) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            labelColor = MaterialTheme.colorScheme.secondary,
                            selectedContainerColor = MaterialTheme.colorScheme.secondary,
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f))
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Custom Tag:", fontFamily = JostFontFamily)
                Spacer(modifier = Modifier.width(8.dp))
                UnderlinedTextField(
                    value = customTagInput,
                    onValueChange = { customTagInput = it },
                    placeholder = "Enter Custom Value",
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    if (customTagInput.isNotBlank()) {
                        selectedTags = selectedTags + customTagInput
                        customTagInput = ""
                    }
                }) {
                    Icon(Icons.Default.Add, "Add tag", tint = MaterialTheme.colorScheme.tertiary)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Separator
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
            Spacer(modifier = Modifier.height(8.dp))

            // Required Field Indicator and Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("* Required Field", fontFamily = JostFontFamily, style = MaterialTheme.typography.bodySmall)
                Button(
                    onClick = {
                        if (isFormValid) {
                            val currentDate = SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                                Locale.getDefault()
                            ).format(Date())
                            val newItem = Item(
                                tableArea = "item",
                                id = item?.id ?: newItemId,
                                details = ItemDetails(
                                    name = itemName,
                                    description = description,
                                    price = sellPrice,
                                    buyPrice = buyPrice,
                                    inStore = inStore,
                                    online = online,
                                    sellerId = selectedSeller?.id ?: "",
                                    uploadDate = item?.details?.uploadDate ?: currentDate,
                                    tags = selectedTags.toList()
                                )
                            )
                            if (item == null) {
                                viewModel.createItem(newItem)
                            } else {
                                viewModel.updateItem(newItem)
                            }
                            onSave()
                        }
                    },
                    enabled = isFormValid
                ) {
                    Text(
                        if (item == null) "Post to Shop" else "Save and Update Item",
                        fontFamily = JostFontFamily
                    )
                }
            }
        }
    }
}