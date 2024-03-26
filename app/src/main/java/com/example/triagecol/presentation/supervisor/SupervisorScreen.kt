package com.example.triagecol.presentation.supervisor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.ShapeDefaults
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
import com.example.triagecol.presentation.admin.details.DetailCardViewModel
import com.example.triagecol.presentation.common.TextFieldComponent
import com.example.triagecol.presentation.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
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
    val isSavingData by supervisorViewModel.isSavingData.collectAsState()
    val saveEnable by supervisorViewModel.saveEnable.collectAsState()

    val focusManager = LocalFocusManager.current

    if(isValidData){
        navController.navigate(AppScreens.VitalSignsScreen.route)
        supervisorViewModel.setValidData(false)
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_go_back),
                contentDescription = null,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            navController.popBackStack()
                            navController.navigate(route = AppScreens.LoginScreen.route)
                        }
                    )
                    .size(25.dp))

            Text(
                text = "Paciente",
                style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        NameLabelTextField("Numero de Identificacion")
        TextFieldComponent(
            placeHolderText = "Numero de Identificacion", value = idNumber, isTextFieldEnable = false,
            onTextFieldChanged = {
                supervisorViewModel.updateUserData(
                    it, name, lastname, age
                )
            })
        NameLabelTextField("Nombre")
        TextFieldComponent(
            placeHolderText = "Nombre", value = name, isTextFieldEnable = false,
            onTextFieldChanged = {
                supervisorViewModel.updateUserData(
                    idNumber, it, lastname, age
                )
            })
        NameLabelTextField("Apellido")
        TextFieldComponent(
            placeHolderText = "Apellido", value = lastname, isTextFieldEnable = false,
            onTextFieldChanged = {
                supervisorViewModel.updateUserData(
                    idNumber, name, it, age
                )
            })
        NameLabelTextField("Edad")
        TextFieldComponent(
            placeHolderText = "Edad",
            value = age,
            isTextFieldEnable = false,
            onTextFieldChanged = {
                supervisorViewModel.updateUserData(idNumber, name, lastname, it)
            })

        NameLabelTextField("Genero")
        SelectGender(supervisorViewModel = supervisorViewModel)

        VitalSigns(supervisorViewModel)

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)){
            Button(
                onClick = { supervisorViewModel.sendPatientData() },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .height(55.dp).width(180.dp),
                shape = ShapeDefaults.Medium,
                enabled = saveEnable && !isSavingData,
                colors = ButtonColors(
                    containerColor = Color(0xFF1A80E5),
                    disabledContainerColor = Color(0xFFAACBEB),
                    contentColor = Color.White,
                    disabledContentColor = Color.White
                )
            ) {
                if(supervisorViewModel.isSavingData.value){
                    ProgressIndicator()
                }else{
                    Text(text = "Continuar",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.W500))
                }
            }
            if (!isValidData) {
                Text(
                    text = supervisorViewModel.errorMessage.value,
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color(0xFFFF0000)
                    ),
                    modifier = Modifier.padding(20.dp).align(Alignment.CenterStart)
                )
            }
        }
        Spacer(modifier = Modifier.height(70.dp))
    }
}

@Composable
fun NameLabelTextField(nameLabel: String) {
    Text(
        text = nameLabel,
        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W500),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp, top = 10.dp),
        textAlign = TextAlign.Start
    )
}

@Composable
fun ProgressIndicator() {
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

    NameLabelTextField("Temperatura")
    TextFieldComponent(
        placeHolderText = "Temperatura",
        value = temperature,
        isTextFieldEnable = false,
        onTextFieldChanged = {
            supervisorViewModel.updateVitalSigns(it, heartRate, bloodOxygen)
        })
    NameLabelTextField("Frecuencia Cardiaca")
    TextFieldComponent(
        placeHolderText = "Frecuencia Cardiaca",
        value = heartRate,
        isTextFieldEnable = false,
        onTextFieldChanged = {
            supervisorViewModel.updateVitalSigns(temperature, it, bloodOxygen)
        })
    NameLabelTextField("Oxigenacion Sanguinea")
    TextFieldComponent(
        placeHolderText = "Oxigenacion Sanguinea",
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
    val options = listOf("Femenino", "Masculino", "Otro")
    val role: String by supervisorViewModel.gender.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 7.dp), horizontalArrangement = Arrangement.Center
    ) {
        options.forEach { option ->
            Row {
                RadioButton(
                    selected = role == option,
                    onClick = { supervisorViewModel.setGender(option) }
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