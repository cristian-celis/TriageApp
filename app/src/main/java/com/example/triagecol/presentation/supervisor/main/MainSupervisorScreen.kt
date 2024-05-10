package com.example.triagecol.presentation.supervisor.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.traigecol.R
import com.example.triagecol.domain.models.dto.PatientDto
import com.example.triagecol.domain.models.dto.PatientDtoForList
import com.example.triagecol.presentation.common.ConfirmScreenDialog
import com.example.triagecol.presentation.common.RefreshButtonAnimation
import com.example.triagecol.presentation.common.TopBarScreen
import com.example.triagecol.presentation.navigation.AppScreens
import com.example.triagecol.utils.Constants
import com.example.triagecol.utils.SupervisorConstants
import com.example.triagecol.utils.TextConstants

@Composable
fun MainSupervisorScreen(
    navController: NavController,
    mainSupervisorViewModel: MainSupervisorViewModel
) {

    val showDialogForSignOff by mainSupervisorViewModel.showDialogForSignOff.collectAsState()
    val patientList by mainSupervisorViewModel.patientList.collectAsState()
    val successCall by mainSupervisorViewModel.successCall.collectAsState()
    val fetchingData by mainSupervisorViewModel.fetchingData.collectAsState()
    val updatingPatientList by mainSupervisorViewModel.updatingPatientList.collectAsState()

    LaunchedEffect(key1 = true){
        mainSupervisorViewModel.startUpdatePatientList()
    }

    if (showDialogForSignOff) {
        ConfirmScreenDialog(
            mainText = TextConstants.CONFIRM_SIGN_OFF,
            onDismiss = { mainSupervisorViewModel.setDialogForSignOff(false) }
        ) {
            mainSupervisorViewModel.setDialogForSignOff(false)
            mainSupervisorViewModel.clearUserData()
            navController.navigate(AppScreens.LoginScreen.route) {
                popUpTo(AppScreens.SupervisorScreen.route) { inclusive = true }
            }
        }
    }

    if(!fetchingData && successCall && !updatingPatientList){
        mainSupervisorViewModel.getPatientList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF3F1F1))
    ) {

        TopBarScreen(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.085f)
                .padding(bottom = 10.dp)
                .background(Color.White),
            titleText = SupervisorConstants.SUPERVISOR_TEXT,
            backColor = Color(0xA3FF4D4D),
            tintColor = Color.White
        ) {
            mainSupervisorViewModel.setDialogForSignOff(true)
        }

        Box (modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp)){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(7.dp)
            ) {
                Text(
                    text = "Nombre: ${mainSupervisorViewModel.userData.value.name} ${mainSupervisorViewModel.userData.value.lastname}",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                )
                Text(text = "Numero Documento: ${mainSupervisorViewModel.userData.value.idNumber}")
            }
        }

        Box (modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp)){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(7.dp)
            ) {

                Text(
                    text = SupervisorConstants.WAIT_LIST,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 13.dp, bottom = 18.dp)
                )

                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, bottom = 14.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Box (modifier = Modifier
                        .size(38.dp)
                        .background(color = Color(0xA3E7E7E7), shape = ShapeDefaults.Medium)){
                        Icon(painter = painterResource(id = R.drawable.people_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center)
                        )
                    }
                    Text(text = "Total pacientes: ${patientList.size}", modifier = Modifier.padding(start = 10.dp))
                }

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFDADADA),
                    thickness = 1.dp
                )

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 12.dp, end = 12.dp)) {
                    Text(
                        text = "Paciente",
                        style = TextStyle(fontSize = 15.sp)
                    )
                    Text(
                        text = "Identificacion",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        style = TextStyle(fontSize = 15.sp)
                    )
                }

                if (successCall) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(7.dp)
                            .fillMaxHeight(0.8f)
                            .background(Color.White)
                    ) {
                        items(count = patientList.size) {
                            val patient = patientList[it]
                            PatientItemCard(patient = patient)
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }
                } else {
                    Text(text = "Oprime aqui para cargar la lista de espera",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontSize = 15.sp))
                    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                        RefreshButtonAnimation(isRefreshing = fetchingData, refreshIconSize = 45.dp) {
                            mainSupervisorViewModel.getPatientList()
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            AddPatient(modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .align(Alignment.BottomCenter)
                .padding(10.dp),
                navigationOnAddClick = {
                    mainSupervisorViewModel.stopUpdatingPatientList()
                    navController.navigate(AppScreens.PatientScreen.route)
                })
        }
    }
}

@Composable
fun AddPatient(
    modifier: Modifier = Modifier,
    navigationOnAddClick: () -> Unit
) {
    Button(
        onClick = {
            navigationOnAddClick()
        },
        modifier = modifier,
        //.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1A80E5),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = SupervisorConstants.ADD_PATIENT,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(vertical = 3.dp)
        )
    }
}

@Composable
fun PatientItemCard(patient: PatientDtoForList) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFF3F1F1), shape = ShapeDefaults.Small)
            .padding(5.dp)
    ) {
        Text(text = "${patient.name} ${patient.lastname}", modifier = Modifier.padding(start = 3.dp))
        Text(text = patient.idNumber, modifier = Modifier
            .fillMaxWidth()
            .padding(end = 3.dp), textAlign = TextAlign.End)
    }
}