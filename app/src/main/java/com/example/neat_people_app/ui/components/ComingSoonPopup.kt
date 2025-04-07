package com.example.neat_people_app.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.neat_people_app.ui.theme.JostFontFamily

@Composable
fun ComingSoonPopup(
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.40f))
                .clickable(
                    onClick = { onDismiss() },
                    indication = null,
                    interactionSource = interactionSource
                )
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec =
                    tween(durationMillis = 600, easing = FastOutSlowInEasing)),
                exit = fadeOut(animationSpec =
                    tween(durationMillis = 600, easing = FastOutSlowInEasing)),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .border(1.dp, MaterialTheme.colorScheme.secondary,
                            RoundedCornerShape(16.dp))

                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    Text(
                        text = "Coming Soon!",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = JostFontFamily
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "This is a concept app with a long way to go. " +
                                "Items like this will be implemented soon! " +
                                "Please try again another time.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = JostFontFamily
                        ),

                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}