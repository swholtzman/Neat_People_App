package com.example.neat_people_app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.neat_people_app.R

val JostFontFamily = FontFamily(
    Font(R.font.jost_regular),
    Font(R.font.jost_bold, FontWeight.Bold)
)

val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = JostFontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
    ),
    bodyMedium = TextStyle(
        fontFamily = JostFontFamily,
        fontSize = 14.sp
    ),
)

// Light Mode Colors
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFFFFFF), // White
    onPrimary = Color(0xFF000000), // Black
    secondary = Color(0xFF000000), // Black
    onSecondary = Color(0xFFFFFFFF), // White
    tertiary = Color(0xFF222222), // Dark Gray
    onTertiary = Color(0xFFFFFFFF), // White
    background = Color(0xFFFFFFFF), // White
    onBackground = Color(0xFF000000), // Black
    surface = Color(0xFFFFFFFF), // White
    onSurface = Color(0xFF000000)  // Black
)

// Dark Mode Colors
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF131313), // Dark Gray
    onPrimary = Color(0xFFFFFFFF), // White
    secondary = Color(0xFFFFFFFF), // White
    onSecondary = Color(0xFF000000), // Black
    tertiary = Color(0xFFDDDDDD), // Light Gray
    onTertiary = Color(0xFF000000), // Black
    background = Color(0xFF131313), // Dark Gray
    onBackground = Color(0xFFFFFFFF), // White
    surface = Color(0xFF131313), // Dark Gray
    onSurface = Color(0xFFFFFFFF)  // White
)

@Composable
fun NeatPeopleTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}