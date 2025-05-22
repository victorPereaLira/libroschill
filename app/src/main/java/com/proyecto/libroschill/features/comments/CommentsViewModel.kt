package com.proyecto.libroschill.features.comments

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.proyecto.libroschill.data.repository.addComment
import com.proyecto.libroschill.data.repository.listenToComments

class CommentsViewModel : ViewModel() {
    private val _comments = mutableStateListOf<BookComment>()
    val comments: List<BookComment> get() = _comments
    var commentText by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadComments(bookId: String) {
        listenToComments(bookId) { loaded ->
            _comments.clear()
            _comments.addAll(loaded)
        }
    }

    fun sendComment(bookId: String, userId: String, userName: String) {
        if (commentText.isBlank()) return

        isLoading = true
        addComment(
            bookId = bookId,
            userId = userId,
            userName = userName,
            content = commentText.trim()
        )
        commentText = ""
        isLoading = false
    }
}