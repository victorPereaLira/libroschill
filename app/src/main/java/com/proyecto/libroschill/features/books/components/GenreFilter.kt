package com.proyecto.libroschill.features.books.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*

@Composable
fun GenreFilter(
    genres: List<String>,
    selectedGenre: String?,
    onGenreSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(selectedGenre ?: "Filtrar por género")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Todos") },
                onClick = {
                    onGenreSelected(null)
                    expanded = false
                }
            )
            genres.forEach { genre ->
                DropdownMenuItem(
                    text = { Text(genre) },
                    onClick = {
                        onGenreSelected(genre)
                        expanded = false
                    }
                )
            }
        }
    }
}