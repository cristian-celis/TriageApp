package com.example.triage.presentation.doctor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triage.presentation.doctor.components.HeaderPatientSection
import com.example.triage.presentation.doctor.components.SymptomsAccordion
import com.example.triage.presentation.doctor.components.VitalSignsSection
import com.example.triage.utils.StringResources

@Composable
fun PatientSectionScreen(doctorViewModel: DoctorViewModel, modifier: Modifier = Modifier) {
    val patientData by doctorViewModel.patientData.collectAsState()

    val patient = patientData.priorityPatient
    val symptoms = patientData.patientSymptoms

    Surface(
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        shape = RoundedCornerShape(4.dp),
        shadowElevation = 4.dp,
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {

            HeaderPatientSection(
                modifier = Modifier.padding(horizontal = 14.dp),
                doctorViewModel = doctorViewModel
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFDADADA),
                thickness = 1.dp
            )

            SymptomsAccordion(symptoms = symptoms)

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFDADADA),
                thickness = 1.dp
            )

            Text(
                text = StringResources.VITAL_SIGNS,
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            )

            VitalSignsSection(
                modifier = Modifier.padding(horizontal = 14.dp),
                temperature = patient.temperature,
                bloodOxygen = patient.bloodOxygen,
                heartRate = patient.heartRate
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                color = Color(0xFFDADADA),
                thickness = 1.dp
            )
            Observations(
                modifier = Modifier.padding(horizontal = 10.dp),
                doctorViewModel = doctorViewModel
            )
        }
    }
}

@Composable
fun Observations(modifier: Modifier = Modifier, doctorViewModel: DoctorViewModel) {
    val observations by doctorViewModel.patientData.collectAsState()
    Text(
        text = "Observaciones",
        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
        textAlign = TextAlign.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp, top = 8.dp)
    )
    if (observations.priorityPatient.observations.isNotEmpty()) {
        Text(text = observations.priorityPatient.observations)
    } else {
        RoundedBoxTextMessage(
            message = "No tiene observaciones",
            topPadding = 5.dp,
            bottomPadding = 5.dp
        )
    }
}

@Composable
fun SymptomsCard(symptomName: String) {
    Text(text = symptomName)
}

@Composable
fun RoundedBoxTextMessage(
    modifier: Modifier = Modifier,
    message: String,
    topPadding: Dp,
    bottomPadding: Dp
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .padding(top = topPadding, bottom = bottomPadding),
            //border = BorderStroke(width = 0.7.dp, color = Color.LightGray),
            shape = MaterialTheme.shapes.small,
            color = Color(0xFFF3F1F1)
        ) {
            Text(
                text = message,
                style = TextStyle(fontSize = 16.sp, color = Color(0xFF6C6C6C)),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(7.dp)
            )
        }
    }
}