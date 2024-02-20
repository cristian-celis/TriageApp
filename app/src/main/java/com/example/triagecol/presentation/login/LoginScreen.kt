package com.example.triagecol.presentation.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.traigecol.R
import com.example.triagecol.presentation.common.PasswordTextField
import com.example.triagecol.presentation.common.TextFieldComponent
import com.example.triagecol.presentation.navigation.AppScreens

@Composable
fun LoginScreen(navController: NavController) {

    val loginViewModel: LoginViewModel = hiltViewModel()

    val user: String by loginViewModel.user.observeAsState(initial = "")
    val password: String by loginViewModel.password.observeAsState(initial = "")
    val loginEnable: Boolean by loginViewModel.loginEnable.observeAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(140.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_app),
                contentDescription = null,
                modifier = Modifier.size(140.dp)
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = "Bienvenido", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp)
            )
        }

        Spacer(modifier = Modifier.size(14.dp))

        TextFieldComponent(placeHolderText = "Usuario", value = user, isTextFieldEnable = false) { loginViewModel.onLoginChanged(it, password) }

        Spacer(modifier = Modifier.size(8.dp))

        PasswordTextField(placeHolderText = "Contraseña", value = password, isEnable = true,onTextFieldChanged = {loginViewModel.onLoginChanged(user, it)})

        Spacer(modifier = Modifier.size(14.dp))

        LoginButton(loginEnable = loginEnable) { navController.navigate(route = AppScreens.AdminScreen.route) }
    }
}

@Composable
fun LoginButton(loginEnable: Boolean, navigate: () -> Unit) {
    Button(
        onClick = { navigate() },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1A80E5),
            disabledContainerColor = Color(0xFFB8CBFA),
            contentColor = Color.White,
            disabledContentColor = Color.White),
        enabled = loginEnable, shape = MaterialTheme.shapes.medium
    ) {
        Text(text = "Iniciar sesión", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
    }
}