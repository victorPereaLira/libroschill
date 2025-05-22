package com.proyecto.libroschill.features.initial.componentes

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.proyecto.libroschill.features.books.components.BookItem
import com.proyecto.libroschill.features.books.viewmodel.BooksViewModel
import com.proyecto.libroschill.data.model.Volume
import com.proyecto.libroschill.features.books.viewmodel.ReadStatusViewModel

@Composable
fun RecommendedSection(
    recs: List<Volume>,
    currentUserId: String,
    navHostController: NavHostController,
    searchViewModel: BooksViewModel,
    readStatusViewModel: ReadStatusViewModel = viewModel(),
) {
    val readIds by readStatusViewModel.readBookIds.collectAsState()
    val pendingIds by readStatusViewModel.pendingBookIds.collectAsState()
    if (recs.isEmpty()) return // no mostrar si no hay recomendaciones

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Recomendados para ti",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(recs) { volume ->
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
}