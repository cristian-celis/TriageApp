package com.example.triagecol.presentation.admin.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triagecol.presentation.common.PasswordTextField
import com.example.triagecol.presentation.common.TextFieldComponent

@Composable
fun TextBoxesForData(modifier: Modifier = Modifier, detailCardViewModel: DetailCardViewModel) {
    val name: String by detailCardViewModel.name.observeAsState(initial = "")
    val lastname: String by detailCardViewModel.lastname.observeAsState(initial = "")
    val password: String by detailCardViewModel.password.observeAsState(initial = "")
    val idNumber: String by detailCardViewModel.idNumber.observeAsState(initial = "")

    val isTextFieldEnable: Boolean = detailCardViewModel.isAddingOrEditingUsers.value

    Column(modifier = modifier.padding(start = 16.dp, end = 16.dp)) {
        NameLabelTextField(modifier, "Nombre")
        TextFieldComponent(
            placeHolderText = "Nombre", value = name, isEnable = !isTextFieldEnable,
            onTextFieldChanged = {
                detailCardViewModel.onUserDataChanged(
                    it, lastname, idNumber, password
                )
            })

        NameLabelTextField(modifier, "Apellido")
        TextFieldComponent(
            placeHolderText = "Apellido", value = lastname,isEnable = !isTextFieldEnable,
            onTextFieldChanged = {
                detailCardViewModel.onUserDataChanged(
                    name, it, idNumber, password
                )
            })

        NameLabelTextField(modifier, "Numero de identificacion")
        TextFieldComponent(
            placeHolderText = "Numero de identificacion", value = idNumber,isEnable = !isTextFieldEnable,
            onTextFieldChanged = {
                detailCardViewModel.onUserDataChanged(
                    name, lastname, it, password
                )
            })

        NameLabelTextField(modifier, "Usuario")
        TextFieldComponent(
            placeHolderText = "Usuario", value = idNumber,isEnable = !isTextFieldEnable,
            onTextFieldChanged = {
                detailCardViewModel.onUserDataChanged(
                    name, lastname, it, password
                )
            })

        NameLabelTextField(modifier, "Contraseña")
        PasswordTextField(placeHolderText = "Contraseña", value = password,isEnable = !isTextFieldEnable, onTextFieldChanged = {
            detailCardViewModel.onUserDataChanged(
                name, lastname, idNumber, it
            )
        })

        ToggleOptionComponent(detailCardViewModel, modifier, !isTextFieldEnable)
    }
}

@Composable
fun NameLabelTextField(modifier: Modifier = Modifier, nameLabel: String) {
    Text(
        text = nameLabel,
        style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.W500),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp, top = 10.dp),
        textAlign = TextAlign.Start
    )
}

@Composable
fun ToggleOptionComponent(detailCardViewModel: DetailCardViewModel, modifier: Modifier = Modifier, isEnable: Boolean) {
    val typePerson by detailCardViewModel.typePerson.collectAsState()
    val options = listOf("medico", "asistente")

    Row(modifier = modifier.fillMaxWidth().padding(top = 20.dp, bottom = 7.dp), horizontalArrangement = Arrangement.Center) {
        options.forEach { option ->
            Row {
                RadioButton(
                    selected = typePerson == option,
                    onClick = { detailCardViewModel.setTypePerson(option) },
                    enabled = isEnable
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