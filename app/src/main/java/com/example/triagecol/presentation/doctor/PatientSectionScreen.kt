package com.example.triagecol.presentation.doctor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.traigecol.R
import com.example.triagecol.utils.DoctorConstants
import com.example.triagecol.utils.SupervisorConstants

@Composable
fun PatientSectionScreen(doctorViewModel: DoctorViewModel, modifier: Modifier = Modifier) {
    val patientData by doctorViewModel.patientData.collectAsState()
    val doctorInConsultation by doctorViewModel.doctorInConsultation.collectAsState()

    val patient = patientData.priorityPatient
    val symptoms = patientData.patientSymptoms

    Column(modifier = modifier) {
        Box {
            Text(
                text = DoctorConstants.PATIENT_INFORMATION,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.patient_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFFDAD9D9), shape = ShapeDefaults.Small)
                    .padding(5.dp)
            )
            if (doctorInConsultation) {
                Text(
                    text = "Paciente: ${patient.name} ${patient.lastname}",
                    style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
                    modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.CenterVertically)
                )
            } else {
                Text(
                    text = DoctorConstants.NO_PATIENT_MESSAGE,
                    style = TextStyle(fontSize = 16.sp, color = Color(0xFF7C7C7C)),
                    modifier = Modifier
                        .fillMaxWidth().padding(12.dp)
                        .height(38.dp),
                    textAlign = TextAlign.Center
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Numero de identificacion: ${patient.idNumber}")
                Text(text = "Edad: ${patient.age}")
            }
        }

        Text(
            text = DoctorConstants.SYMPTOMS,
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold),
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp)
        )
        if (symptoms.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(count = symptoms.size) {
                    val symptom = symptoms[it]
                    SymptomsCard(symptomName = symptom.symptomName)
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        } else {
            Spacer(modifier = Modifier.height(30.dp))
        }
        Text(
            text = DoctorConstants.VITAL_SIGNS,
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold),
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 17.dp)
        )
        Column(
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(60.dp)
                ) {
                    VitalSignsCards(
                        iconResource = painterResource(id = R.drawable.temperature_icon),
                        vitalSignName = SupervisorConstants.TEMPERATURE,
                        vitalSignValue = patient.temperature
                    )
                }
                Spacer(
                    modifier = Modifier
                        .width(9.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(60.dp)
                ) {
                    VitalSignsCards(
                        iconResource = painterResource(id = R.drawable.blood_oxygen_icon),
                        vitalSignName = SupervisorConstants.BLOOD_OXYGEN,
                        vitalSignValue = patient.bloodOxygen
                    )
                }
            }
            Spacer(modifier = Modifier.height(9.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(60.dp)
            ) {
                VitalSignsCards(
                    iconResource = painterResource(id = R.drawable.heart_rate_icon),
                    vitalSignName = SupervisorConstants.HEART_RATE,
                    vitalSignValue = patient.heartRate
                )
            }
        }
    }
}

@Composable
fun SymptomsCard(symptomName: String) {
    Text(text = symptomName)
}

@Composable
fun VitalSignsCards(iconResource: Painter, vitalSignName: String, vitalSignValue: String) {
    Row(
        modifier = Modifier
            .border(
                border = BorderStroke(width = 1.dp, color = Color(0xFFBEBEBE)),
                shape = ShapeDefaults.Small
            )
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(5.dp)
        ) {
            Icon(
                painter = iconResource,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFEEEEEE), shape = ShapeDefaults.Medium)
                    .align(Alignment.CenterStart)
            )
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = vitalSignName,
                style = TextStyle(color = Color.Gray, fontSize = 14.sp),
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = vitalSignValue,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}