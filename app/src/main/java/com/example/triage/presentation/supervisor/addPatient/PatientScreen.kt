package com.example.triage.presentation.supervisor.addPatient

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.triage.presentation.common.TopBarScreen
import com.example.triage.presentation.navigation.AppScreens
import com.example.triage.presentation.supervisor.addPatient.components.BasicData
import com.example.triage.presentation.supervisor.addPatient.components.SelectGender
import com.example.triage.presentation.supervisor.addPatient.components.SupervisorDialog
import com.example.triage.presentation.supervisor.addPatient.components.VitalSigns
import com.example.triage.utils.StringResources

@Composable
fun PatientScreen(
    navController: NavController,
    patientViewModel: PatientViewModel
) {

    val isValidData by patientViewModel.isDataValid.collectAsState()
    val error by patientViewModel.error.collectAsState()
    val isDialogShown by patientViewModel.isDialogShown.collectAsState()
    val idPatient by patientViewModel.idPatient.collectAsState()

    val focusManager = LocalFocusManager.current

    BackHandler(true) {
        navController.popBackStack()
        patientViewModel.resetData()
    }

    if (isValidData) {
        navController.navigate(
            AppScreens.AddSymptomsScreen.setInitialValues(
                idPatient,
                patientViewModel.patient.value.sex
            )
        )
        patientViewModel.resetData()
    }

    if (isDialogShown) {
        SupervisorDialog(
            onDismiss = { patientViewModel.setIsDialogShown(false) },
            onConfirm = {
                patientViewModel.savePatient()
                patientViewModel.setIsDialogShown(false)
            },
            patientViewModel = patientViewModel
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            }
    ) {
        TopBarScreen(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp), titleText = StringResources.PATIENT_TEXT,
            signOut = false
        ) {
            navController.popBackStack()
            patientViewModel.resetData()
        }

        BasicData(patientViewModel = patientViewModel)

        SelectGender(patientViewModel = patientViewModel)

        VitalSigns(patientViewModel = patientViewModel)

        ContinueButton(patientViewModel = patientViewModel)

        Text(
            text = if (error.isNotEmpty() && !isValidData) error else "",
            style = TextStyle(
                fontSize = 16.sp,
                color = Color(0xFFFF0000)
            ),
            modifier = Modifier
                .padding(7.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(70.dp))
    }
}

@Composable
fun ContinueButton(patientViewModel: PatientViewModel) {

    val isSavingData by patientViewModel.isSavingData.collectAsState()
    val saveEnable by patientViewModel.saveEnable.collectAsState()
    val focusManager = LocalFocusManager.current

    Button(
        onClick = {
            focusManager.clearFocus()
            patientViewModel.setIsDialogShown(true)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(52.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1A80E5),
            disabledContainerColor = Color(0xFFB8CBFA),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        enabled = saveEnable && !isSavingData,
        shape = MaterialTheme.shapes.medium
    ) {
        if (isSavingData) ProgressIndicator()
        else Text(
            text = StringResources.CONTINUE_TEXT,
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
private fun ProgressIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(23.dp),
        color = Color.White,
        strokeWidth = 5.dp
    )
}