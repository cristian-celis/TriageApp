package com.example.triagecol.presentation.supervisor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.traigecol.R
import com.example.triagecol.presentation.common.ConfirmScreenDialog
import com.example.triagecol.presentation.common.TextFieldComponent
import com.example.triagecol.presentation.common.TopBarScreen
import com.example.triagecol.presentation.navigation.AppScreens
import com.example.triagecol.utils.SupervisorConstants
import com.example.triagecol.utils.TextConstants

@Composable
fun SupervisorScreen(
    navController: NavController,
    supervisorViewModel: SupervisorViewModel
) {

    val idNumber by supervisorViewModel.idNumber.collectAsState()
    val name by supervisorViewModel.name.collectAsState()
    val lastname by supervisorViewModel.lastname.collectAsState()
    val age by supervisorViewModel.age.collectAsState()

    val isValidData by supervisorViewModel.isDataValid.collectAsState()
    val error by supervisorViewModel.error.collectAsState()
    val isDialogShown by supervisorViewModel.isDialogShown.collectAsState()
    val showDialogForSignOff by supervisorViewModel.showDialogForSignOff.collectAsState()

    val focusManager = LocalFocusManager.current

    if (isValidData) {
        navController.navigate(AppScreens.SymptomsScreen.route)
        supervisorViewModel.resetData()
    }

    if (showDialogForSignOff) {
        ConfirmScreenDialog(
            mainText = TextConstants.CONFIRM_SIGN_OFF,
            onDismiss = {supervisorViewModel.setDialogForSignOff(false)}
        ) {
            supervisorViewModel.setDialogForSignOff(false)
            navController.popBackStack()
            navController.navigate(route = AppScreens.LoginScreen.route)
        }
    }

    if (isDialogShown) {
        SupervisorDialog(
            onDismiss = { supervisorViewModel.setIsDialogShown(false) },
            onConfirm = {
                supervisorViewModel.setIsDialogShown(false)
                supervisorViewModel.savePatient()
            },
            supervisorViewModel = supervisorViewModel
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            }
    ) {

        TopBarScreen(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.09f)
                .padding(top = 15.dp, bottom = 12.dp), titleText = SupervisorConstants.PATIENT_TEXT
        ) {
            supervisorViewModel.setDialogForSignOff(true)
        }

        NameLabelTextField(SupervisorConstants.NAME)
        TextFieldComponent(
            placeHolderText = SupervisorConstants.NAME, value = name, isTextFieldEnable = false,
            onTextFieldChanged = {
                supervisorViewModel.updateUserData(
                    idNumber, it, lastname, age
                )
            })
        NameLabelTextField(SupervisorConstants.LAST_NAME)
        TextFieldComponent(
            placeHolderText = SupervisorConstants.LAST_NAME,
            value = lastname,
            isTextFieldEnable = false,
            onTextFieldChanged = {
                supervisorViewModel.updateUserData(
                    idNumber, name, it, age
                )
            })
        NameLabelTextField(SupervisorConstants.ID_NUMBER)
        TextFieldComponent(
            placeHolderText = SupervisorConstants.ID_NUMBER,
            value = idNumber,
            isTextFieldEnable = false,
            onTextFieldChanged = {
                supervisorViewModel.updateUserData(
                    it, name, lastname, age
                )
            })
        NameLabelTextField(SupervisorConstants.AGE)
        TextFieldComponent(
            placeHolderText = SupervisorConstants.AGE,
            value = age,
            isTextFieldEnable = false,
            onTextFieldChanged = {
                supervisorViewModel.updateUserData(idNumber, name, lastname, it)
            })

        NameLabelTextField(SupervisorConstants.GENDER)

        SelectGender(supervisorViewModel = supervisorViewModel)

        VitalSigns(supervisorViewModel)

        ContinueButton(supervisorViewModel = supervisorViewModel)

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
fun ContinueButton(supervisorViewModel: SupervisorViewModel) {

    val isSavingData by supervisorViewModel.isSavingData.collectAsState()
    val saveEnable by supervisorViewModel.saveEnable.collectAsState()
    val focusManager = LocalFocusManager.current

    Button(
        onClick = {
            focusManager.clearFocus()
            supervisorViewModel.setIsDialogShown(true)
        },
        modifier = Modifier
            .fillMaxWidth()
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
fun VitalSigns(supervisorViewModel: SupervisorViewModel) {
    val temperature by supervisorViewModel.temperature.collectAsState()
    val heartRate by supervisorViewModel.heartRate.collectAsState()
    val bloodOxygen by supervisorViewModel.bloodOxygen.collectAsState()

    NameLabelTextField(SupervisorConstants.TEMPERATURE)
    TextFieldComponent(
        placeHolderText = SupervisorConstants.TEMPERATURE,
        value = temperature,
        isTextFieldEnable = false,
        onTextFieldChanged = {
            supervisorViewModel.updateVitalSigns(it, heartRate, bloodOxygen)
        })
    NameLabelTextField(SupervisorConstants.HEART_RATE)
    TextFieldComponent(
        placeHolderText = SupervisorConstants.HEART_RATE,
        value = heartRate,
        isTextFieldEnable = false,
        onTextFieldChanged = {
            supervisorViewModel.updateVitalSigns(temperature, it, bloodOxygen)
        })
    NameLabelTextField(SupervisorConstants.BLOOD_OXYGEN)
    TextFieldComponent(
        placeHolderText = SupervisorConstants.BLOOD_OXYGEN,
        value = bloodOxygen,
        isTextFieldEnable = false,
        onTextFieldChanged = {
            supervisorViewModel.updateVitalSigns(temperature, heartRate, it)
        })
}

@Composable
fun SelectGender(
    supervisorViewModel: SupervisorViewModel,
) {
    val options = listOf(
        SupervisorConstants.FEMALE_TEXT,
        SupervisorConstants.MALE_TEXT,
        SupervisorConstants.OTHER_TEXT
    )
    val role: String by supervisorViewModel.gender.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 2.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        options.forEach { option ->
            Row {
                RadioButton(
                    selected = role == option,
                    onClick = { supervisorViewModel.setGender(option) },
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