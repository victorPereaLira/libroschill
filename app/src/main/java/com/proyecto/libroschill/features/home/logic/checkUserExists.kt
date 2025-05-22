package com.proyecto.libroschill.features.home.logic

import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun checkUserExists(auth: FirebaseAuth, navHostController: NavHostController) {
    val user = auth.currentUser
    user?.reload()?.addOnSuccessListener {
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) {
                    auth.signOut()
                    navHostController.navigate("initial") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }
    }
}