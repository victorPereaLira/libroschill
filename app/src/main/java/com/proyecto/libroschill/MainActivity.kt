package com.proyecto.libroschill

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.proyecto.libroschill.ui.theme.LibroschillTheme
import com.proyecto.libroschill.core.utlils.LanguageManager
import com.proyecto.libroschill.navigation.NavigationWrapper
import com.google.firebase.FirebaseApp
class MainActivity : ComponentActivity() {

    private lateinit var navHostController: NavHostController
    private lateinit var auth: FirebaseAuth

    override fun attachBaseContext(newBase: Context) {
        val context = LanguageManager.applyStoredLocale(newBase)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        enableEdgeToEdge()
        setContent {
            navHostController = rememberNavController()

            LibroschillTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationWrapper(navHostController)
                    LaunchedEffect(Unit) {
                        val currentUser = Firebase.auth.currentUser
                        if (currentUser != null) {
                            navHostController.navigate("home") {
                                popUpTo("initial") { inclusive = true }
                            }
                        }
                    }
                }
            }
        }
    }
}
