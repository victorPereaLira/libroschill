package com.proyecto.libroschill.features.comments

import com.google.firebase.Timestamp

data class BookComment(
    val userId: String = "",
    val userName: String = "",
    val content: String = "",
    val timestamp: Timestamp? = null
)