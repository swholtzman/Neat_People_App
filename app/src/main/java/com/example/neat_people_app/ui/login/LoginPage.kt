package com.example.neat_people_app.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neat_people_app.ui.login.components.InputArea
import com.example.neat_people_app.ui.login.components.SubmissionButton
import com.example.neat_people_app.ui.theme.JostFontFamily
import com.example.neat_people_app.ui.theme.White

@Composable
fun LoginContent(loginViewModel: LoginViewModel, onLoginSuccess: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "REGISTER / LOGIN",
                fontFamily = JostFontFamily,
                fontSize = 24.sp,
                color = White
            )

            Spacer(modifier = Modifier.height(32.dp))

            InputArea(
                value = loginViewModel.email,
                onValueChange = { loginViewModel.onEmailChange(it) },
                placeholder = "Email ID",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            InputArea(
                value = loginViewModel.password,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                placeholder = "Password",
                isPassword = true,
                passwordVisible = loginViewModel.passwordVisible,
                onTogglePasswordVisibility = { loginViewModel.togglePasswordVisibility() },
                modifier = Modifier.fillMaxWidth()
            )

            if (loginViewModel.showVerificationInput) {
                Spacer(modifier = Modifier.height(16.dp))
                InputArea(
                    value = loginViewModel.verificationCode,
                    onValueChange = { loginViewModel.onVerificationCodeChange(it) },
                    placeholder = "Verification Code",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                SubmissionButton(
                    text = "Confirm",
                    onClick = { loginViewModel.confirmRegistration() },
                    enabled = !loginViewModel.isLoading
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            loginViewModel.errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SubmissionButton(
                    text = "Register",
                    onClick = { loginViewModel.register() },
                    enabled = !loginViewModel.isLoading
                )
                SubmissionButton(
                    text = "Login",
                    onClick = { loginViewModel.login(onLoginSuccess) },
                    enabled = !loginViewModel.isLoading
                )
            }
        }
    }
}