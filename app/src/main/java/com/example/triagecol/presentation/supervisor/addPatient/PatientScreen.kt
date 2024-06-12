package com.example.triagecol.presentation.supervisor.addPatient

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.triagecol.presentation.common.LabelTextField
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
                .padding(bottom = 10.dp), titleText = SupervisorConstants.PATIENT_TEXT,
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
            text = SupervisorConstants.CONTINUE_TEXT,
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

@Composable
fun BasicData(patientViewModel: PatientViewModel, modifier: Modifier = Modifier) {
    val birthDialogShown by patientViewModel.birthDialogShown.collectAsState()
    val patient by patientViewModel.patient.collectAsState()

    Column(modifier = modifier.padding(start = 20.dp, end = 20.dp)) {

        Text(
            text = "Datos básicos",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            textAlign = TextAlign.Start,
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Medium)
        )

        LabelTextField(nameLabel = SupervisorConstants.NAME)
        TextFieldComponent(
            modifier = Modifier.padding(bottom = 14.dp),
            labelTitle = SupervisorConstants.NAME,
            helpText = "Ingrese el nombre",
            value = patient.name,
            onTextFieldChanged = {
                patientViewModel.updatePatientData(it, PatientData.NAME)
            })
        LabelTextField(nameLabel = SupervisorConstants.LAST_NAME)
        TextFieldComponent(
            modifier = Modifier.padding(bottom = 14.dp),
            labelTitle = SupervisorConstants.LAST_NAME,
            helpText = "Ingrese el apellido",
            value = patient.lastname,
            onTextFieldChanged = {
                patientViewModel.updatePatientData(it, PatientData.LASTNAME)
            })
        LabelTextField(nameLabel = SupervisorConstants.ID_NUMBER)
        TextFieldComponent(
            modifier = Modifier.padding(bottom = 14.dp),
            labelTitle = SupervisorConstants.ID_NUMBER,
            helpText = "Ingrese la identificacion",
            value = patient.idNumber,
            onTextFieldChanged = {
                patientViewModel.updatePatientData(it, PatientData.ID_NUMBER)
            })
        LabelTextField(nameLabel = SupervisorConstants.AGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            TextFieldComponent(modifier = Modifier.clickable {
                patientViewModel.setBirthDialogShown(
                    true
                )
            },
                labelTitle = SupervisorConstants.AGE, value = patient.age,
                helpText = "Ingrese la edad",
                onTextFieldChanged = {})
            if (birthDialogShown) {
                DateOfBirthInput({
                    patientViewModel.updatePatientData(it, PatientData.AGE)
                    patientViewModel.setBirthDialogShown(false)
                }) {
                    patientViewModel.setBirthDialogShown(false)
                }
            }
        } else {
            Toast.makeText(
                LocalContext.current,
                "Ingrese la edad del paciente manualmente",
                Toast.LENGTH_LONG
            ).show()
            TextFieldComponent(
                modifier = Modifier.padding(bottom = 14.dp),
                labelTitle = SupervisorConstants.AGE,
                helpText = "Ingrese la edad",
                value = patient.age,
                onTextFieldChanged = {
                    patientViewModel.updatePatientData(it, PatientData.AGE)
                })
        }
    }
}

@Composable
fun VitalSigns(patientViewModel: PatientViewModel, modifier: Modifier = Modifier) {
    val patient by patientViewModel.patient.collectAsState()
    val temperatureError by patientViewModel.temperatureError.collectAsState()
    val heartRateError by patientViewModel.heartRateError.collectAsState()
    val bloodOxygenError by patientViewModel.bloodOxygenError.collectAsState()

    Column(modifier = modifier.padding(start = 20.dp, end = 20.dp)) {
        Text(
            text = "Signos vitales",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            textAlign = TextAlign.Start,
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Medium)
        )

        LabelTextField(nameLabel = SupervisorConstants.TEMPERATURE)
        TextFieldComponent(
            labelTitle = SupervisorConstants.TEMPERATURE,
            helpText = "Ingrese la temperatura",
            value = patient.temperature,
            onTextFieldChanged = {
                if (it.length < 3) {
                    patientViewModel.updatePatientData(it, PatientData.TEMPERATURE)
                    patientViewModel.checkVitalSigns(it, Signs.TEMPERATURE)
                }
            })
        SignErrorLabel(
            modifier = Modifier
                .fillMaxWidth(), !temperatureError, 28, 43, "°C"
        )
        LabelTextField(nameLabel = SupervisorConstants.HEART_RATE)
        TextFieldComponent(
            labelTitle = SupervisorConstants.HEART_RATE,
            helpText = "Ingrese la frecuencia cardiaca",
            value = patient.heartRate,
            onTextFieldChanged = {
                if (it.length <= 3) {
                    patientViewModel.updatePatientData(it, PatientData.HEART_RATE)
                    patientViewModel.checkVitalSigns(it, Signs.HEART_RATE)
                }
            })
        SignErrorLabel(
            modifier = Modifier
                .fillMaxWidth(), !heartRateError, 40, 200, "bpm"
        )
        LabelTextField(nameLabel = SupervisorConstants.BLOOD_OXYGEN)
        TextFieldComponent(
            labelTitle = SupervisorConstants.BLOOD_OXYGEN,
            helpText = "Ingrese la oxigenacion sanguinea",
            value = patient.bloodOxygen,
            onTextFieldChanged = {
                if (it.length <= 3) {
                    patientViewModel.updatePatientData(it, PatientData.BLOOD_OXYGEN)
                    patientViewModel.checkVitalSigns(it, Signs.BLOOD_OXYGEN)
                }
            })
        SignErrorLabel(
            modifier = Modifier
                .fillMaxWidth(), !bloodOxygenError, 80, 100, "%"
        )
    }
}

@Composable
fun SignErrorLabel(modifier: Modifier = Modifier, show: Boolean, min: Int, max: Int, unitMeasurement: String) {
    Box(modifier = modifier.padding(bottom = 14.dp)){
        Crossfade(targetState = !show, label = "") { isVisible ->
            if (isVisible) {
                Text(
                    text = "Valor entre $min y $max",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.W300),
                    textAlign = TextAlign.End,
                )
            }
        }
        Crossfade(targetState = show, label = "") { isVisible ->
            if (isVisible) {
                Text(
                    text = "Valor fuera de rango ($min - $max $unitMeasurement)",
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Red,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.End
                )
            }
        }
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

    Column(modifier = modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp)) {
        LabelTextField(nameLabel = "Sexo")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 22.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            options.forEach { option ->
                Row {
                    RadioButton(
                        selected = patient.sex == option,
                        onClick = {
                            patientViewModel.updatePatientData(
                                option,
                                PatientData.SEX
                            )
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF1A80E5)
                        )
                    )
                    Text(
                        text = option,
                        modifier = Modifier
                            .padding(top = 12.dp)
                    )
                }
            }
        }
    }
}