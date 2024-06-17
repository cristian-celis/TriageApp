package com.example.triage.presentation.supervisor.addSymptoms.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.triage.utils.StringResources

@Composable
fun AlertDialog(onCancel: () -> Unit, onAccept: () -> Unit) {
    androidx.compose.material3.AlertDialog(onDismissRequest = { onCancel() },
        dismissButton = { Button(onClick = { onCancel() }) { Text(text = "No") } },
        confirmButton = {
            Button(onClick = {
                onAccept()
            }) {
                Text(
                    text = StringResources.YES_TEXT
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