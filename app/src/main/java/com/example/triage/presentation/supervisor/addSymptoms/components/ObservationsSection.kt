package com.example.triage.presentation.supervisor.addSymptoms.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.traigecol.R
import com.example.triage.presentation.supervisor.addSymptoms.SymptomsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Textarea(symptomsViewModel: SymptomsViewModel) {
    val observations by symptomsViewModel.observations.collectAsState()
    TextField(
        value = observations,
        onValueChange = { symptomsViewModel.updateObservation(it) },
        placeholder = {
            Text(
                text = "Ingrese las observaciones medicas (No es obligatorio).",
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(id = R.color.placeholder)
            )
        },
        maxLines = 3,
        textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(10.dp)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(0xFFE8EDF2),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}