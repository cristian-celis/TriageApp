package com.example.triage.presentation.supervisor.main

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.traigecol.R
import com.example.triage.presentation.common.ConfirmScreenDialog
import com.example.triage.presentation.common.TopBarScreen
import com.example.triage.presentation.navigation.AppScreens
import com.example.triage.presentation.supervisor.main.components.AccordionScreen
import com.example.triage.utils.StringResources

@Composable
fun MainSupervisorScreen(
    navController: NavController,
    mainSupervisorViewModel: MainSupervisorViewModel,
    idSupervisor: String
) {

    val showDialogForSignOff by mainSupervisorViewModel.showDialogForSignOff.collectAsState()
    val successCall by mainSupervisorViewModel.successCall.collectAsState()
    val fetchingData by mainSupervisorViewModel.fetchingPatients.collectAsState()
    val updatingPatientList by mainSupervisorViewModel.waitListUpdateEnabled.collectAsState()
    val error by mainSupervisorViewModel.error.collectAsState()

    LaunchedEffect(key1 = true) {
        if (mainSupervisorViewModel.supervisorAccount.value.idNumber.isBlank()) {
            mainSupervisorViewModel.getSupervisorData(idSupervisor)
        }
        mainSupervisorViewModel.startUpdatePatientList()
    }

    BackHandler(true) {
        mainSupervisorViewModel.setDialogForSignOff(true)
    }

    if (showDialogForSignOff) {
        ConfirmScreenDialog(
            mainText = StringResources.CONFIRM_SIGN_OFF,
            onDismiss = { mainSupervisorViewModel.setDialogForSignOff(false) }
        ) {
            mainSupervisorViewModel.setDialogForSignOff(false)
            mainSupervisorViewModel.clearUserData()
            navController.navigate(AppScreens.LoginScreen.route) {
                popUpTo(AppScreens.MainSupervisorScreen.route) { inclusive = true }
            }
        }
    }

    val context = LocalContext.current
    if (error.isNotBlank() && !successCall) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    if (!fetchingData && successCall && !updatingPatientList) {
        mainSupervisorViewModel.getPatientList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = Color(0xFFF3F1F1))
    ) {

        TopBarScreen(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .background(Color.White),
            titleText = StringResources.SUPERVISOR_TEXT,
            signOut = true
        ) {
            mainSupervisorViewModel.setDialogForSignOff(true)
        }

        HeaderScreen(mainSupervisorViewModel = mainSupervisorViewModel)

        BodyScreen(modifier = Modifier.weight(1f), mainSupervisorViewModel = mainSupervisorViewModel)

        Box(modifier = Modifier.fillMaxSize()) {
            AddPatientButton(modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .align(Alignment.BottomCenter)
                .padding(10.dp),
                navigationOnAddClick = {
                    mainSupervisorViewModel.stopUpdatingPatientList()
                    navController.navigate(AppScreens.AddPatientScreen.route)
                })
        }
    }
}

@Composable
fun AddPatientButton(
    modifier: Modifier = Modifier,
    navigationOnAddClick: () -> Unit
) {
    Button(
        onClick = {
            navigationOnAddClick()
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1A80E5),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = StringResources.ADD_PATIENT,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(vertical = 3.dp)
        )
    }
}

@Composable
fun HeaderScreen(modifier: Modifier = Modifier, mainSupervisorViewModel: MainSupervisorViewModel) {
    val supervisorAccount by mainSupervisorViewModel.supervisorAccount.collectAsState()
    val fetchingStaffMember by mainSupervisorViewModel.fetchingStaffMember.collectAsState()

    Surface(
        modifier = modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        shape = RoundedCornerShape(4.dp),
        shadowElevation = 4.dp,
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp)
        ) {
            Row {
                Text(
                    text = "Nombre: ${supervisorAccount.fullName}",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                )
                if (fetchingStaffMember) ProgressIndicator()
            }
            Row {
                Text(text = "Identificacion: ${supervisorAccount.idNumber}")
                if (fetchingStaffMember) ProgressIndicator()
            }
        }
    }
}

@Composable
fun BodyScreen(modifier: Modifier = Modifier, mainSupervisorViewModel: MainSupervisorViewModel) {
    val patientList by mainSupervisorViewModel.patientList.collectAsState()

    Surface(
        modifier = modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        shape = RoundedCornerShape(4.dp),
        shadowElevation = 4.dp,
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {

            Text(
                text = StringResources.WAIT_LIST,
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 20.dp, bottom = 17.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(color = Color(0xA3E7E7E7), shape = ShapeDefaults.Medium)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.people_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center)
                    )
                }
                Text(
                    text = "Total pacientes: ${patientList.size}",
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
            AccordionScreen(
                modifier = Modifier,
                mainSupervisorViewModel = mainSupervisorViewModel
            )
        }
    }
}

@Composable
private fun ProgressIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(25.dp),
        color = Color.Black,
        strokeWidth = 3.dp
    )
}