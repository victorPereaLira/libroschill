package com.proyecto.libroschill.features.books.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.proyecto.libroschill.data.repository.BooksRepository
import com.proyecto.libroschill.data.model.Volume
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecommendationsViewModel(private val repository: BooksRepository) : ViewModel() {
    private val _recs = MutableStateFlow<List<Volume>>(emptyList())
    val recs: StateFlow<List<Volume>> = _recs.asStateFlow()

    fun loadRecommendations(userId: String, apiKey: String, selectedLanguage: String) {
        viewModelScope.launch {
            try {
                val result = repository.getRecommendedBooks(userId, apiKey, selectedLanguage)
                _recs.value = result
            } catch (e: Exception) {
                _recs.value = emptyList()
            }
        }
    }
}

class RecommendationsViewModelFactory(
    private val repository: BooksRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecommendationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecommendationsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}