package com.example.triagecol.presentation.supervisor.addPatient

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.example.triagecol.presentation.admin.details.DetailMode
import com.example.triagecol.presentation.common.ConfirmScreenDialog
import com.example.triagecol.presentation.common.TextFieldComponent
import com.example.triagecol.presentation.common.TopBarScreen
import com.example.triagecol.presentation.navigation.AppScreens
import com.example.triagecol.utils.SupervisorConstants
import com.example.triagecol.utils.TextConstants

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
            .padding(top = 15.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            }
    ) {
        TopBarScreen(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.085f).padding(bottom = 10.dp)
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

        Spacer(modifier = Modifier.height(140.dp))
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
            .fillMaxWidth().padding(horizontal = 16.dp)
            .height(52.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1A80E5),
            disabledContainerColor = Color(0xFFB8CBFA),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        enabled = saveEnable, shape = MaterialTheme.shapes.medium
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
            .padding(bottom = 5.dp, top = 7.dp),
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
    val idNumber by patientViewModel.idNumber.collectAsState()
    val name by patientViewModel.name.collectAsState()
    val lastname by patientViewModel.lastname.collectAsState()
    val age by patientViewModel.age.collectAsState()

    Column (modifier = modifier.padding(start = 16.dp, end = 16.dp)){
        NameLabelTextField(SupervisorConstants.NAME)
        TextFieldComponent(
            placeHolderText = SupervisorConstants.NAME, value = name, isTextFieldEnable = false,
            onTextFieldChanged = {
                patientViewModel.updateUserData(
                    idNumber, it, lastname, age
                )
            })
        NameLabelTextField(SupervisorConstants.LAST_NAME)
        TextFieldComponent(
            placeHolderText = SupervisorConstants.LAST_NAME,
            value = lastname,
            isTextFieldEnable = false,
            onTextFieldChanged = {
                patientViewModel.updateUserData(
                    idNumber, name, it, age
                )
            })
        NameLabelTextField(SupervisorConstants.ID_NUMBER)
        TextFieldComponent(
            placeHolderText = SupervisorConstants.ID_NUMBER,
            value = idNumber,
            isTextFieldEnable = false,
            onTextFieldChanged = {
                patientViewModel.updateUserData(
                    it, name, lastname, age
                )
            })
        NameLabelTextField(SupervisorConstants.AGE)
        TextFieldComponent(
            placeHolderText = SupervisorConstants.AGE,
            value = age,
            isTextFieldEnable = false,
            onTextFieldChanged = {
                patientViewModel.updateUserData(idNumber, name, lastname, it)
            })
    }
}

@Composable
fun VitalSigns(patientViewModel: PatientViewModel, modifier: Modifier = Modifier) {
    val temperature by patientViewModel.temperature.collectAsState()
    val heartRate by patientViewModel.heartRate.collectAsState()
    val bloodOxygen by patientViewModel.bloodOxygen.collectAsState()

    Column (modifier = modifier.padding(start = 16.dp, end = 16.dp)){
        NameLabelTextField(SupervisorConstants.TEMPERATURE)
        TextFieldComponent(
            placeHolderText = SupervisorConstants.TEMPERATURE,
            value = temperature,
            isTextFieldEnable = false,
            onTextFieldChanged = {
                patientViewModel.updateVitalSigns(it, heartRate, bloodOxygen)
            })
        NameLabelTextField(SupervisorConstants.HEART_RATE)
        TextFieldComponent(
            placeHolderText = SupervisorConstants.HEART_RATE,
            value = heartRate,
            isTextFieldEnable = false,
            onTextFieldChanged = {
                patientViewModel.updateVitalSigns(temperature, it, bloodOxygen)
            })
        NameLabelTextField(SupervisorConstants.BLOOD_OXYGEN)
        TextFieldComponent(
            placeHolderText = SupervisorConstants.BLOOD_OXYGEN,
            value = bloodOxygen,
            isTextFieldEnable = false,
            onTextFieldChanged = {
                patientViewModel.updateVitalSigns(temperature, heartRate, it)
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
        SupervisorConstants.MALE_TEXT,
        SupervisorConstants.OTHER_TEXT
    )
    val role: String by patientViewModel.gender.collectAsState()

    Column (modifier = modifier.padding(start = 16.dp, end = 16.dp)){
        NameLabelTextField(SupervisorConstants.GENDER)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 2.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEach { option ->
                Row {
                    RadioButton(
                        selected = role == option,
                        onClick = { patientViewModel.setGender(option) },
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