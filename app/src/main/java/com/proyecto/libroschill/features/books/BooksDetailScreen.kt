import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.libroschill.features.comments.CommentsSection
import com.proyecto.libroschill.features.books.components.HtmlText
import com.proyecto.libroschill.data.model.Volume



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksDetailScreen(volume: Volume, onBack: () -> Unit) {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var isExpanded by remember { mutableStateOf(false) }
    val description = volume.volumeInfo.description ?: "No hay descripción disponible."
    val context = LocalContext.current

    // Construir URL directa a Google Books
    val googleBooksUrl = "https://www.google.es/books/edition/${volume.volumeInfo.title?.replace(" ", "_")}/${volume.id}?hl=es"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = volume.volumeInfo.title ?: "Detalle del libro",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = volume.volumeInfo.imageLinks?.thumbnail,
                    contentDescription = volume.volumeInfo.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(2f / 3f)
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            Text(
                text = volume.volumeInfo.title ?: "Sin título",
                style = MaterialTheme.typography.headlineSmall
            )


            volume.volumeInfo.authors?.takeIf { it.isNotEmpty() }?.let { authors ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Por ${authors.joinToString()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Descripción",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (isExpanded) {
                HtmlText(description)
            } else {
                HtmlText(description.take(300) + "...")
            }

            if (description.length > 300) {
                TextButton(onClick = { isExpanded = !isExpanded }) {
                    Text(if (isExpanded) "Leer menos" else "Leer más")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                CommentsSection(
                    bookId = volume.id,
                    currentUserId = currentUserId
                )
            }

            // 🛒 Botón de Google Books
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleBooksUrl))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver en Google Books")
            }
        }
    }
}