package com.example.triagecol.presentation.doctor

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.Dp
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

    Box(
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
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
                        Text(
                            text = "Estado de embarazo: ${if(patient.pregnancy == 1) "Si" else "No"}",
                            style = TextStyle(fontSize = 14.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                        )
                    }
                } else {
                    RoundedBoxTextMessage(message = DoctorConstants.NO_PATIENT_MESSAGE, topPadding = 5.dp, bottomPadding = 5.dp)
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
                    .padding(bottom = 10.dp, top = 8.dp)
            )

            if (symptoms.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    items(count = symptoms.size) {
                        val symptom = symptoms[it]
                        SymptomsCard(symptomName = symptom.symptomName)
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            } else {
                Column (modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    RoundedBoxTextMessage(message = "No hay sintomas registrados.", topPadding = 8.dp, bottomPadding = 14.dp)
                }

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFDADADA),
                    thickness = 1.dp
                )
            }

            Text(
                text = DoctorConstants.VITAL_SIGNS,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp, top = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                VitalSignsCards(
                    iconResource = painterResource(id = R.drawable.temperature_icon),
                    vitalSignName = SupervisorConstants.TEMPERATURE,
                    vitalSignValue = "${patient.temperature} °C",
                    modifier = Modifier.fillMaxWidth(0.33f)
                )
                Spacer(modifier = Modifier.width(9.dp))

                VitalSignsCards(
                    iconResource = painterResource(id = R.drawable.blood_oxygen_icon),
                    vitalSignName = SupervisorConstants.BLOOD_OXYGEN,
                    vitalSignValue = "${patient.bloodOxygen}%",
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
                Spacer(modifier = Modifier.width(9.dp))

                VitalSignsCards(
                    iconResource = painterResource(id = R.drawable.heart_rate_icon),
                    vitalSignName = SupervisorConstants.HEART_RATE,
                    vitalSignValue = "${patient.heartRate} bpm",
                    modifier = Modifier.fillMaxWidth(1f)
                )
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFDADADA),
                thickness = 1.dp
            )
            Observations(doctorViewModel = doctorViewModel)
        }
    }
}

@Composable
fun Observations(doctorViewModel: DoctorViewModel) {
    val observations by doctorViewModel.patientData.collectAsState()
    Text(
        text = "Observaciones",
        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp, top = 8.dp)
    )
    if(observations.priorityPatient.observations.isNotEmpty()){
        Text(text = observations.priorityPatient.observations)
    }else{
        RoundedBoxTextMessage(message = "No tiene observaciones", topPadding = 5.dp, bottomPadding = 5.dp)
    }
}

@Composable
fun SymptomsCard(symptomName: String) {
    Text(text = symptomName)
}

@Composable
private fun VitalSignsCards(
    iconResource: Painter,
    vitalSignName: String,
    vitalSignValue: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(100.dp)
    ) {
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
                    .height(35.dp)
                    .padding(top = 3.dp),
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
                ) {
                    Icon(
                        painter = iconResource,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
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
}

@Composable
fun RoundedBoxTextMessage(message: String, topPadding: Dp, bottomPadding: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topPadding, bottom = bottomPadding)
    ) {
        Column(
            modifier = Modifier
                .clip(shape = ShapeDefaults.Small)
                .background(Color(0xFFECECEC))
                .align(Alignment.Center)
                .height(33.dp)
                .width(250.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = message,
                style = TextStyle(fontSize = 16.sp, color = Color(0xFF000000)),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}