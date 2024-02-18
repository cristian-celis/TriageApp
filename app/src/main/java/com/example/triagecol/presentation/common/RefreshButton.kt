package com.example.triagecol.presentation.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.traigecol.R

@Composable
fun RefreshButton(onRefresh:() -> Unit) {
    var isRefreshing by remember {mutableStateOf(false)}

    val rotation by animateFloatAsState(
        targetValue = if(isRefreshing) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    IconButton(
        onClick = {
            isRefreshing = !isRefreshing
            onRefresh()
        },
        modifier = Modifier
            .rotate(rotation).size(70.dp)
    ) {
        Icon(
            painterResource(id = R.drawable.ic_refresh), // Usa el ícono de recarga
            contentDescription = "Refresh",
            modifier = Modifier.size(70.dp),
            tint = Color.LightGray
        )
    }
}