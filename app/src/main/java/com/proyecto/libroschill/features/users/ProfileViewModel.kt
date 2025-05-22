package com.proyecto.libroschill.features.users

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    val imageUriState = mutableStateOf<String?>(null)

    fun loadProfileImage() {
        val shared = getApplication<Application>()
            .getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
        imageUriState.value = shared.getString("profile_image_uri", null)
    }

    fun saveProfileImage(path: String) {
        imageUriState.value = path
        val shared = getApplication<Application>()
            .getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
        shared.edit().putString("profile_image_uri", path).apply()
    }
}