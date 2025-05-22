package com.proyecto.libroschill.features.books

import BooksDetailScreen
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.proyecto.libroschill.data.model.ImageLinks
import com.proyecto.libroschill.data.model.Volume
import com.proyecto.libroschill.data.model.VolumeInfo
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class BooksDetailScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun fakeVolume() = Volume(
        id = "1234",
        volumeInfo = VolumeInfo(
            title = "El libro de pruebas",
            authors = listOf("Autor de Prueba"),
            description = "Esta es una descripción muy larga que debería ser cortada al principio..." + " más texto".repeat(
                50
            ),
            imageLinks = ImageLinks(
                smallThumbnail = null,
                thumbnail = ""
            )
        )
    )

    @Test
    fun booksDetail_displaysTitleAndExpandButton() {
        composeTestRule.setContent {
            BooksDetailScreen(volume = fakeVolume(), onBack = {})
        }

        composeTestRule.onNodeWithText("El libro de pruebas").assertIsDisplayed()
        composeTestRule.onNodeWithText("Leer más").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ver en Google Books").assertIsDisplayed()
    }

    @Test
    fun booksDetail_expandAndCollapseDescription() {
        composeTestRule.setContent {
            BooksDetailScreen(volume = fakeVolume(), onBack = {})
        }

        composeTestRule.onNodeWithText("Leer más").performClick()
        composeTestRule.onNodeWithText("Leer menos").assertIsDisplayed()
    }
}