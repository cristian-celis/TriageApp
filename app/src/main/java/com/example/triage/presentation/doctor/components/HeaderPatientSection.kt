package com.example.triage.presentation.doctor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triage.presentation.doctor.DoctorViewModel
import com.example.triage.presentation.doctor.RoundedBoxTextMessage
import com.example.triage.utils.StringResources

@Composable
fun HeaderPatientSection(modifier: Modifier = Modifier, doctorViewModel: DoctorViewModel) {

    val patientData by doctorViewModel.patientData.collectAsState()
    val doctorInConsultation by doctorViewModel.doctorInConsultation.collectAsState()

    val patient = patientData.priorityPatient

    Text(
        text = "Datos del Paciente",
        style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold),
        textAlign = TextAlign.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (doctorInConsultation) {
            Column(modifier = Modifier.padding(start = 10.dp)) {
                DataLabels(label = StringResources.NAME, data = "${patient.name.uppercase()} ${patient.lastname.uppercase()}")
                DataLabels(label = StringResources.ID_NUMBER, data = patient.idNumber)
                DataLabels(label = StringResources.AGE, data = patient.age)
                if(patient.sex == "Femenino"){
                    DataLabels(label = StringResources.PREGNANCY, data = if(patient.pregnancy == 1) StringResources.YES_TEXT else StringResources.NOT_TEXT)
                }
            }


                Column (modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
                    Surface(color = Color(
                        when(patient.triage){
                            1 -> 0xFFB51B13
                            2 -> 0xFFCB3721
                            3 -> 0xFFC24616
                            4 -> 0xFFE7740F
                            5 -> 0xFFE49610
                            else -> 0xFFF0F2F5
                        }),
                        shape = RoundedCornerShape(18.dp)
                    ){
                        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Triage", style = TextStyle(fontSize = 16.sp, color = Color.White))
                            Text(text = patient.triage.toString(), style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = Color.White))
                        }
                    }
                }

        } else {
            RoundedBoxTextMessage(message = StringResources.NO_PATIENT_MESSAGE, topPadding = 5.dp, bottomPadding = 5.dp)
        }
    }
}

@Composable
fun DataLabels(modifier: Modifier = Modifier, label: String, data: String) {
    Row (modifier){
        Text(text = "$label: ",
            style = TextStyle(fontSize = 16.sp))
        Text(text = data,
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold))
    }
}