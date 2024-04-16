package com.example.triagecol.presentation.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.triagecol.presentation.admin.details.DetailMode
import com.example.triagecol.utils.TextConstants

@Composable
fun ConfirmScreenDialog(mainText: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(onDismissRequest = { onDismiss() },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = TextConstants.NOT_TEXT
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm()
            }) {
                Text(
                    text = TextConstants.YES_TEXT
                )
            }
        },
        title = {
            Text(
                text = TextConstants.SIGN_OFF
            )
        },
        text = { Text(text = mainText) })
}