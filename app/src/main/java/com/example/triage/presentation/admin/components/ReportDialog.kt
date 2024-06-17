package com.example.triage.presentation.admin.components

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.triage.presentation.admin.AdminViewModel
import com.example.triage.presentation.common.DateOfBirthInput
import com.example.triage.presentation.common.LabelTextField
import com.example.triage.presentation.supervisor.addPatient.components.AgeComponent
import com.example.triage.utils.Errors
import java.time.Instant
import java.time.ZoneId

@Composable
fun ReportDialog(modifier: Modifier = Modifier, adminViewModel: AdminViewModel) {
    val reportResult by adminViewModel.reportResult.collectAsState()
    val fetchingReport by adminViewModel.fetchingReport.collectAsState()
    val reportDate by adminViewModel.reportDate.collectAsState()
    val error by adminViewModel.error.collectAsState()
    val showCalendar by adminViewModel.showCalendar.collectAsState()

    if(showCalendar){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateOfBirthInput(title = "Seleccione la fecha del reporte",onConfirm = {
                val localDate = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
                adminViewModel.setReportDate(localDate.year, localDate.monthValue, localDate.dayOfMonth)
                adminViewModel.setShowCalendar(false)
            }) {
                adminViewModel.setShowCalendar(false)
            }
        }
    }

    Dialog(
        onDismissRequest = {
            adminViewModel.setReportDialog(false)
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(15.dp)),
            colors = CardColors(
                containerColor = Color.White,
                contentColor = Color.Black,
                disabledContentColor = Color.Black,
                disabledContainerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = "Reporte medico",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 26.sp),
                    textAlign = TextAlign.Center
                )
                LabelTextField(modifier = Modifier.padding(start = 5.dp, top = 20.dp), nameLabel = "Selecciona la fecha del informe")
                AgeComponent(modifier = Modifier
                    .fillMaxWidth(), value = reportDate.toString(), helpText = "Seleccione la fecha") {
                    adminViewModel.setShowCalendar(true)
                }
                Text(text = error, style = MaterialTheme.typography.labelMedium, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, bottom = 20.dp),
                    color = Color.Red)

                Column(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row {
                     CardItem(modifier = Modifier.fillMaxWidth(0.5f), label = "Fecha informe", data = reportDate.toString(), false)
                     CardItem(modifier = Modifier.fillMaxWidth(1f), label = "Pacientes atendidos", data = "${reportResult.patientsAttended}", fetchingReport)
                    }
                    Row {
                        CardItem(modifier = Modifier.fillMaxWidth(0.5f), label = "Tiempo promedio de espera", data = reportResult.averageTime, fetchingReport)
                        CardItem(modifier = Modifier.fillMaxWidth(1f), label = "Pacientes urgentes", data = "${reportResult.urgentPatient}", fetchingReport)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { adminViewModel.setReportDialog(false) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF3939),
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Cerrar",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun CardItem(modifier: Modifier = Modifier, label: String, data: String, loading: Boolean) {
    Surface(color = Color(0xFFF0F2F5),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .padding(5.dp)
            .height(164.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(text = label, style = TextStyle(fontSize = 16.sp))
            if(!loading){
                Text(text = data, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold))
            }else{
                ProgressIndicator()
            }
        }
    }
}

@Composable
private fun ProgressIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(27.dp),
        color = Color.LightGray,
        strokeWidth = 5.dp
    )
}