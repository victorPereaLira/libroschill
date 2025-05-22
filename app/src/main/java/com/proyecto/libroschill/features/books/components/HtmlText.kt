package com.proyecto.libroschill.features.books.components

import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.proyecto.libroschill.core.utlils.parseHtml

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
    val spanned = parseHtml(html)
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                text = spanned
                textSize = 16f
            }
        },
        modifier = modifier
    )
}