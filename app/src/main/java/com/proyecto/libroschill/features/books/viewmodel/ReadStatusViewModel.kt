package com.proyecto.libroschill.features.books.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ReadStatusViewModel : ViewModel() {
    private val _readBookIds = MutableStateFlow(setOf<String>())
    val readBookIds: StateFlow<Set<String>> = _readBookIds.asStateFlow()

    private val _pendingBookIds = MutableStateFlow(setOf<String>())
    val pendingBookIds: StateFlow<Set<String>> = _pendingBookIds.asStateFlow()

    fun toggleReadStatus(userId: String, bookId: String, status: String) {
        if (bookId.isBlank()) return

        viewModelScope.launch {
            val docRef = FirebaseFirestore.getInstance()
                .collection("user_read_status")
                .document("${userId}_$bookId")

            try {
                val snapshot = docRef.get().await()
                val currentStatus = snapshot.getString("status")

                if (currentStatus == status) {
                    docRef.delete().await()
                } else {
                    docRef.set(
                        mapOf(
                            "userId" to userId,
                            "bookId" to bookId,
                            "status" to status,
                            "timestamp" to FieldValue.serverTimestamp()
                        )
                    ).await()
                }
                loadReadAndPendingBooks(userId)
            } catch (_: Exception) {}
        }
    }

    fun loadReadAndPendingBooks(userId: String) {
        viewModelScope.launch {
            try {
                val snapshot = FirebaseFirestore.getInstance()
                    .collection("user_read_status")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                _readBookIds.value = snapshot.documents
                    .filter { it.getString("status") == "read" }
                    .mapNotNull { it.getString("bookId") }
                    .toSet()

                _pendingBookIds.value = snapshot.documents
                    .filter { it.getString("status") == "pending" }
                    .mapNotNull { it.getString("bookId") }
                    .toSet()

            } catch (e: Exception) {
                _readBookIds.value = emptySet()
                _pendingBookIds.value = emptySet()
            }
        }
    }
}
