package com.example.neat_people_app.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neat_people_app.R
import com.example.neat_people_app.ui.login.LoginContent
import com.example.neat_people_app.ui.login.LoginViewModel
import com.example.neat_people_app.ui.theme.JostFontFamily
import com.example.neat_people_app.ui.theme.PurpleBrightStop1
import com.example.neat_people_app.ui.theme.PurpleBrightStop2
import com.example.neat_people_app.ui.theme.PurpleBrightStop4
import com.example.neat_people_app.ui.theme.White

@Composable
fun AuthPages(
    currentScreen: String,
    onContinue: () -> Unit,
    loginViewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                val brush = Brush.radialGradient(
                    colors = listOf(
                        PurpleBrightStop1,
                        PurpleBrightStop2,
                        PurpleBrightStop4
                    ),
                    center = Offset(size.width / 2f, size.height),
                    radius = size.width,
                    tileMode = TileMode.Clamp
                )
                drawRect(brush)
            }
    ) {
        // Navigation Bar
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .padding(15.dp)
                .safeDrawingPadding(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "NP",
                fontFamily = JostFontFamily,
                color = White,
                fontSize = 32.sp
            )
            Image(
                painter = painterResource(id = R.drawable.hamburger),
                contentDescription = "Hamburger Menu",
                modifier = Modifier.size(45.dp)
            )
        }

        // Animated Content
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                if (targetState == "login" && initialState == "landing") {
                    (slideInHorizontally { width -> width } + fadeIn()) togetherWith
                            (slideOutHorizontally { width -> -width } + fadeOut())
                } else {
                    EnterTransition.None togetherWith ExitTransition.None
                }
            },
            modifier = Modifier.fillMaxSize(), label = ""
        ) { screen ->
            when (screen) {
                "landing" -> LandingContent(onContinue = onContinue)
                "login" -> LoginContent(
                    loginViewModel = loginViewModel,
                    onLoginSuccess = onLoginSuccess
                )
            }
        }
    }
}