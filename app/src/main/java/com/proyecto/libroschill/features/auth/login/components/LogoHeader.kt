package com.proyecto.libroschill.features.auth.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.proyecto.libroschill.R

@Composable
fun LogoHeader() {
    Spacer(modifier = Modifier.height(40.dp))
    Image(
        painter = painterResource(id = R.drawable.logobueno),
        contentDescription = "Logo",
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
    )
    Spacer(modifier = Modifier.height(40.dp))
}