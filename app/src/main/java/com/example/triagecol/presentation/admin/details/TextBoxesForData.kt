package com.example.triagecol.presentation.admin.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.traigecol.R
import com.example.triagecol.presentation.common.LabelTextField
import com.example.triagecol.presentation.common.PasswordTextField
import com.example.triagecol.presentation.common.TextFieldComponent
import com.example.triagecol.utils.Constants
import com.example.triagecol.utils.SupervisorConstants
import com.example.triagecol.utils.TextConstants

@Composable
fun TextBoxesForData(modifier: Modifier = Modifier, detailCardViewModel: DetailCardViewModel) {
    val name: String by detailCardViewModel.name.collectAsState()
    val lastname: String by detailCardViewModel.lastname.collectAsState()
    val idNumber: String by detailCardViewModel.idNumber.collectAsState()
    val password: String by detailCardViewModel.password.collectAsState()
    val phoneNumber: String by detailCardViewModel.phoneNumber.collectAsState()

    Column(modifier = modifier.padding(start = 20.dp, end = 20.dp)) {
        LabelTextField(modifier, SupervisorConstants.NAME)
        TextFieldComponent(
            modifier = Modifier.padding(bottom = 14.dp),
            labelTitle = SupervisorConstants.NAME, helpText = "Ingrese el nombre", value = name,
            onTextFieldChanged = {
                detailCardViewModel.onUserDataChanged(
                    idNumber, it, lastname, password, phoneNumber
                )
            })

        LabelTextField(modifier, SupervisorConstants.LAST_NAME)
        TextFieldComponent(
            modifier = Modifier.padding(bottom = 14.dp),
            labelTitle = SupervisorConstants.LAST_NAME, helpText = "Ingrese el apellido", value = lastname,
            onTextFieldChanged = {
                detailCardViewModel.onUserDataChanged(
                    idNumber, name, it, password, phoneNumber
                )
            })

        LabelTextField(modifier, TextConstants.PHONE_NUMBER)
        TextFieldComponent(
            modifier = Modifier.padding(bottom = 14.dp),
            labelTitle = TextConstants.PHONE_NUMBER,helpText = "Ingrese el telefono",
            value = phoneNumber,
            onTextFieldChanged = {
                detailCardViewModel.onUserDataChanged(
                    idNumber, name, lastname, password, it
                )
            })

        LabelTextField(modifier, SupervisorConstants.ID_NUMBER)
        TextFieldComponent(
            modifier = Modifier.padding(bottom = 14.dp),
            labelTitle = SupervisorConstants.ID_NUMBER,helpText = "Ingrese la identificacion",
            value = idNumber,
            onTextFieldChanged = {
                detailCardViewModel.onUserDataChanged(
                    it, name, lastname, password, phoneNumber
                )
            })

        LabelTextField(modifier, TextConstants.USERNAME)
        Text(
            text = idNumber,
            style = MaterialTheme.typography.bodyLarge,
            color = colorResource(id = R.color.placeholder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        LabelTextField(modifier, TextConstants.PASSWORD)
        PasswordTextField(
            placeHolderText = TextConstants.PASSWORD,
            value = password,
            onTextFieldChanged = {
                detailCardViewModel.onUserDataChanged(
                    idNumber, name, lastname, it, phoneNumber
                )
            })

        LabelTextField(modifier.padding(top = 7.dp), TextConstants.ACCOUNT_TYPE)
        ToggleOptionComponent(detailCardViewModel, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 4.dp))
    }
}

@Composable
fun ToggleOptionComponent(
    detailCardViewModel: DetailCardViewModel,
    modifier: Modifier = Modifier
) {
    val options = listOf(Constants.DOCTOR, Constants.SUPERVISOR)
    val role: String by detailCardViewModel.role.collectAsState()

    Row(
        modifier = modifier, horizontalArrangement = Arrangement.Center
    ) {
        options.forEach { option ->
            Row {
                RadioButton(
                    selected = role == option,
                    onClick = { detailCardViewModel.setRole(option) },
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