package com.example.triagecol.presentation.admin.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.traigecol.R
import com.example.triagecol.presentation.common.PasswordTextField
import com.example.triagecol.presentation.common.TextFieldComponent

@Composable
fun TextBoxesForData(modifier: Modifier = Modifier, detailCardViewModel: DetailCardViewModel) {
    val name: String by detailCardViewModel.name.collectAsState()
    val lastname: String by detailCardViewModel.lastname.collectAsState()
    val idNumber: String by detailCardViewModel.idNumber.collectAsState()
    val password: String by detailCardViewModel.password.collectAsState()
    val phoneNumber: String by detailCardViewModel.phoneNumber.collectAsState()

    val isTextFieldEnable: Boolean by detailCardViewModel.isApiRequestPending.collectAsState()

    Column(modifier = modifier.padding(start = 16.dp, end = 16.dp)) {
        NameLabelTextField(modifier, "Nombre")
        TextFieldComponent(
            placeHolderText = "Nombre", value = name, isTextFieldEnable = isTextFieldEnable,
            onTextFieldChanged = {
                detailCardViewModel.onUserDataChanged(
                    idNumber, it, lastname, password, phoneNumber
                )
            })

        NameLabelTextField(modifier, "Apellido")
        TextFieldComponent(
            placeHolderText = "Apellido", value = lastname, isTextFieldEnable = isTextFieldEnable,
            onTextFieldChanged = {
                detailCardViewModel.onUserDataChanged(
                    idNumber, name, it, password, phoneNumber
                )
            })

        NameLabelTextField(modifier, "Numero de Telefono")
        TextFieldComponent(
            placeHolderText = "Numero de Telefono",
            value = phoneNumber,
            isTextFieldEnable = isTextFieldEnable,
            onTextFieldChanged = {
                detailCardViewModel.onUserDataChanged(
                    idNumber, name, lastname, password, it
                )
            })

        NameLabelTextField(modifier, "Numero de Identificacion")
        TextFieldComponent(
            placeHolderText = "Numero de Identificacion",
            value = idNumber,
            isTextFieldEnable = isTextFieldEnable,
            onTextFieldChanged = {
                detailCardViewModel.onUserDataChanged(
                    it, name, lastname, password, phoneNumber
                )
            })

        NameLabelTextField(modifier, "Usuario")
        Text(
            text = idNumber,
            style = MaterialTheme.typography.bodyLarge,
            color = colorResource(id = R.color.placeholder),
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        )

        NameLabelTextField(modifier, "Contraseña")
        PasswordTextField(
            placeHolderText = "Contraseña",
            value = password,
            isEnable = !isTextFieldEnable,
            onTextFieldChanged = {
                detailCardViewModel.onUserDataChanged(
                    idNumber, name, lastname, it, phoneNumber
                )
            })

        ToggleOptionComponent(detailCardViewModel, modifier, isTextFieldEnable)
    }
}

@Composable
fun NameLabelTextField(modifier: Modifier = Modifier, nameLabel: String) {
    Text(
        text = nameLabel,
        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W500),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp, top = 10.dp),
        textAlign = TextAlign.Start
    )
}

@Composable
fun ToggleOptionComponent(
    detailCardViewModel: DetailCardViewModel,
    modifier: Modifier = Modifier,
    isTextFieldEnable: Boolean
) {
    val options = listOf("Doctor", "Supervisor")
    val role: String by detailCardViewModel.role.collectAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 7.dp), horizontalArrangement = Arrangement.Center
    ) {
        options.forEach { option ->
            Row {
                RadioButton(
                    selected = role == option,
                    onClick = { detailCardViewModel.setRole(option) }
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