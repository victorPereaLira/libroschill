package com.proyecto.libroschill.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.proyecto.libroschill.features.comments.BookComment


fun listenToComments(bookId: String, onCommentsLoaded: (List<BookComment>) -> Unit) {
    FirebaseFirestore.getInstance()
        .collection("books")
        .document(bookId)
        .collection("comments")
        .orderBy("timestamp", Query.Direction.ASCENDING)
        .addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener

            val comments = snapshot.documents.mapNotNull { doc ->
                doc.toObject(BookComment::class.java)
            }

            onCommentsLoaded(comments)
        }
}

fun addComment(bookId: String, userId: String, userName: String, content: String) {
    val commentData = mapOf(
        "userId" to userId,
        "userName" to userName,
        "content" to content,
        "timestamp" to FieldValue.serverTimestamp()
    )

    FirebaseFirestore.getInstance()
        .collection("books")
        .document(bookId)
        .collection("comments")
        .add(commentData)
}