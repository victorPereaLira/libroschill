package com.proyecto.libroschill.features.initial.componentes

import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeDrawer(
    drawerState: DrawerState,
    navController: NavHostController,
    auth: FirebaseAuth,
    scope: CoroutineScope
) {
    ModalDrawerSheet(modifier = Modifier.width(280.dp)) {
        DrawerContent(
            onItemSelected = { route ->
                scope.launch { drawerState.close() }
                navController.navigate(route)
            },
            onLogout = {
                scope.launch {
                    drawerState.close()
                    auth.signOut()
                    navController.navigate("initial") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }
        )
    }
}