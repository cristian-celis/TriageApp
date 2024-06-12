package com.example.triagecol.presentation.supervisor.addPatient

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.triagecol.presentation.supervisor.addPatient.utils.DateUtils
import java.time.LocalDate
import java.time.Period

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateOfBirthInput(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val dateState = rememberDatePickerState(
        initialDisplayedMonthMillis = System.currentTimeMillis(),
        yearRange = (LocalDate.now().year - 130)..LocalDate.now().year)
    val millisToLocalDate = dateState.selectedDateMillis?.let {
        DateUtils().convertMillisToLocalDate(it)
    }

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(yearsBetween(millisToLocalDate)) },
                enabled = true
            ) {
                Text(text = "Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Cancelar")
            }
        }
    ) {
        DatePicker(state = dateState,
            title = {
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = "Seleccione la fecha de nacimiento",
                    style = TextStyle(fontSize = 18.sp)
                )
            })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun yearsBetween(date: LocalDate? = LocalDate.now()): String {
    val bornDate = date ?: LocalDate.now()
    val currentDate = LocalDate.now()
    val periodBetween = Period.between(bornDate, currentDate)
    return periodBetween.years.toString()
}