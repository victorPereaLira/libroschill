package com.proyecto.libroschill.data.repository

import com.google.firebase.firestore.FirebaseFirestore

fun getUserNickname(userId: String, onResult: (String) -> Unit) {
    FirebaseFirestore.getInstance()
        .collection("users")
        .document(userId)
        .get()
        .addOnSuccessListener { document ->
            val nickname = document.getString("nickname") ?: "Anónimo"
            onResult(nickname)
        }
        .addOnFailureListener {
            onResult("Anónimo")
        }
}