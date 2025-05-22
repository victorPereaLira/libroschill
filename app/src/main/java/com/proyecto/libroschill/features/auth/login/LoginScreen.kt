package com.proyecto.libroschill.features.auth.login

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.proyecto.libroschill.features.auth.login.components.LoginBackground
import com.proyecto.libroschill.features.auth.login.components.LoginForm
import com.proyecto.libroschill.features.auth.login.components.LogoHeader
import com.proyecto.libroschill.features.auth.viewmodel.AuthViewModel


@Composable
fun LoginScreen(authViewModel: AuthViewModel, navigationToHome: () -> Unit) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LoginBackground {
        LogoHeader()

        LoginForm(
            email = email,
            onEmailChange = { email = it },
            password = password,
            onPasswordChange = { password = it },
            passwordVisible = passwordVisible,
            onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
            isFormValid = email.isNotBlank() && password.isNotBlank(),
            onLoginClick = {
                authViewModel.login(
                    email = email,
                    password = password,
                    onSuccess = navigationToHome,
                    onError = { error ->
                        Toast.makeText(
                            context,
                            error.message ?: "Error al iniciar sesión",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        )
    }
}