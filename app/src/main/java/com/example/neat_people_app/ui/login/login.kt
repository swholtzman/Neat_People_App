package com.example.neat_people_app.ui.login

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.example.neat_people_app.data.auth.CognitoAuthService
import kotlinx.coroutines.launch

class LoginViewModel(
    application: Application,
    private val cognitoService: CognitoAuthService
) : AndroidViewModel(application) {
//    private val cognitoService = CognitoAuthService(application)

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var passwordVisible by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var showVerificationInput by mutableStateOf(false)
        private set
    var verificationCode by mutableStateOf("")
        private set

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun onVerificationCodeChange(newCode: String) {
        verificationCode = newCode
    }

    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }

    fun register() {
        if (!isValidEmail(email)) {
            errorMessage = "Invalid email format"
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = cognitoService.signUp(email, password)
            isLoading = false
            if (result.isSuccess) {
                showVerificationInput = true
                message = "Registration successful. " +
                        "Please enter the verification code sent to your email."
                messageColour = Color.White
            } else {
                val exception = result.exceptionOrNull()
                errorMessage = when {
                    exception?.message?.contains("InvalidPasswordException") == true -> {
                        "Please include a number and special character in your password"
                    }
                    exception?.message?.contains("UsernameExistsException") == true -> {
                        "User ID already exists"
                    }
                    else -> {
                        "Invalid or empty password"
                    }
                }
            }
        }
    }

    fun confirmRegistration() {
        if (verificationCode.isEmpty()) {
            errorMessage = "Please enter the verification code"
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val user = cognitoService.userPool.getUser(email)

            user.confirmSignUpInBackground(
                verificationCode, false,
                object : GenericHandler
                {

                override fun onSuccess() {
                    showVerificationInput = false
                    message = "Account confirmed. Please log in."
                    messageColour = Color.White
                    isLoading = false
                }

                override fun onFailure(exception: Exception?) {
                    isLoading = false
                    when {
                        exception?.message?.contains("CodeMismatchException") == true -> {
                            errorMessage = "Invalid verification code. Please try again."
                        }
                        exception?.message?.contains("ExpiredCodeException") == true -> {
                            errorMessage = "Verification code has expired. " +
                                    "Please request a new one."
                        }
                        else -> {
                            errorMessage = "Confirmation failed. Please try again."
                        }
                    }
                }
            })
        }
    }

    fun login(onLoginSuccess: () -> Unit) {
        if (!isValidEmail(email)) {
            errorMessage = "Please enter a valid email address"
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = cognitoService.login(email, password)
            isLoading = false
            if (result.isSuccess) {
                result.getOrNull()?.let { session ->
                    cognitoService.storeSessionToken(session)
                }
                onLoginSuccess()
            } else {
                val exception = result.exceptionOrNull()
                when {
                    exception?.message?.contains("UserNotConfirmedException") == true -> {
                        showVerificationInput = true
                        errorMessage = "Your account is not confirmed. Please enter the verification code sent to your email."
                    }
                    exception?.message?.contains("Incorrect username or password") == true -> {
                        errorMessage = "Incorrect email or password. Please try again."
                    }
                    else -> {
                        errorMessage = "Login failed. Please check your connection and try again."
                    }
                }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
        return regex.matches(email)
    }

    var message by mutableStateOf<String?>(null)
        private set

    var messageColour by mutableStateOf(Color.Unspecified)
        private set
}