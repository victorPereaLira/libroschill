package com.proyecto.libroschill.features.initial.componentes

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.libroschill.R
import com.proyecto.libroschill.features.users.ProfileViewModel
import java.io.File
import java.io.FileOutputStream


@Composable
fun DrawerContent(
    onItemSelected: (String) -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid.orEmpty()
    val email = user?.email.orEmpty()
    var nickname by remember { mutableStateOf("Usuario") }
    val profileViewModel: ProfileViewModel = viewModel()
    val imageUri = profileViewModel.imageUriState.value



    // Cargar imagen persistente
    val profilePainter = imageUri?.let { rememberAsyncImagePainter(File(it)) }
        ?: painterResource(id = R.drawable.iconoperfil)


    // Launcher para seleccionar imagen
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val file = File(context.filesDir, "profile_image.jpg")
            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }

            context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
                .edit()
                .putString("profile_image_uri", file.absolutePath)
                .apply()
        }
    }

    // Obtener nickname desde Firestore
    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { doc ->
                    nickname = doc.getString("nickname") ?: "Usuario"
                }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = profilePainter,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = nickname,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Divider()
        NavigationDrawerItem(
            label = { Text("Perfil") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            selected = false,
            onClick = { onItemSelected("user_perfile") }
        )

        NavigationDrawerItem(
            label = { Text(stringResource(R.string.inicio)) },
            selected = false,
            onClick = { onItemSelected("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) }
        )

        NavigationDrawerItem(
            label = { Text(stringResource(R.string.favoritos)) },
            selected = false,
            onClick = { onItemSelected("like") },
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) }
        )

        NavigationDrawerItem(
            label = { Text(stringResource(R.string.leidos)) },
            selected = false,
            onClick = { onItemSelected("read") },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) }
        )

        NavigationDrawerItem(
            label = { Text(stringResource(R.string.pendientes)) },
            selected = false,
            onClick = { onItemSelected("pending") },
            icon = { Icon(Icons.Default.Schedule, contentDescription = null) }
        )

        Spacer(modifier = Modifier.weight(1f))

        NavigationDrawerItem(
            label = { Text(stringResource(R.string.cerrarSesion)) },
            selected = false,
            onClick = onLogout,
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) }
        )
    }
}