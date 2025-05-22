package com.proyecto.libroschill.features.initial.componentes

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import androidx.navigation.NavHostController
import com.proyecto.libroschill.features.books.components.BookItem
import com.proyecto.libroschill.features.books.viewmodel.BooksViewModel

import com.proyecto.libroschill.features.books.viewmodel.ReadStatusViewModel

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    searchViewModel: BooksViewModel,
    readStatusViewModel: ReadStatusViewModel = viewModel(),
    navHostController: NavHostController,
    currentUserId: String,

    ) {
    val booksToShow = searchViewModel.books
    val readIds by readStatusViewModel.readBookIds.collectAsState()
    val pendingIds by readStatusViewModel.pendingBookIds.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = booksToShow,
            key = { it.id }
        ) { volume ->
            val isLiked = searchViewModel.likedBookIds.contains(volume.id)
            val isLoading = searchViewModel.loadingLikes.contains(volume.id)
            val isRead = readIds.contains(volume.id)
            val isPending = pendingIds.contains(volume.id)
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
                    readStatusViewModel.toggleReadStatus(currentUserId, volume.id, "read")
                },
                onPendingClick = {
                    readStatusViewModel.toggleReadStatus(currentUserId, volume.id, "pending")
                }
            )
        }
    }
}