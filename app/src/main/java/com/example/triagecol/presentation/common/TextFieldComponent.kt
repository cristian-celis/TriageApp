package com.example.triagecol.presentation.common

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.traigecol.R
import com.example.triagecol.presentation.admin.details.DetailCardViewModel
import com.example.triagecol.utils.SupervisorConstants
import com.example.triagecol.utils.TextConstants
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldComponent(
    placeHolderText: String,
    value: String,
    isTextFieldEnable: Boolean,
    onTextFieldChanged: (String) -> Unit
) {

    val regex = "^[a-zA-Z0-9]*$".toRegex()

    Box(
        modifier = Modifier
            .padding(bottom = 14.dp)
            .fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = { if(regex.matches(it) && it.length < 40) onTextFieldChanged(it) },
            textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
            label = { Text(
                text = placeHolderText, color = colorResource(id = R.color.body)
            ) },
            shape = MaterialTheme.shapes.extraSmall,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFE8EDF2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = keyboardSelected(placeHolderText),
            enabled = !isTextFieldEnable
        )
    }
}

private fun keyboardSelected(boxName: String): KeyboardOptions{
    return if (boxName.equals(SupervisorConstants.ID_NUMBER, ignoreCase = true)
        || boxName.equals(TextConstants.PHONE_NUMBER, ignoreCase = true)
        || boxName.equals(SupervisorConstants.AGE, ignoreCase = true)
        || boxName.equals(SupervisorConstants.TEMPERATURE, ignoreCase = true)
        || boxName.equals(SupervisorConstants.HEART_RATE, ignoreCase = true))
        KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
    else if(boxName.equals(SupervisorConstants.BLOOD_OXYGEN, ignoreCase = true))
        KeyboardOptions(keyboardType = KeyboardType.Number)
    else KeyboardOptions(imeAction = ImeAction.Next)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    placeHolderText: String,
    value: String, isEnable: Boolean,
    onTextFieldChanged: (String) -> Unit
) {
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = value,
            onValueChange = { onTextFieldChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            //isError = true,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconToggleButton(checked = passwordVisibility, onCheckedChange = {passwordVisibility = it}) {
                    Icon(imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(37.dp))
                }
            },
            shape = MaterialTheme.shapes.extraSmall,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFE8EDF2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            label = { Text(
                text = placeHolderText, color = colorResource(id = R.color.body)
            ) },
            enabled = isEnable
        )
    }
}