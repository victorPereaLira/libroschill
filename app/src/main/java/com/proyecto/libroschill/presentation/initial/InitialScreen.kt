package com.proyecto.libroschill.presentation.initial


import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.libroschill.R
import com.proyecto.libroschill.ui.theme.BackgroundButton
import com.proyecto.libroschill.ui.theme.DarkBlueGradient
import com.proyecto.libroschill.ui.theme.MediumBlue

@Preview
@Composable
fun InitialScreen(navigateToLogin: () -> Unit = {}, navigateToRegister: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(listOf(DarkBlueGradient, MediumBlue))
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(20f))
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .clip(
                    CircleShape
                )
                .size(300.dp)
        )
        Spacer(modifier = Modifier.weight(8f))
        Text(
            "Libros & Chill",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navigateToLogin() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {

            Text("Iniciar Sesión", color = Color.White)
        }
        Spacer(modifier = Modifier.weight(8f))
        CustomButton(
            Modifier.clickable { },
            painterResource(id = R.drawable.google),
            "Iniciar Sesión con Google"
        )
        Spacer(modifier = Modifier.weight(8f))
        CustomButton(
            Modifier.clickable { },
            painterResource(id = R.drawable.facebook),
            "Iniciar Sesión con Facebook"
        )
        Spacer(modifier = Modifier.weight(8f))
        Button(
            onClick = { navigateToRegister() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {

            Text("Registrarse", color = Color.White)
        }
        Spacer(modifier = Modifier.weight(100f))


    }
}

@Composable
fun CustomButton(modifier: Modifier, painter: Painter, title: String) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 32.dp)
            .background(BackgroundButton)
            .border(color = Color.White, width = 1.dp, shape = CircleShape),
        contentAlignment = Alignment.CenterStart


    ) {
        Image(
            painter = painter,
            contentDescription = "google",
            modifier = Modifier
                .padding(start = 10.dp)
                .size(16.dp)

        )
        Text(
            text = title,
            color = Color.White,
            modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center
        )
    }
}