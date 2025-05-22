package com.proyecto.libroschill.features.library

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.proyecto.libroschill.BuildConfig
import com.proyecto.libroschill.R
import com.proyecto.libroschill.navigation.ScreenWithDrawer
import com.proyecto.libroschill.features.books.components.GenreFilter
import com.proyecto.libroschill.features.books.components.BookItem
import com.proyecto.libroschill.features.books.viewmodel.BooksViewModel
import com.proyecto.libroschill.features.books.viewmodel.ReadStatusViewModel


@Composable
fun LikeScreen(
    searchViewModel: BooksViewModel,
    readStatusViewModel: ReadStatusViewModel = viewModel(),
    navHostController: NavHostController
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    val apiKey = BuildConfig.GOOGLE_BOOKS_API_KEY

    val likedBooks = searchViewModel.books
    val readIds = readStatusViewModel.readBookIds.collectAsState().value
    val pendingIds = readStatusViewModel.pendingBookIds.collectAsState().value
    val selectedGenre = remember { mutableStateOf<String?>(null) }

    val allGenres = remember(likedBooks) {
        likedBooks.flatMap { it.volumeInfo.categories ?: emptyList() }.distinct()
    }

    val filteredBooks = if (selectedGenre.value != null) {
        likedBooks.filter { it.volumeInfo.categories?.contains(selectedGenre.value) == true }
    } else {
        likedBooks
    }

    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            searchViewModel.getLikedBooks(userId, apiKey)
            searchViewModel.loadLikedBooks(userId)
            readStatusViewModel.loadReadAndPendingBooks(userId)
        }
    }

    ScreenWithDrawer(
        navController = navHostController,
        onLogout = {
            FirebaseAuth.getInstance().signOut()
            navHostController.navigate("initial") {
                popUpTo("home") { inclusive = true }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            GenreFilter(
                genres = allGenres,
                selectedGenre = selectedGenre.value,
                onGenreSelected = { selectedGenre.value = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredBooks.isEmpty()) {
                Text(
                    text = stringResource(R.string.noBooksFav),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(150.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredBooks) { volume ->
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
                                searchViewModel.toggleLike(userId, volume.id)
                            },
                            onReadClick = {
                                readStatusViewModel.toggleReadStatus(userId, volume.id, "read")
                            },
                            onPendingClick = {
                                readStatusViewModel.toggleReadStatus(userId, volume.id, "pending")
                            }
                        )
                    }
                }
            }
        }
    }
}