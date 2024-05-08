package com.example.triagecol.presentation.doctor

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.traigecol.R
import com.example.triagecol.utils.Constants
import com.example.triagecol.utils.DoctorConstants
import com.example.triagecol.utils.SupervisorConstants
import java.util.Locale

@Composable
fun PatientSectionScreen(doctorViewModel: DoctorViewModel, modifier: Modifier = Modifier) {
    val patientData by doctorViewModel.patientData.collectAsState()
    val doctorInConsultation by doctorViewModel.doctorInConsultation.collectAsState()

    val patient = patientData.priorityPatient
    val symptoms = patientData.patientSymptoms

    Box(modifier = modifier
        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
        //.border(border = BorderStroke(1.dp, color = Color(0xFFCFCFCF)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(8.dp)
        ) {

            Text(
                text = "Datos del Paciente",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                if (doctorInConsultation) {
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        Text(
                            text = "Nombre: ${patient.name.uppercase()} ${patient.lastname.uppercase()}",
                            style = TextStyle(fontSize = 16.sp)
                        )
                        Text(
                            text = "Identificacion: ${patient.idNumber}",
                            style = TextStyle(fontSize = 14.sp)
                        )
                        Text(
                            text = "Edad: ${patient.age} Años",
                            style = TextStyle(fontSize = 14.sp)
                        )
                    }
                } else {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                    ) {
                        Column(modifier = Modifier
                            .clip(shape = ShapeDefaults.Small)
                            .background(Color(0xFFECECEC))
                            .align(Alignment.Center)
                            .height(33.dp)
                            .width(250.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = DoctorConstants.NO_PATIENT_MESSAGE,
                                style = TextStyle(fontSize = 16.sp, color = Color(0xFF000000)),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFDADADA),
                thickness = 1.dp
            )

            Text(
                text = DoctorConstants.SYMPTOMS,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 17.dp, top = 5.dp)
            )
            if (symptoms.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
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

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFDADADA),
                thickness = 1.dp
            )

            Text(
                text = DoctorConstants.VITAL_SIGNS,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp, top = 5.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.33f)
                        .height(95.dp)
                ) {
                    VitalSignsCards(
                        iconResource = painterResource(id = R.drawable.temperature_icon),
                        vitalSignName = SupervisorConstants.TEMPERATURE,
                        vitalSignValue = patient.temperature
                    )
                }
                Spacer(modifier = Modifier.width(9.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(95.dp)
                ) {
                    VitalSignsCards(
                        iconResource = painterResource(id = R.drawable.blood_oxygen_icon),
                        vitalSignName = SupervisorConstants.BLOOD_OXYGEN,
                        vitalSignValue = patient.bloodOxygen
                    )
                }
                Spacer(modifier = Modifier.width(9.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(95.dp)
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
}

@Composable
fun SymptomsCard(symptomName: String) {
    Text(text = symptomName)
}

@Composable
fun VitalSignsCards(iconResource: Painter, vitalSignName: String, vitalSignValue: String) {
    Column(
        modifier = Modifier
            .border(
                border = BorderStroke(width = 1.dp, color = Color(0xFFBEBEBE)),
                shape = ShapeDefaults.Small
            )
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            text = vitalSignName,
            style = TextStyle(color = Color.Gray, fontSize = 14.sp),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(3.dp)
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