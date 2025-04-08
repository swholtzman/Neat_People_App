package com.example.neat_people_app

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.neat_people_app.data.auth.CognitoAuthService
import com.example.neat_people_app.data.db.DynamoAccessService
import com.example.neat_people_app.ui.content.AuthPages
import com.example.neat_people_app.ui.components.BottomNav
import com.example.neat_people_app.ui.components.ComingSoonPopup
import com.example.neat_people_app.ui.content.ContentPage
import com.example.neat_people_app.ui.content.ContentViewModel
import com.example.neat_people_app.ui.content.ContentViewModelFactory
import com.example.neat_people_app.ui.content.ItemCreationPage
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

    val navController = rememberNavController()
    val viewModel: ContentViewModel = viewModel(factory = ContentViewModelFactory(dynamoAccessService))
    val userName = "Unnamed User" // Replace with actual username if available
    var showComingSoon by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = "auth_check") {
            composable("auth_check") {
                LaunchedEffect(Unit) {
                    if (cognitoService.getSessionToken() != null) {
                        navController.navigate("content") {
                            popUpTo("auth_check") { inclusive = true }
                        }
                    } else {
                        navController.navigate("landing") {
                            popUpTo("auth_check") { inclusive = true }
                        }
                    }
                }
            }
            composable("landing") {
                AuthPages(
                    currentScreen = "landing",
                    onContinue = { navController.navigate("login") },
                    loginViewModel = viewModel(factory = LoginViewModelFactory(application, cognitoService)),
                    onLoginSuccess = { navController.navigate("content") }
                )
            }
            composable("login") {
                AuthPages(
                    currentScreen = "login",
                    onContinue = { /* Not needed here */ },
                    loginViewModel = viewModel(factory = LoginViewModelFactory(application, cognitoService)),
                    onLoginSuccess = { navController.navigate("content") }
                )
            }
            composable("content") {
                ContentPage(
                    navController = navController,
                    viewModel = viewModel,
                    userName = userName
                )
            }
            composable(
                route = "item_creation/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.StringType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId")
                val item = if (itemId == "new") null else viewModel.items.find { it.id.toString() == itemId }
                ItemCreationPage(
                    viewModel = viewModel,
                    item = item,
                    onSave = { navController.popBackStack() },
                    onCancel = { navController.popBackStack() },
                    userName = userName
                )
            }
        }

        // Observe the current destination to show/hide BottomNav
        val currentDestination by navController.currentBackStackEntryAsState()
        val currentScreen = currentDestination?.destination?.route ?: "auth_check"

        if (currentScreen !in listOf("landing", "login", "auth_check")) {
            BottomNav(
                currentScreen = currentScreen,
                onScreenSelected = { newScreen -> navController.navigate(newScreen) },
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