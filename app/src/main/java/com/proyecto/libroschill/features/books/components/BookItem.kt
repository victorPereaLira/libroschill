package com.proyecto.libroschill.features.books.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.proyecto.libroschill.data.model.Volume


@Composable
fun BookItem(
    volume: Volume,
    isLiked: Boolean,
    isRead: Boolean,
    isPending: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit,
    onLikeClick: () -> Unit,
    onReadClick: () -> Unit,
    onPendingClick: () -> Unit
) {
    val imageUrl = volume.volumeInfo.imageLinks?.thumbnail

    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(160.dp)
            .clickable { onClick() }
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = volume.volumeInfo.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Text(
            text = volume.volumeInfo.title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )

        volume.volumeInfo.authors?.joinToString()?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = onLikeClick, enabled = !isLoading) {
                if (isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Outlined.Favorite,
                        contentDescription = null,
                        tint = if (isLiked) Color.Red else Color.Gray
                    )
                }
            }

            IconButton(onClick = onReadClick) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Marcar como leído",
                    tint = if (isRead) Color.Green else Color.Gray
                )
            }

            IconButton(onClick = onPendingClick) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Marcar como pendiente",
                    tint = if (isPending) Color(0xFFFF9800) else Color.Gray // Naranja o gris
                )
            }
        }
    }
}