package com.example.triage.presentation.supervisor.addPatient.components

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.traigecol.R
import com.example.triage.presentation.common.DateOfBirthInput
import com.example.triage.presentation.common.LabelTextField
import com.example.triage.presentation.common.TextFieldComponent
import com.example.triage.presentation.common.utils.yearsBetween
import com.example.triage.presentation.supervisor.addPatient.PatientData
import com.example.triage.presentation.supervisor.addPatient.PatientViewModel
import com.example.triage.utils.StringResources
import java.time.Instant
import java.time.ZoneId

@Composable
fun BasicData(patientViewModel: PatientViewModel, modifier: Modifier = Modifier) {
    val birthDialogShown by patientViewModel.birthDialogShown.collectAsState()
    val patient by patientViewModel.patient.collectAsState()

    if (birthDialogShown) {
        DateOfBirthInput(title = "Seleccione la fecha de nacimiento", {
            patientViewModel.updatePatientData(
                (yearsBetween(Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC"))
                    .toLocalDate())), PatientData.AGE
            )
            patientViewModel.setBirthDialogShown(false)
        }) {
            patientViewModel.setBirthDialogShown(false)
        }
    }

    Column(modifier = modifier.padding(start = 20.dp, end = 20.dp)) {

        Text(
            text = "Datos básicos",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            textAlign = TextAlign.Start,
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Medium)
        )

        LabelTextField(nameLabel = StringResources.NAME)
        TextFieldComponent(
            modifier = Modifier.padding(bottom = 14.dp),
            labelTitle = StringResources.NAME,
            helpText = "Ingrese el nombre",
            value = patient.name,
            onTextFieldChanged = {
                patientViewModel.updatePatientData(it, PatientData.NAME)
            })
        LabelTextField(nameLabel = StringResources.LAST_NAME)
        TextFieldComponent(
            modifier = Modifier.padding(bottom = 14.dp),
            labelTitle = StringResources.LAST_NAME,
            helpText = "Ingrese el apellido",
            value = patient.lastname,
            onTextFieldChanged = {
                patientViewModel.updatePatientData(it, PatientData.LASTNAME)
            })

        LabelTextField(nameLabel = "Tipo documento")
        DropDownForIdType {
            patientViewModel.updatePatientData(it, PatientData.ID_TYPE)
        }

        LabelTextField(nameLabel = StringResources.ID_NUMBER)
        TextFieldComponent(
            modifier = Modifier.padding(bottom = 14.dp),
            labelTitle = StringResources.ID_NUMBER,
            helpText = "Ingrese la identificacion",
            value = patient.idNumber,
            keypad = true,
            onTextFieldChanged = {
                if (it.length <= 15) patientViewModel.updatePatientData(it, PatientData.ID_NUMBER)
            })

        LabelTextField(nameLabel = StringResources.AGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AgeComponent(Modifier.fillMaxWidth()
                .padding(start = 7.dp, top = 8.dp, bottom = 8.dp), patient.age, "Ingrese fecha nacimiento") {
                patientViewModel.setBirthDialogShown(true)
            }
        } else {
            Toast.makeText(
                LocalContext.current,
                "Ingrese la edad del paciente manualmente",
                Toast.LENGTH_LONG
            ).show()
            TextFieldComponent(
                modifier = Modifier.padding(bottom = 14.dp),
                labelTitle = StringResources.AGE,
                helpText = "Ingrese la edad",
                value = patient.age,
                onTextFieldChanged = {
                    patientViewModel.updatePatientData(it, PatientData.AGE)
                })
        }
    }
}

@Composable
fun AgeComponent(
    modifier: Modifier = Modifier,
    value: String,
    helpText: String,
    onClick: () -> Unit
) {
    var color by remember { mutableStateOf(Color.LightGray) }

    TextButton(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                color = if (it.isFocused) Color(0xFF1A80E5) else Color.LightGray
            }
            .padding(bottom = 14.dp),
        border = BorderStroke(
            width = if (color == Color.LightGray) 0.7.dp else 1.2.dp,
            color = color,
        ),
        shape = MaterialTheme.shapes.small,
        colors = ButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        )
    ) {
        Text(
            text = value.ifBlank { helpText },
            style = if (value.isBlank()) {
                TextStyle(
                    color = colorResource(id = R.color.placeholder),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W300
                )
            } else {
                TextStyle(
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W400
                )
            },
            modifier = modifier,
            textAlign = TextAlign.Start
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownForIdType(onValueChange: (String) -> Unit) {
    val idType = listOf("RCN", "TI", "CC", "CE", "PAS", "PEP", "PPT")

    var color by remember { mutableStateOf(Color.LightGray) }
    var selectedOption by remember { mutableStateOf(idType[0]) }
    var expanded by remember { mutableStateOf(false) }

    Surface(contentColor = Color.White) {
        ExposedDropdownMenuBox(
            modifier = Modifier.background(Color.White),
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                modifier = Modifier
                    .onFocusChanged {
                        color = if (it.isFocused) Color(0xFF1A80E5) else Color.LightGray
                    }
                    .menuAnchor()
                    .padding(bottom = 14.dp)
                    .border(
                        width = if (color == Color.LightGray) 0.7.dp else 1.2.dp,
                        color = color,
                        shape = MaterialTheme.shapes.small
                    ),
                value = selectedOption,
                onValueChange = { },
                readOnly = true,
                label = { Text("Selecciona el tipo de documento") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                shape = MaterialTheme.shapes.small,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W400
                )
            )

            ExposedDropdownMenu(
                containerColor = Color.White,
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                idType.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W400
                            )
                        },
                        onClick = {
                            onValueChange(option)
                            selectedOption = option
                            expanded = false
                        }
                    )
                }
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
        StringResources.FEMALE_TEXT,
        StringResources.MALE_TEXT
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