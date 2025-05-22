package com.proyecto.libroschill.features.comments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CommentItem(comment: BookComment) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = comment.userName, style = MaterialTheme.typography.labelSmall)
        Text(text = comment.content, style = MaterialTheme.typography.bodyMedium)
    }
}