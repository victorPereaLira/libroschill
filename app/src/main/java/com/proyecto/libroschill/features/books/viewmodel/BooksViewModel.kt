package com.proyecto.libroschill.features.books.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.libroschill.data.repository.BooksRepository
import com.proyecto.libroschill.data.model.Volume
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class BooksViewModel(private val repository: BooksRepository) : ViewModel() {
    var books by mutableStateOf<List<Volume>>(emptyList())
        private set
    var likedBookIds by mutableStateOf(setOf<String>())
        private set
    var loadingLikes by mutableStateOf(setOf<String>())

    var readBookIds by mutableStateOf(setOf<String>())
        private set
    var pendingBookIds by mutableStateOf(setOf<String>())
        private set

    fun loadLikedBooks(userId: String) {
        viewModelScope.launch {
            try {
                val snapshot = FirebaseFirestore.getInstance()
                    .collection("user_likes")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                likedBookIds = snapshot.documents.mapNotNull {
                    it.getString("bookId")
                }.toSet()
            } catch (e: Exception) {
                likedBookIds = emptySet()
            }
        }
    }

    fun toggleLike(userId: String, bookId: String) {
        if (bookId.isBlank()) return
        viewModelScope.launch {
            loadingLikes = loadingLikes + bookId
            val docRef = FirebaseFirestore.getInstance()
                .collection("user_likes")
                .document("${userId}_$bookId")

            val isLiked = likedBookIds.contains(bookId)

            try {
                if (isLiked) {
                    docRef.delete().await()
                    likedBookIds = likedBookIds - bookId
                } else {
                    docRef.set(
                        mapOf(
                            "userId" to userId,
                            "bookId" to bookId,
                            "timestamp" to FieldValue.serverTimestamp()
                        )
                    ).await()
                    likedBookIds = likedBookIds + bookId
                }
            } catch (_: Exception) {
            } finally {
                loadingLikes = loadingLikes - bookId
            }
        }
    }

    fun searchBooks(query: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val result = repository.searchBooks(query, apiKey)
                books = result
                Log.d("BooksViewModel", "Resultados encontrados: ${result.size}")
            } catch (e: Exception) {
                Log.e("BooksViewModel", "Error en búsqueda", e)
                books = emptyList()
            }
        }
    }

    fun getLikedBooks(userId: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val result = repository.getLikedBooks(userId, apiKey)
                books = result
            } catch (e: Exception) {
                books = emptyList()
            }
        }
    }

    fun getReadBooks(userId: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val result = repository.getBooksByStatus(userId, "read", apiKey)
                books = result
                readBookIds = result.map { it.id }.toSet()
            } catch (e: Exception) {
                books = emptyList()
                readBookIds = emptySet()
            }
        }
    }
    fun getPendingBooks(userId: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val result = repository.getBooksByStatus(userId, "pending", apiKey)
                books = result
                pendingBookIds = result.map { it.id }.toSet()
            } catch (e: Exception) {
                books = emptyList()
                pendingBookIds = emptySet()
            }
        }
    }
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
                    // Si ya tiene ese estado, lo quitamos
                    docRef.delete().await()
                } else {
                    // Si no lo tiene o es distinto, lo actualizamos
                    docRef.set(
                        mapOf(
                            "userId" to userId,
                            "bookId" to bookId,
                            "status" to status,
                            "timestamp" to FieldValue.serverTimestamp()
                        )
                    ).await()
                }

            } catch (e: Exception) {

                Log.e("BooksViewModel", "Error al cambiar estado de lectura", e)
            }
        }
    }
    fun clearBooks(){
        books = emptyList()
    }
}