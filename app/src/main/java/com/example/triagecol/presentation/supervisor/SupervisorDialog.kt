package com.example.triagecol.presentation.supervisor

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.triagecol.utils.Constants
import com.example.triagecol.utils.SupervisorConstants
import com.example.triagecol.utils.TextConstants

@Composable
fun SupervisorDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    supervisorViewModel: SupervisorViewModel
) {
    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    )
    {
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp)
            ) {
                Text(
                    text = "Resumen datos ingresados.",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                    textAlign = TextAlign.Center
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    PatientSummary(
                        label = SupervisorConstants.NAME,
                        value = supervisorViewModel.name.value,
                        modifier = Modifier.fillMaxWidth()
                    )
                    PatientSummary(
                        label = SupervisorConstants.LAST_NAME,
                        value = supervisorViewModel.lastname.value,
                        modifier = Modifier.fillMaxWidth()
                    )
                    PatientSummary(
                        label = SupervisorConstants.ID_NUMBER,
                        value = supervisorViewModel.idNumber.value,
                        modifier = Modifier.fillMaxWidth()
                    )
                    PatientSummary(
                        label = SupervisorConstants.GENDER,
                        value = supervisorViewModel.gender.value,
                        modifier = Modifier.fillMaxWidth()
                    )
                    PatientSummary(
                        label = SupervisorConstants.AGE,
                        value = supervisorViewModel.age.value,
                        modifier = Modifier.fillMaxWidth()
                    )
                    PatientSummary(
                        label = SupervisorConstants.TEMPERATURE,
                        value = supervisorViewModel.temperature.value,
                        modifier = Modifier.fillMaxWidth()
                    )
                    PatientSummary(
                        label = SupervisorConstants.HEART_RATE,
                        value = supervisorViewModel.heartRate.value,
                        modifier = Modifier.fillMaxWidth()
                    )
                    PatientSummary(
                        label = SupervisorConstants.BLOOD_OXYGEN,
                        value = supervisorViewModel.bloodOxygen.value,
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider()
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFD5757),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        shape = CircleShape
                    ) {
                        Text(
                            text = "Editar",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    Button(
                        onClick = { onConfirm() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1A80E5),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        shape = CircleShape
                    ) {
                        Text(
                            text = "Enviar",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun PatientSummary(label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = label)
        Text(text = value)
    }
}