package com.proyecto.libroschill.navigation

import BooksDetailScreen
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.proyecto.libroschill.data.repository.BooksRepository
import com.proyecto.libroschill.features.home.HomeScreen
import com.proyecto.libroschill.features.initial.InitialScreen
import com.proyecto.libroschill.features.library.LikeScreen
import com.proyecto.libroschill.features.auth.login.LoginScreen
import com.proyecto.libroschill.data.model.Volume
import com.proyecto.libroschill.features.auth.signup.SignupScreen
import com.proyecto.libroschill.features.books.viewmodel.BooksViewModel
import com.proyecto.libroschill.features.books.viewmodel.BooksViewModelFactory
import com.proyecto.libroschill.features.library.PendingScreen
import com.proyecto.libroschill.features.library.ReadScreen
import com.proyecto.libroschill.features.auth.viewmodel.AuthViewModel
import com.proyecto.libroschill.features.books.viewmodel.ReadStatusViewModel
import com.proyecto.libroschill.features.books.viewmodel.RecommendationsViewModel
import com.proyecto.libroschill.features.books.viewmodel.RecommendationsViewModelFactory
import com.proyecto.libroschill.features.users.UserScreen

@Composable
fun NavigationWrapper(navHostController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val booksRepository = remember { BooksRepository() }

    val searchViewModel: BooksViewModel =
        viewModel(factory = BooksViewModelFactory(booksRepository))

    val recsVm: RecommendationsViewModel =
        viewModel(factory = RecommendationsViewModelFactory(booksRepository))

    val readStatusViewModel: ReadStatusViewModel = viewModel()

    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
    val startDestination = if (isLoggedIn) "home" else "initial"

    NavHost(navController = navHostController, startDestination = startDestination) {
        composable("initial") {
            InitialScreen(
                navigateToLogin = { navHostController.navigate("login") },
                navigateToRegister = { navHostController.navigate("signup") }
            )
        }
        composable("login") {
            LoginScreen(authViewModel = authViewModel) {
                navHostController.navigate("home") {
                    popUpTo("initial") { inclusive = true }
                }
            }
        }
        composable("signup") {
            SignupScreen(authViewModel = authViewModel) {
                navHostController.navigate("home") {
                    popUpTo("initial") { inclusive = true }
                }
            }
        }
        composable("home") {
            HomeScreen(
                searchViewModel = searchViewModel,
                recommendationsViewModel = recsVm,
                navHostController = navHostController,
                auth = FirebaseAuth.getInstance()
            )
        }
        composable("like") {
            LikeScreen(searchViewModel = searchViewModel, navHostController = navHostController)
        }
        composable("read") {
            ReadScreen(
                searchViewModel = searchViewModel,
                readStatusViewModel = readStatusViewModel,
                navHostController = navHostController
            )
        }
        composable("pending") {
            PendingScreen(
                searchViewModel = searchViewModel,
                readStatusViewModel = readStatusViewModel,
                navHostController = navHostController
            )
        }
        composable("detail/{volume}") { backStackEntry ->
            val volumeJson = backStackEntry.arguments?.getString("volume")
            if (volumeJson != null) {
                val decodedJson = Uri.decode(volumeJson)
                val volume = Gson().fromJson(decodedJson, Volume::class.java)
                BooksDetailScreen(volume = volume, onBack = { navHostController.popBackStack() })
            } else {
                LaunchedEffect(Unit) { navHostController.popBackStack() }
            }
        }
        composable("user_perfile"){
            UserScreen(
                navHostController = navHostController,
                auth = FirebaseAuth.getInstance(),
                searchViewModel = searchViewModel

            )
        }
    }
}

