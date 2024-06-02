package com.example.triagecol.presentation.supervisor.addPatient

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import com.example.triagecol.presentation.common.TextFieldComponent
import com.example.triagecol.presentation.common.TopBarScreen
import com.example.triagecol.presentation.navigation.AppScreens
import com.example.triagecol.utils.SupervisorConstants

@Composable
fun PatientScreen(
    navController: NavController,
    patientViewModel: PatientViewModel
) {

    val isValidData by patientViewModel.isDataValid.collectAsState()
    val error by patientViewModel.error.collectAsState()
    val isDialogShown by patientViewModel.isDialogShown.collectAsState()

    val focusManager = LocalFocusManager.current

    if (isValidData) {
        navController.navigate(AppScreens.SymptomsScreen.route) {
            popUpTo(AppScreens.PatientScreen.route) { inclusive = true }
        }
        patientViewModel.resetData()
    }

    if (isDialogShown) {
        SupervisorDialog(
            onDismiss = { patientViewModel.setIsDialogShown(false) },
            onConfirm = {
                patientViewModel.setIsDialogShown(false)
                patientViewModel.savePatient()
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
                .padding(bottom = 10.dp)
            , titleText = SupervisorConstants.PATIENT_TEXT,
            backColor = Color.White,
            tintColor = Color.Black
        ) {
            navController.popBackStack()
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
            text = SupervisorConstants.CONTINUE_TEXT,
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun NameLabelTextField(nameLabel: String) {
    Text(
        text = nameLabel,
        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W500),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 3.dp, top = 5.dp),
        textAlign = TextAlign.Start
    )
}

@Composable
private fun ProgressIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(23.dp),
        color = Color.White,
        strokeWidth = 5.dp
    )
}

@Composable
fun BasicData(patientViewModel: PatientViewModel, modifier: Modifier = Modifier) {
    val patient by patientViewModel.patient.collectAsState()

    Column (modifier = modifier.padding(start = 16.dp, end = 16.dp)){
        NameLabelTextField(SupervisorConstants.NAME)
        TextFieldComponent(
            placeHolderText = SupervisorConstants.NAME, value = patient.name, isTextFieldEnable = false,
            onTextFieldChanged = {
                patientViewModel.updatePatientData(it, PatientData.NAME)
            })
        NameLabelTextField(SupervisorConstants.LAST_NAME)
        TextFieldComponent(
            placeHolderText = SupervisorConstants.LAST_NAME, value = patient.lastname, isTextFieldEnable = false,
            onTextFieldChanged = {
                patientViewModel.updatePatientData(it, PatientData.LASTNAME)
            })
        NameLabelTextField(SupervisorConstants.ID_NUMBER)
        TextFieldComponent(
            placeHolderText = SupervisorConstants.ID_NUMBER, value = patient.idNumber, isTextFieldEnable = false,
            onTextFieldChanged = {
                patientViewModel.updatePatientData(it, PatientData.ID_NUMBER)
            })
        NameLabelTextField(SupervisorConstants.AGE)
        TextFieldComponent(
            placeHolderText = SupervisorConstants.AGE, value = patient.age, isTextFieldEnable = false,
            onTextFieldChanged = {
                patientViewModel.updatePatientData(it, PatientData.AGE)
            })
    }
}

@Composable
fun VitalSigns(patientViewModel: PatientViewModel, modifier: Modifier = Modifier) {
    val patient by patientViewModel.patient.collectAsState()
    Column (modifier = modifier.padding(start = 16.dp, end = 16.dp)){
        NameLabelTextField(SupervisorConstants.TEMPERATURE)
        TextFieldComponent(
            placeHolderText = SupervisorConstants.TEMPERATURE,
            value = patient.temperature,
            isTextFieldEnable = false,
            onTextFieldChanged = {
                patientViewModel.updatePatientData(it, PatientData.TEMPERATURE)
            })
        NameLabelTextField(SupervisorConstants.HEART_RATE)
        TextFieldComponent(
            placeHolderText = SupervisorConstants.HEART_RATE,
            value = patient.heartRate,
            isTextFieldEnable = false,
            onTextFieldChanged = {
                patientViewModel.updatePatientData(it, PatientData.HEART_RATE)
            })
        NameLabelTextField(SupervisorConstants.BLOOD_OXYGEN)
        TextFieldComponent(
            placeHolderText = SupervisorConstants.BLOOD_OXYGEN,
            value = patient.bloodOxygen,
            isTextFieldEnable = false,
            onTextFieldChanged = {
                patientViewModel.updatePatientData(it, PatientData.BLOOD_OXYGEN)
            })
    }
}

@Composable
fun SelectGender(
    patientViewModel: PatientViewModel,
    modifier: Modifier = Modifier
) {
    val options = listOf(
        SupervisorConstants.FEMALE_TEXT,
        SupervisorConstants.MALE_TEXT
    )
    val patient by patientViewModel.patient.collectAsState()

    Column (modifier = modifier.padding(start = 16.dp, end = 16.dp)){
        NameLabelTextField(SupervisorConstants.SEX)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 3.dp, bottom = 2.dp), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            options.forEach { option ->
                Row {
                    RadioButton(
                        selected = patient.sex == option,
                        onClick = { patientViewModel.updatePatientData(option, PatientData.SEX) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF1A80E5)
                        )
                    )
                    Text(
                        text = option,
                        modifier = Modifier
                            .padding(top = 13.dp)
                    )
                }
            }
        }
    }
}