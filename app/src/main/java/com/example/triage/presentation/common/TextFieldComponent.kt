package com.example.triage.presentation.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.traigecol.R
import com.example.triage.utils.StringResources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldComponent(
    modifier: Modifier = Modifier,
    labelTitle: String,
    helpText: String,
    value: String,
    keypad: Boolean = false,
    onTextFieldChanged: (String) -> Unit
) {

    val regexText = remember { Regex("^(?!.*\\s{2,})(?!^\\s)[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]*$") }
    val regex = remember { Regex("^(?!0\\d)(?!.*\\.\\d*\\.)(\\d*\\.?\\d?)$") }
    val regexInt = remember { Regex("^(?!0)[0-9]*$") }

    var color by remember { mutableStateOf(Color.LightGray) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextField(
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged { color = if (it.isFocused) Color(0xFF1A80E5) else Color.LightGray }
                .border(
                    width = if (color == Color.LightGray) 0.7.dp else 1.2.dp,
                    color = color,
                    shape = MaterialTheme.shapes.small
                ),
            value = value,
            onValueChange = {
                if(keypad){
                    if(labelTitle == StringResources.TEMPERATURE && it.matches(regex))
                        onTextFieldChanged(it)
                    else if(labelTitle != StringResources.TEMPERATURE && it.matches(regexInt))
                        onTextFieldChanged(it)
                }else if(it.matches(regexText)){
                    onTextFieldChanged(it)
                }
            },
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.W400
            ),
            placeholder = {
                Text(
                    text = helpText,
                    color = colorResource(id = R.color.placeholder),
                    fontWeight = FontWeight.W300
                )
            },
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType =
                if (keypad) KeyboardType.Decimal
                else KeyboardType.Text,
                imeAction =
                if (labelTitle == StringResources.BLOOD_OXYGEN || labelTitle == StringResources.ID_NUMBER) ImeAction.Done
                else ImeAction.Next
            ),
            enabled = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    placeHolderText: String,
    value: String,
    onTextFieldChanged: (String) -> Unit
) {
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    var color by remember { mutableStateOf(Color.LightGray) }

    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = value,
            onValueChange = { onTextFieldChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { color = if (it.isFocused) Color(0xFF1A80E5) else Color.LightGray }
                .border(
                    width = if (color == Color.LightGray) 0.7.dp else 1.2.dp,
                    color = color,
                    shape = MaterialTheme.shapes.small
                ),
            singleLine = true,
            textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconToggleButton(
                    checked = passwordVisibility,
                    onCheckedChange = { passwordVisibility = it }) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(37.dp)
                    )
                }
            },
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    text = placeHolderText,
                    color = colorResource(id = R.color.placeholder),
                    fontWeight = FontWeight.W300
                )
            },
            enabled = true
        )
    }
}