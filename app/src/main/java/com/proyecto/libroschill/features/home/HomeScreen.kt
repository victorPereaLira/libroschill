package com.proyecto.libroschill.features.home



import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.proyecto.libroschill.features.books.viewmodel.BooksViewModel
import kotlinx.coroutines.launch
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.libroschill.features.books.viewmodel.RecommendationsViewModel
import com.proyecto.libroschill.features.initial.componentes.HomeContent
import com.proyecto.libroschill.features.initial.componentes.HomeDrawer
import com.proyecto.libroschill.features.initial.componentes.RegularAppBar
import com.proyecto.libroschill.features.initial.componentes.SearchAppBar
import com.proyecto.libroschill.features.home.logic.checkUserExists
import androidx.compose.foundation.layout.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.libroschill.core.utlils.LanguageManager
import com.proyecto.libroschill.features.initial.componentes.RecommendedSection
import com.proyecto.libroschill.features.books.viewmodel.ReadStatusViewModel

@Composable
fun HomeScreen(
    searchViewModel: BooksViewModel,
    recommendationsViewModel: RecommendationsViewModel,
    navHostController: NavHostController,
    auth: FirebaseAuth
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    val currentUserId = auth.currentUser?.uid.orEmpty()
    val recs by recommendationsViewModel.recs.collectAsState()
    val context = LocalContext.current
    val selectedLanguage = LanguageManager.getSavedLanguage(context)



    // Validar usuario y cargar datos
    LaunchedEffect(currentUserId) {
        if (currentUserId.isNotEmpty()) {
            searchViewModel.clearBooks()
            searchViewModel.loadLikedBooks(currentUserId)
            recommendationsViewModel.loadRecommendations(
                currentUserId,
                "AIzaSyBo5O1rT1WXjkAQNCF7IUcmBzUUtHEh-yg",
                selectedLanguage
            )
            checkUserExists(auth, navHostController)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !isSearchActive,
        drawerContent = {
            HomeDrawer(
                drawerState = drawerState,
                navController = navHostController,
                auth = auth,
                scope = scope
            )
        }
    ) {
        Scaffold(
            topBar = {
                if (isSearchActive) {
                    SearchAppBar(
                        searchText = searchText,
                        onSearchTextChange = { searchText = it },
                        onCloseClick = {
                            isSearchActive = false
                            searchText = ""
                            searchViewModel.clearBooks()
                        },
                        onSearchClick = {
                            searchViewModel.searchBooks(
                                searchText,
                                "AIzaSyBo5O1rT1WXjkAQNCF7IUcmBzUUtHEh-yg"
                            )
                        }
                    )
                } else {
                    RegularAppBar(
                        onMenuClick = { scope.launch { drawerState.open() } },
                        onSearchClick = {
                            isSearchActive = true
                            searchText = ""
                            searchViewModel.clearBooks()
                        }
                    )
                }
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                val showRecommendations = searchText.isBlank() || searchViewModel.books.isEmpty()

                if (showRecommendations) {
                    RecommendedSection(
                        recs = recs,
                        currentUserId = currentUserId,
                        navHostController = navHostController,
                        searchViewModel = searchViewModel
                    )
                } else {
                    HomeContent(
                        modifier = Modifier.weight(1f),
                        searchViewModel = searchViewModel,
                        navHostController = navHostController,
                        currentUserId = currentUserId,

                    )
                }
            }
        }
    }
}