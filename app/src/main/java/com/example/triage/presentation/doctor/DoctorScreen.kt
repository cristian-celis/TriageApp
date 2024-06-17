package com.example.triage.presentation.doctor

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.triage.presentation.common.ConfirmScreenDialog
import com.example.triage.presentation.common.TopBarScreen
import com.example.triage.presentation.navigation.AppScreens
import com.example.triage.utils.StringResources

@Composable
fun DoctorScreen(
    navController: NavController,
    doctorViewModel: DoctorViewModel,
    idDoctor: String
) {

    val showDialogForSignOff by doctorViewModel.showDialogForSignOff.collectAsState()
    val error by doctorViewModel.error.collectAsState()
    val successCall by doctorViewModel.successCall.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        doctorViewModel.getDoctorData(idDoctor)
    }

    BackHandler(true) {
        doctorViewModel.setDialogForSignOff(true)
    }

    if (showDialogForSignOff) {
        ConfirmScreenDialog(mainText = StringResources.CONFIRM_SIGN_OFF,
            onDismiss = { doctorViewModel.setDialogForSignOff(false) }) {
            doctorViewModel.clearAll()
            navController.navigate(AppScreens.LoginScreen.route) {
                popUpTo(AppScreens.DoctorScreen.route) { inclusive = true }
            }
        }
    }

    if (!successCall && error.isNotBlank()) {
        Toast.makeText(
            context,
            error.ifEmpty { error }, Toast.LENGTH_SHORT
        ).show()
        doctorViewModel.clearError()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF3F1F1))
    ) {

        TopBarScreen(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .background(Color.White),
            titleText = StringResources.DOCTOR_SCREEN,
            signOut = true
        ) {
            doctorViewModel.setDialogForSignOff(true)
        }

        DoctorData(
            doctorViewModel = doctorViewModel, modifier = Modifier.fillMaxWidth()
        )

        PatientSectionScreen(
            doctorViewModel = doctorViewModel,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        GetAndEndPatientConsultBtt(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .height(55.dp),
            doctorViewModel = doctorViewModel
        )
    }
}

@Composable
fun DoctorData(doctorViewModel: DoctorViewModel, modifier: Modifier = Modifier) {
    val doctorAccount by doctorViewModel.doctorAccount.collectAsState()
    val patientsWaitingCount by doctorViewModel.patientsWaitingCount.collectAsState()
    val isDoctorOnline by doctorViewModel.isDoctorOnline.collectAsState()

    Surface(
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        shape = RoundedCornerShape(4.dp),
        shadowElevation = 4.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "Dr. ${doctorAccount.fullName}", style = TextStyle(
                        fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.Black
                    )
                )
                Text(
                    text = "Identificacion: ${doctorAccount.idNumber}",
                    style = TextStyle(fontSize = 14.sp)
                )
                Surface(
                    tonalElevation = 5.dp,
                    shape = RoundedCornerShape(5.dp),
                    color = if (isDoctorOnline) Color(0xFF1A80E5) else Color(0xFFB8CBFA),
                    contentColor = Color.White,
                    modifier = Modifier.padding(top = 3.dp)
                ) {
                    Text(
                        text = "En espera: $patientsWaitingCount",
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                }
            }

            OnlineSwitch(doctorViewModel = doctorViewModel) {
                doctorViewModel.updateDoctorStatus(it)
            }
        }
    }
}

@Composable
fun GetAndEndPatientConsultBtt(doctorViewModel: DoctorViewModel, modifier: Modifier = Modifier) {
    val doctorInConsultation by doctorViewModel.doctorInConsultation.collectAsState()
    val isDoctorOnline by doctorViewModel.isDoctorOnline.collectAsState()
    val isFetchingPatients by doctorViewModel.isFetchingPatients.collectAsState()

    Button(
        onClick = {
            if (doctorInConsultation) doctorViewModel.endConsultation()
            else doctorViewModel.assignPatient()
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (doctorInConsultation) Color(0xFFFF3030) else Color(0xFF1A80E5),
            disabledContainerColor = Color(0xFFB8CBFA),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        enabled = isDoctorOnline,
        shape = ShapeDefaults.Medium
    ) {
        if (isFetchingPatients) {
            ProgressIndicator(
                size = 35.dp, color = Color.White, strokeWidth = 5.dp, padding = 3.dp
            )
        } else {
            Text(
                text = if (doctorInConsultation) StringResources.END_CONSULTATION else StringResources.GET_PATIENT,
                textAlign = TextAlign.Center,
                style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            )
        }
    }
}

@Composable
private fun OnlineSwitch(doctorViewModel: DoctorViewModel, onCheckedChange: (Boolean) -> Unit) {
    val isDoctorOnline by doctorViewModel.isDoctorOnline.collectAsState()
    val changingDoctorState by doctorViewModel.updatingDocStatus.collectAsState()
    val doctorInConsultation by doctorViewModel.doctorInConsultation.collectAsState()
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 10.dp)
    ) {
        Text(
            text = if (isDoctorOnline) StringResources.ONLINE else StringResources.OFFLINE,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            style = TextStyle(color = Color.Gray, fontSize = 14.sp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (changingDoctorState)
                Box(modifier = Modifier.padding(top = 2.dp)) {
                    ProgressIndicator(
                        size = 28.dp,
                        color = Color(0xFF1A80E5),
                        strokeWidth = 3.dp,
                        padding = 5.dp
                    )
                }
            Switch(checked = isDoctorOnline, onCheckedChange = {
                if (doctorInConsultation) Toast.makeText(
                    context, StringResources.CONSULTATION_IN_PROGRESS_MESSAGE, Toast.LENGTH_LONG
                ).show()
                else onCheckedChange(it)
            }, thumbContent = if (isDoctorOnline) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                }
            } else {
                null
            }, enabled = !changingDoctorState, colors = SwitchDefaults.colors(
                checkedTrackColor = Color(0xFFBAD4EE),
                checkedThumbColor = Color(0xFF1A80E5),
                checkedIconColor = Color.White,
                checkedBorderColor = Color(0xFFBAD4EE),
                uncheckedTrackColor = Color(0xFFE9E9E9),
                uncheckedThumbColor = Color(0xFF9B9B9B),
                uncheckedBorderColor = Color(0xFFE9E9E9),
                disabledCheckedThumbColor = Color(0xFF1A80E5),
                disabledUncheckedThumbColor = Color(0xFF1A80E5)
            ))
        }
    }
}

@Composable
private fun ProgressIndicator(size: Dp, color: Color, strokeWidth: Dp, padding: Dp) {
    CircularProgressIndicator(
        modifier = Modifier
            .size(size)
            .padding(end = padding),
        color = color,
        strokeWidth = strokeWidth
    )
}