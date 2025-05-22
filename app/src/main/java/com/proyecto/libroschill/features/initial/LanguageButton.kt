package com.proyecto.libroschill.features.initial

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.proyecto.libroschill.core.utlils.LanguageManager

@Composable
fun LanguageButton(
    languageCode: String,
    flagResId: Int,
    context: Context,
    activity: Activity?
) {
    Image(
        painter = painterResource(id = flagResId),
        contentDescription = "Idioma $languageCode",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(48.dp) // Tamaño uniforme
            .clip(CircleShape) // Redondeado
            .border(2.dp, Color.White, CircleShape) // Borde blanco
            .clickable {
                LanguageManager.setLocale(context, languageCode)
                activity?.recreate()
            }
    )
}