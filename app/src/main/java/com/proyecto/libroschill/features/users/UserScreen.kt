package com.proyecto.libroschill.features.users


import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.libroschill.R
import java.io.File
import java.io.FileOutputStream
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.libroschill.navigation.ScreenWithDrawer
import com.proyecto.libroschill.features.books.viewmodel.BooksViewModel


@Composable
fun UserScreen(
    navHostController: NavHostController,
    auth: FirebaseAuth,
    searchViewModel: BooksViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    val user = auth.currentUser
    val email = user?.email ?: "Sin email"
    val userId = user?.uid.orEmpty()
    val context = LocalContext.current
    val nicknameState = remember { mutableStateOf("Usuario") }


    val readCount = searchViewModel.readBookIds.size
    val pendingCount = searchViewModel.pendingBookIds.size
    val favCount = searchViewModel.likedBookIds.size

    val imageUri = profileViewModel.imageUriState.value


    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val file = File(context.filesDir, "profile_image.jpg")
            inputStream?.use { input ->
                FileOutputStream(file).use { output -> input.copyTo(output) }
            }
            profileViewModel.saveProfileImage(file.absolutePath)
        }
    }

    //Cargar nickname e imágenes
    LaunchedEffect(Unit) {
        profileViewModel.loadProfileImage()

        if (userId.isNotEmpty()) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { doc ->
                    nicknameState.value = doc.getString("nickname") ?: "Usuario"
                }

            // 📥 Aseguramos que las estadísticas estén cargadas
            val apiKey = "AIzaSyBo5O1rT1WXjkAQNCF7IUcmBzUUtHEh-yg"
            searchViewModel.getReadBooks(userId, apiKey)
            searchViewModel.getPendingBooks(userId, apiKey)
        }
    }

    val profilePainter = imageUri?.let { rememberAsyncImagePainter(File(it)) }
        ?: painterResource(id = R.drawable.iconoperfil)

    ScreenWithDrawer(
        navController = navHostController,
        onLogout = {
            auth.signOut()
            navHostController.navigate("initial") {
                popUpTo("home") { inclusive = true }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🖼 Imagen de perfil
            Image(
                painter = profilePainter,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(16.dp))


            Text(text = stringResource(R.string.nickname) + "${nicknameState.value}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))


            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(email, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text(text = stringResource( R.string.estadisticas), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            StatsSection(
                readCount = readCount,
                pendingCount = pendingCount,
                favoritesCount = favCount
            )

            Spacer(modifier = Modifier.height(32.dp))


            Button(onClick = {
                user?.email?.let {
                    auth.sendPasswordResetEmail(it)
                        .addOnSuccessListener {
                            Toast.makeText(context, R.string.CorreoEnviado, Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                        }
                }
            }) {
                Text(stringResource(R.string.RestablecerPassowrd))
            }
        }
    }
}