package com.proyecto.libroschill.presentation.signup

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.libroschill.R
import com.proyecto.libroschill.ui.theme.DarkBlueGradient
import com.proyecto.libroschill.ui.theme.MediumBlue

@Composable
fun SignupScreen(auth: FirebaseAuth, navigationToHome: () -> Unit) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isFormValid by remember(email, password) {
        derivedStateOf { email.isNotBlank() && password.isNotBlank() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(listOf(DarkBlueGradient, MediumBlue))
            )
            .padding(horizontal = 32.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "back",
            modifier = Modifier.padding(top = 16.dp)
        )

        Text("Email")
        TextField(value = email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        Text("Password")
        TextField(value = password, onValueChange = { password = it }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            context, "Registro exitoso", Toast.LENGTH_SHORT
                        ).show()
                        navigationToHome()
                    } else {
                        Toast.makeText(
                            context, "Error en el registro", Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}