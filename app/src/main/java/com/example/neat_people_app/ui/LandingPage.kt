package com.example.neat_people_app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neat_people_app.ui.theme.AppTypography
import com.example.neat_people_app.ui.theme.JostFontFamily
import com.example.neat_people_app.ui.theme.PurpleBrightStop1
import com.example.neat_people_app.ui.theme.PurpleBrightStop2
import com.example.neat_people_app.ui.theme.PurpleBrightStop3
import com.example.neat_people_app.ui.theme.PurpleBrightStop4
import com.example.neat_people_app.ui.theme.ShadowBlack50
import com.example.neat_people_app.ui.theme.ShadowBlue25
import com.example.neat_people_app.ui.theme.White

@Composable
fun LandingContent(onContinue: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy((-100).dp)
            ) {
                Text(
                    text = "Neat",
                    style = AppTypography.bodyLarge.copy(
                        fontSize = 125.sp,
                        fontWeight = FontWeight.W600
                    ),
                    color = Color(0xFFFFFFF1)
                )
                Text(
                    text = "People",
                    style = AppTypography.bodyLarge.copy(
                        fontSize = 125.sp,
                        fontWeight = FontWeight.W600
                    ),
                    color = Color(0xFFFFFFF1)
                )
            }

            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .drawBehind {
                        val brush = Brush.radialGradient(
                            colors = listOf(
                                PurpleBrightStop1,
                                PurpleBrightStop2,
                                PurpleBrightStop3,
                                PurpleBrightStop4
                            ),
                            center = Offset(size.width / 2f, size.height * 1.0203f),
                            radius = size.height * 2.4025f
                        )
                        drawRect(brush)
                    }
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(50.dp),
                        ambientColor = ShadowBlack50,
                        spotColor = ShadowBlack50
                    )
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(50.dp),
                        ambientColor = ShadowBlue25,
                        spotColor = ShadowBlue25
                    )
                    .clickable { onContinue() },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Continue",
                        color = White,
                        fontFamily = JostFontFamily,
                        fontSize = 20.sp
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 16.dp)
                            .size(40.dp)
                            .background(Color(0x7531335E), shape = CircleShape)
                            .border(0.5.dp, Color(0x35FFFFFF), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Continue",
                            tint = White
                        )
                    }
                }
            }
        }

        Button(
            onClick = { /* TODO: add contact form for potential clients */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .offset(x = (-15).dp)
                .width(150.dp)
                .height(50.dp)
                .border(1.dp, White, RoundedCornerShape(50.dp)),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Text(
                text = "Contact Us",
                color = White,
                fontFamily = JostFontFamily,
                fontSize = 20.sp
            )
        }
    }
}