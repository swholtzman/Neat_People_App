package com.example.neat_people_app.ui.login.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neat_people_app.ui.theme.JostFontFamily
import com.example.neat_people_app.ui.theme.White

@Composable
fun SubmissionButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        border = BorderStroke(1.dp, White),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = White),
        modifier = modifier
    ) {
        Text(text, fontFamily = JostFontFamily)
    }
}