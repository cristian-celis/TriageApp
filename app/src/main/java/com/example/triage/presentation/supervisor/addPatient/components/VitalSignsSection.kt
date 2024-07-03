package com.example.triage.presentation.supervisor.addPatient.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triage.presentation.common.LabelTextField
import com.example.triage.presentation.common.TextFieldComponent
import com.example.triage.presentation.supervisor.addPatient.PatientData
import com.example.triage.presentation.supervisor.addPatient.PatientViewModel
import com.example.triage.utils.StringResources

@Composable
fun VitalSigns(patientViewModel: PatientViewModel, modifier: Modifier = Modifier) {
    val patient by patientViewModel.patient.collectAsState()
    var temperatureError by remember { mutableStateOf(false) }
    var heartRateError by remember { mutableStateOf(false) }
    var bloodOxygenError by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(start = 20.dp, end = 20.dp)) {
        Text(
            text = "Signos vitales",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            textAlign = TextAlign.Start,
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Medium)
        )

        LabelTextField(nameLabel = StringResources.TEMPERATURE)
        TextFieldComponent(
            labelTitle = StringResources.TEMPERATURE,
            helpText = "Ingrese la temperatura",
            value = patient.temperature,
            keypad = true,
            onTextFieldChanged = {
                if(it.length <= 4){
                    temperatureError = if(it.length > 1) it.toFloat() !in 28.0..43.0 else false
                    patientViewModel.updatePatientData(it, PatientData.TEMPERATURE)
                }
            })
        SignErrorLabel(
            modifier = Modifier
                .fillMaxWidth(), temperatureError, 28, 43, "°C"
        )
        LabelTextField(nameLabel = StringResources.HEART_RATE)
        TextFieldComponent(
            labelTitle = StringResources.HEART_RATE,
            helpText = "Ingrese la frecuencia cardiaca",
            value = patient.heartRate,
            keypad = true,
            onTextFieldChanged = {
                if (it.length <= 3) {
                    heartRateError = if(it.length > 1) it.toInt() !in 40..200 else false
                    patientViewModel.updatePatientData(it, PatientData.HEART_RATE)
                }
            })
        SignErrorLabel(
            modifier = Modifier
                .fillMaxWidth(), heartRateError, 40, 200, "bpm"
        )
        LabelTextField(nameLabel = StringResources.BLOOD_OXYGEN)
        TextFieldComponent(
            labelTitle = StringResources.BLOOD_OXYGEN,
            helpText = "Ingrese la oxigenacion sanguinea",
            value = patient.bloodOxygen,
            keypad = true,
            onTextFieldChanged = {
                if (it.length <= 3) {
                    bloodOxygenError = if(it.length > 1) it.toInt() !in 80..100 else false
                    patientViewModel.updatePatientData(it, PatientData.BLOOD_OXYGEN)
                }
            })
        SignErrorLabel(
            modifier = Modifier
                .fillMaxWidth(), bloodOxygenError, 80, 100, "%"
        )
    }
}

@Composable
fun SignErrorLabel(modifier: Modifier = Modifier, showError: Boolean, min: Int, max: Int, unitMeasurement: String) {
    Box(modifier = modifier.padding(bottom = 14.dp)){
        Crossfade(targetState = !showError, label = "") { isVisible ->
            if (isVisible) {
                Text(
                    text = "Valor entre $min y $max",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.W300),
                    textAlign = TextAlign.End,
                )
            }
        }
        Crossfade(targetState = showError, label = "") { isVisible ->
            if (isVisible) {
                Text(
                    text = "Valor fuera de rango ($min - $max $unitMeasurement)",
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Red,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}