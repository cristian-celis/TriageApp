package com.example.triage.presentation.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.triage.utils.StringResources

@Composable
fun ConfirmScreenDialog(mainText: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(onDismissRequest = { onDismiss() },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = StringResources.NOT_TEXT
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm()
            }) {
                Text(
                    text = StringResources.YES_TEXT
                )
            }
        },
        title = {
            Text(
                text = StringResources.SIGN_OFF
            )
        },
        text = { Text(text = mainText) })
}