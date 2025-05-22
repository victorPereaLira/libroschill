package com.proyecto.libroschill.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.libroschill.data.model.Volume
import com.proyecto.libroschill.core.network.RetrofitInstance
import kotlinx.coroutines.tasks.await

class BooksRepository {
    private val apiService = RetrofitInstance.api
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun searchBooks(query: String, apikey: String): List<Volume> {
        val response = apiService.searchBooks(query, apikey)
        return response.items
    }


    suspend fun getLikedBooks(userId: String, apiKey: String): List<Volume> {
        return try {
            val snapshot = firestore.collection("user_likes")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val bookIds = snapshot.documents.mapNotNull { it.getString("bookId") }

            bookIds.mapNotNull { bookId ->
                try {
                    apiService.getBookDetails(bookId, apiKey)
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getBooksByStatus(userId: String, status: String, apiKey: String): List<Volume> {
        return try {
            val snapshot = firestore.collection("user_read_status")
                .whereEqualTo("userId", userId)
                .whereEqualTo("status", status)
                .get()
                .await()

            val bookIds = snapshot.documents.mapNotNull { it.getString("bookId") }

            bookIds.mapNotNull { bookId ->
                try {
                    apiService.getBookDetails(bookId, apiKey)
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getRecommendedBooks(
        userId: String,
        apiKey: String,
        language: String,
        maxPerCategory: Int = 10
    ): List<Volume> {
        Log.d("BooksRepository", "Recomendaciones para usuario: $userId en idioma $language")

        val likedIds = firestore.collection("user_likes")
            .whereEqualTo("userId", userId)
            .get().await().documents.mapNotNull { it.getString("bookId") }

        val readIds = firestore.collection("user_read_status")
            .whereEqualTo("userId", userId)
            .get().await().documents.mapNotNull { it.getString("bookId") }

        val knownIds = (likedIds + readIds).toSet()
        if (knownIds.isEmpty()) {
            Log.d("BooksRepository", "No hay libros marcados como leídos o favoritos")
            return emptyList()
        }

        val knownVolumes = knownIds.mapNotNull { id ->
            try {
                apiService.getBookDetails(id, apiKey)
            } catch (e: Exception) {
                Log.d("BooksRepository", "Error detalle $id: ${e.message}")
                null
            }
        }

        if (knownVolumes.isEmpty()) {
            Log.d("BooksRepository", "No se pudo obtener ningún volumen")
            return emptyList()
        }

        val topCategories = knownVolumes
            .flatMap { it.volumeInfo.categories ?: emptyList() }
            .groupingBy { it }
            .eachCount()
            .entries.sortedByDescending { it.value }
            .take(3)
            .map { it.key }

        val recs = mutableListOf<Volume>()

        if (topCategories.isNotEmpty()) {
            Log.d("BooksRepository", "Categorías más frecuentes: $topCategories")
            for (cat in topCategories) {
                try {
                    val result = apiService.searchBooks("subject:$cat", apiKey, language)
                    recs += result.items.take(maxPerCategory)
                } catch (e: Exception) {
                    println("Error buscando en categoría '$cat': ${e.message}")
                }
            }
        } else {
            val fallbackAuthor = knownVolumes.firstOrNull()?.volumeInfo?.authors?.firstOrNull()
            if (fallbackAuthor != null) {
                println("Sin categorías. Buscando por autor: $fallbackAuthor")
                try {
                    val result = apiService.searchBooks("inauthor:$fallbackAuthor", apiKey, language)
                    recs += result.items.take(maxPerCategory)
                } catch (e: Exception) {
                    println("Error buscando por autor '$fallbackAuthor': ${e.message}")
                }
            } else {
                println("No se pudo obtener autor de ningún libro")
            }
        }

        return recs
            .distinctBy { it.id }
            .filterNot { knownIds.contains(it.id) }
    }
}