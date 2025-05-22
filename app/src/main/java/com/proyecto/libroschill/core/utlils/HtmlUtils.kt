package com.proyecto.libroschill.core.utlils

import android.text.Spanned
import androidx.core.text.HtmlCompat

fun parseHtml(htmlText: String): Spanned {
    return HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
}