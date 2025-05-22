package com.proyecto.libroschill.features.auth.viewmodel


import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class AuthViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isLoggedIn.value = true
                    onSuccess()
                } else {
                    onError(task.exception ?: Exception("Error al iniciar sesión"))
                }
            }
    }

    fun signup(
        email: String,
        password: String,
        nickname: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        val userData = mapOf(
                            "uid" to user.uid,
                            "email" to email,
                            "nickname" to nickname
                        )

                        FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(user.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                _isLoggedIn.value = true
                                onSuccess()
                            }
                            .addOnFailureListener { error ->
                                onError(error.message ?: "Error al guardar el perfil")
                            }
                    } else {
                        onError("Usuario no encontrado después del registro")
                    }
                } else {
                    onError(task.exception?.message ?: "Error al registrarse")
                }
            }
    }
}