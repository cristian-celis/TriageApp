package com.example.triagecol.presentation.supervisor.addSymptoms

import android.app.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.triagecol.utils.TextConstants

@Composable
fun AlertDialog(onCancel: () -> Unit, onAccept: () -> Unit) {
    androidx.compose.material3.AlertDialog(onDismissRequest = { onCancel() },
        dismissButton = { Button(onClick = { onCancel() }) { Text(text = "No") } },
        confirmButton = {
            Button(onClick = {
                onAccept()
            }) {
                Text(
                    text = TextConstants.YES_TEXT
                )
            }
        },
        title = {
            Text(
                text = "Volver Al Menu Principal"
            )
        },
        text = { Text(text = "¿Seguro que desea salir sin guardar al paciente?") })
}