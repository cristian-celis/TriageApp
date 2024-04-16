package com.example.triagecol.presentation.doctor

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.traigecol.R
import com.example.triagecol.presentation.common.ConfirmScreenDialog
import com.example.triagecol.presentation.common.TopBarScreen
import com.example.triagecol.presentation.navigation.AppScreens
import com.example.triagecol.utils.DoctorConstants
import com.example.triagecol.utils.TextConstants

@Composable
fun DoctorScreen(navController: NavController, doctorViewModel: DoctorViewModel) {

    val error by doctorViewModel.error.collectAsState()
    val showDialog by doctorViewModel.showDialog.collectAsState()

    if(showDialog){
        ConfirmScreenDialog(mainText = TextConstants.CONFIRM_SIGN_OFF,
            onDismiss = {doctorViewModel.setDialog(false)}) {
            doctorViewModel.setDialog(false)
            doctorViewModel.updateDoctorStatus(false)
            navController.navigate(AppScreens.LoginScreen.route) {
                popUpTo(AppScreens.DoctorScreen.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        TopBarScreen(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.09f)
                .padding(10.dp),
            titleText = DoctorConstants.DOCTOR_SCREE
        ) {
            doctorViewModel.setDialog(true)
        }

        DoctorData(
            doctorViewModel = doctorViewModel, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )

        Text(
            text = error, style = TextStyle(color = Color.Red), modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp), textAlign = TextAlign.Center
        )

        HorizontalDivider(modifier = Modifier.padding(bottom = 15.dp))

        PatientSectionScreen(
            doctorViewModel = doctorViewModel,
            modifier = Modifier
                .fillMaxHeight(0.88f)
                .fillMaxWidth()
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(Color.Transparent)
        ) {
            GetAndEndPatientConsultBtt(
                doctorViewModel = doctorViewModel,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxHeight()
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun DoctorData(doctorViewModel: DoctorViewModel, modifier: Modifier = Modifier) {
    val doctorData by doctorViewModel.doctorData.collectAsState()

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Text(
                text = "${doctorData.name} ${doctorData.lastname}", style = TextStyle(
                    fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.Black
                )
            )
            Text(
                text = "Id: ${doctorData.idNumber}", style = TextStyle(fontSize = 14.sp)
            )
        }


        OnlineSwitch(doctorViewModel = doctorViewModel) {
            doctorViewModel.updateDoctorStatus(it)
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
            if (doctorInConsultation) doctorViewModel.setDoctorInConsultation(false)
            else doctorViewModel.assignPatient()
        },
        modifier = modifier
            .padding(5.dp),
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
                size = 35.dp,
                color = Color.White,
                strokeWidth = 5.dp,
                padding = 3.dp
            )
        } else {
            Text(
                text = if (doctorInConsultation) DoctorConstants.END_CONSULTATION else DoctorConstants.GET_PATIENT,
                textAlign = TextAlign.Center,
                style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            )
        }
    }
}

@Composable
private fun OnlineSwitch(doctorViewModel: DoctorViewModel, onCheckedChange: (Boolean) -> Unit) {
    val isDoctorOnline by doctorViewModel.isDoctorOnline.collectAsState()
    val changingDoctorState by doctorViewModel.changingDoctorState.collectAsState()
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
            text = if (isDoctorOnline) DoctorConstants.ONLINE else DoctorConstants.OFFLINE,
            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End,
            style = TextStyle(color = Color.Gray, fontSize = 14.sp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (changingDoctorState) ProgressIndicator(
                size = 25.dp,
                color = Color(0xFF1A80E5),
                strokeWidth = 5.dp,
                padding = 5.dp
            )
            Switch(checked = isDoctorOnline,
                onCheckedChange = {
                    if (doctorInConsultation) Toast.makeText(
                        context,
                        DoctorConstants.CONSULTATION_IN_PROGRESS_MESSAGE,
                        Toast.LENGTH_LONG
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
                }, enabled = !changingDoctorState,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Color(0xFFBAD4EE),
                    checkedThumbColor = Color(0xFF1A80E5),
                    checkedIconColor = Color.White,
                    checkedBorderColor = Color(0xFFBAD4EE),
                    uncheckedTrackColor = Color(0xFFCFCFCF),
                    uncheckedThumbColor = Color.Gray,
                    uncheckedBorderColor = Color.Gray,
                    disabledCheckedThumbColor = Color(0xFF1A80E5),
                    disabledUncheckedThumbColor = Color(0xFF1A80E5)
                )
            )
        }
    }
}

@Composable
private fun ProgressIndicator(size: Dp, color: Color, strokeWidth: Dp, padding: Dp) {
    CircularProgressIndicator(
        modifier = Modifier
            .size(size)
            .padding(end = padding), color = color, strokeWidth = strokeWidth
    )
}