package com.example.triagecol.presentation.common

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.traigecol.R
import com.example.triagecol.presentation.admin.details.DetailCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldComponent(
    placeHolderText: String,
    value: String,
    isTextFieldEnable: Boolean,
    onTextFieldChanged: (String) -> Unit
) {

    Box(
        modifier = Modifier
            .padding(bottom = 10.dp).fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = { onTextFieldChanged(it) },
            placeholder = {
                Text(
                    text = placeHolderText, style = MaterialTheme.typography.bodyLarge,
                    color = colorResource(id = R.color.placeholder)
                )
            },
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFE8EDF2),
                textColor = Color(0xFF383838),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            enabled = !isTextFieldEnable
        )
    }
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
            singleLine = true,
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image =
                    if (!passwordVisibility) R.drawable.visibility else R.drawable.visibility_off

                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        painter = painterResource(id = image),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            },
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFE8EDF2),
                textColor = Color(0xFF383838),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    text = placeHolderText, style = MaterialTheme.typography.bodyLarge,
                    color = colorResource(id = R.color.placeholder)
                )
            },
            enabled = isEnable
        )
    }
}