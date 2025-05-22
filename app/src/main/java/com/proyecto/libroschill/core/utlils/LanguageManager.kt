package com.proyecto.libroschill.core.utlils


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import java.util.*

object LanguageManager {

    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

    // Devuelve el idioma guardado (o el predeterminado del sistema)
    fun getSavedLanguage(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(SELECTED_LANGUAGE, Locale.getDefault().language) ?: "es"
    }

    // Guarda el idioma elegido
    fun persistLanguage(context: Context, language: String) {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putString(SELECTED_LANGUAGE, language).apply()
    }

    // Aplica el idioma seleccionado
    fun setLocale(context: Context, language: String): Context {
        persistLanguage(context, language)
        return updateResources(context, language)
    }

    // Aplica el idioma guardado (por ejemplo, al iniciar la app)
    fun applyStoredLocale(context: Context): Context {
        val language = getSavedLanguage(context)
        return setLocale(context, language)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = context.resources
        val config = resources.configuration

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            config.setLayoutDirection(locale)
            return context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
            @Suppress("DEPRECATION")
            resources.updateConfiguration(config, resources.displayMetrics)
            return context
        }
    }
}