package com.example.neat_people_app

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.neat_people_app.data.auth.CognitoAuthService
import com.example.neat_people_app.data.db.DynamoAccessService
import com.example.neat_people_app.ui.AuthPages
import com.example.neat_people_app.ui.components.BottomNav
import com.example.neat_people_app.ui.components.ComingSoonPopup
import com.example.neat_people_app.ui.content.ContentPage
import com.example.neat_people_app.ui.login.LoginViewModel
import com.example.neat_people_app.ui.theme.NeatPeopleTheme
import com.example.neat_people_app.ui.theme.ThemeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            NeatPeopleTheme(darkTheme = themeViewModel.isDarkTheme) {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val cognitoService = CognitoAuthService(context)
    val dynamoAccessService = DynamoAccessService(context)

    val initialScreen = if (cognitoService.getSessionToken() != null) "content" else "landing"
    var currentScreen by remember { mutableStateOf(initialScreen) }
    var showComingSoon by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        when (currentScreen) {
            in listOf("landing", "login") -> {
                val loginViewModel: LoginViewModel = viewModel(
                    factory = LoginViewModelFactory(application, cognitoService)
                )
                AuthPages(
                    currentScreen = currentScreen,
                    onContinue = { currentScreen = "login" },
                    loginViewModel = loginViewModel,
                    onLoginSuccess = { currentScreen = "content" }
                )
            }
            "content" -> ContentPage(dynamoAccessService = dynamoAccessService)
        }

        if (currentScreen !in listOf("landing", "login")) {
            BottomNav(
                currentScreen = currentScreen,
                onScreenSelected = { newScreen -> currentScreen = newScreen },
                onComingSoonClick = { showComingSoon = true },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        ComingSoonPopup(
            isVisible = showComingSoon,
            onDismiss = { showComingSoon = false }
        )
    }
}

class LoginViewModelFactory(
    private val application: Application,
    private val cognitoService: CognitoAuthService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(application, cognitoService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}