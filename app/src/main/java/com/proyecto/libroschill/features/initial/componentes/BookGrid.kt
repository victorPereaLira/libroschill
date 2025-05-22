package com.proyecto.libroschill.features.initial.componentes

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.proyecto.libroschill.features.books.components.BookItem
import com.proyecto.libroschill.features.books.viewmodel.BooksViewModel
import com.proyecto.libroschill.data.model.Volume

@Composable
fun BookGrid(
    books: List<Volume>,
    navHostController: NavHostController,
    searchViewModel: BooksViewModel,
    currentUserId: String
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(books, key = { it.id }) { volume ->
            val isLiked = searchViewModel.likedBookIds.contains(volume.id)
            val isLoading = searchViewModel.loadingLikes.contains(volume.id)
            val isRead = searchViewModel.readBookIds.contains(volume.id)
            val isPending = searchViewModel.pendingBookIds.contains(volume.id)

            BookItem(
                volume = volume,
                isLiked = isLiked,
                isLoading = isLoading,
                isRead = isRead,
                isPending = isPending,
                onClick = {
                    val volumeJson = Uri.encode(Gson().toJson(volume))
                    navHostController.navigate("detail/$volumeJson")
                },
                onLikeClick = {
                    searchViewModel.toggleLike(currentUserId, volume.id)
                },
                onReadClick = {
                    searchViewModel.toggleReadStatus(currentUserId, volume.id, "read")
                },
                onPendingClick = {
                    searchViewModel.toggleReadStatus(currentUserId, volume.id, "pending")
                }
            )
        }
    }
}