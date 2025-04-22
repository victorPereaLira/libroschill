package com.proyecto.libroschill

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.libroschill.presentation.home.HomeScreen
import com.proyecto.libroschill.presentation.initial.InitialScreen
import com.proyecto.libroschill.presentation.login.LoginScreen
import com.proyecto.libroschill.presentation.signup.SignupScreen

@Composable
fun NavigationWrapper(navHostController: NavHostController, auth: FirebaseAuth) {
    NavHost(navController = navHostController, startDestination = "initial") {
        composable("initial") {
            InitialScreen(
                navigateToLogin = {navHostController.navigate("login")},
                navigateToRegister = {navHostController.navigate("signup")}

            )
        }
        composable("login") {
            LoginScreen(auth){
                navHostController.navigate("home")
            }
        }
        composable("signup") {
            SignupScreen(auth) {
                navHostController.navigate("initial")
            }
        }
        composable("home") {
            HomeScreen()
        }

    }
}