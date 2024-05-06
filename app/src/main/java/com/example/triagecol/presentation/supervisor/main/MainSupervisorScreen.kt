package com.example.triagecol.presentation.supervisor.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.NavController
import com.example.triagecol.domain.models.dto.PatientDto
import com.example.triagecol.presentation.common.ConfirmScreenDialog
import com.example.triagecol.presentation.common.RefreshButtonAnimation
import com.example.triagecol.presentation.common.TopBarScreen
import com.example.triagecol.presentation.navigation.AppScreens
import com.example.triagecol.utils.SupervisorConstants
import com.example.triagecol.utils.TextConstants

@Composable
fun MainSupervisorScreen(
    navController: NavController,
    mainSupervisorViewModel: MainSupervisorViewModel
) {

    val showDialogForSignOff by mainSupervisorViewModel.showDialogForSignOff.collectAsState()
    val userData by mainSupervisorViewModel.userData.collectAsState()
    val patientList by mainSupervisorViewModel.patientList.collectAsState()
    val successCall by mainSupervisorViewModel.successCall.collectAsState()
    val patientListUpdated by mainSupervisorViewModel.patientListUpdated.collectAsState()
    val fetchingData by mainSupervisorViewModel.fetchingData.collectAsState()

    if (showDialogForSignOff) {
        ConfirmScreenDialog(
            mainText = TextConstants.CONFIRM_SIGN_OFF,
            onDismiss = { mainSupervisorViewModel.setDialogForSignOff(false) }
        ) {
            mainSupervisorViewModel.setDialogForSignOff(false)
            navController.popBackStack()
            navController.navigate(route = AppScreens.LoginScreen.route)
        }
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
            titleText = SupervisorConstants.MAIN_SCREEN_TEXT,
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
                    text = SupervisorConstants.SUPERVISOR_TEXT,
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Nombre: ${userData.name} ${userData.lastname}",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                )
                Text(text = "Numero Documento: ${userData.idNumber}")
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
                    style = TextStyle(fontSize = 18.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 9.dp, bottom = 16.dp)
                )

                if (successCall && patientListUpdated) {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
                        Text(
                            text = "Nombre Paciente",
                            style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        )
                        Text(
                            text = "Numero Documento",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                            style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        )
                    }
                    LazyColumn(
                        modifier = Modifier
                            .padding(7.dp)
                            .fillMaxHeight(0.8f)
                            .background(Color.White)
                    ) {
                        items(count = patientList.size) {
                            val patient = patientList[it]
                            PatientItemCard(patient = patient)
                            Spacer(modifier = Modifier.height(3.dp))
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(10.dp)
                    ) {
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
fun PatientItemCard(patient: PatientDto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFF3F1F1))
            .padding(5.dp)
    ) {
        Text(text = "${patient.name} ${patient.lastname}")
        Text(text = patient.idNumber, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End)
    }
}