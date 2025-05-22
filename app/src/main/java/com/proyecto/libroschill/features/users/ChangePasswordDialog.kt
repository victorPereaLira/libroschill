package com.proyecto.libroschill.features.users


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ChangePasswordDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit
) {
    if (!showDialog) return

    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cambiar contraseña") },
        text = {
            Column {
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Nueva contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true
                )
                if (!errorMessage.isNullOrEmpty()) {
                    Text(errorMessage ?: "", color = Color.Red)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (newPassword.length < 6) {
                    errorMessage = "Debe tener al menos 6 caracteres"
                    return@TextButton
                }
                if (newPassword != confirmPassword) {
                    errorMessage = "Las contraseñas no coinciden"
                    return@TextButton
                }

                FirebaseAuth.getInstance().currentUser?.updatePassword(newPassword)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onDismiss()
                        } else {
                            errorMessage = "Error: Vuelve a iniciar sesión"
                        }
                    }
            }) {
                Text("Cambiar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}