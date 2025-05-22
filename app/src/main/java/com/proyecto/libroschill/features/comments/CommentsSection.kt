package com.proyecto.libroschill.features.comments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.libroschill.R
import com.proyecto.libroschill.data.repository.getUserNickname

@Composable
fun CommentsSection(
    bookId: String,
    currentUserId: String,
    viewModel: CommentsViewModel = viewModel()
) {
    val comments = viewModel.comments
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage
    val commentText = viewModel.commentText

    var userName by remember { mutableStateOf("Anónimo") }

    // Cargar el nickname del usuario desde Firestore
    LaunchedEffect(currentUserId) {
        getUserNickname(currentUserId) { nickname ->
            userName = nickname
        }
    }

    // Cargar los comentarios del libro
    LaunchedEffect(bookId) {
        viewModel.loadComments(bookId)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = stringResource(R.string.comentarios), style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        if (comments.isEmpty()) {
            Text(stringResource(R.string.NoComentarios), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        } else {
            LazyColumn(modifier = Modifier.heightIn(max = 250.dp)) {
                items(comments) { CommentItem(it) }
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = commentText,
                onValueChange = { viewModel.commentText = it },
                placeholder = { Text(stringResource(R.string.EscribeComentario)) },
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(8.dp))

            Button(
                onClick = {
                    viewModel.sendComment(bookId, currentUserId, userName)
                },
                enabled = !isLoading && commentText.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                } else {
                    Text(stringResource(R.string.enviar))
                }
            }
        }

        error?.let {
            Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }
    }
}