package com.example.triagecol.presentation.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.traigecol.R
import com.example.triagecol.presentation.common.PasswordTextField
import com.example.triagecol.presentation.common.TextFieldComponent
import com.example.triagecol.presentation.navigation.AppScreens
import com.example.triagecol.utils.TextConstants

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel) {

    val user: String by loginViewModel.user.collectAsState()
    val password: String by loginViewModel.password.collectAsState()
    val loginEnable: Boolean by loginViewModel.loginEnable.observeAsState(initial = false)

    val isValidCredentials by loginViewModel.isValidCredentials.collectAsState()
    val authenticatingCredentials by loginViewModel.authenticatingCredentials.collectAsState()
    val showToastMessage by loginViewModel.showToastMessage.collectAsState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    if (isValidCredentials) {
        loginViewModel.clearError()
        navController.navigate(loginViewModel.userLoggedIn.value.route) {
            popUpTo(AppScreens.LoginScreen.route) { inclusive = true }
        }
        loginViewModel.setValidCredentials(false)
    }

    if(showToastMessage){
        Toast.makeText(context, loginViewModel.error.value, Toast.LENGTH_LONG).show()
        loginViewModel.setShowToastMessage()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            },
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
                text = TextConstants.WELCOME,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp)
            )
        }

        Spacer(modifier = Modifier.size(14.dp))

        TextFieldComponent(
            placeHolderText = TextConstants.USERNAME,
            value = user,
            isTextFieldEnable = false
        ) { loginViewModel.onLoginChanged(if(it.length < 11) it else user, password) }

        Spacer(modifier = Modifier.size(8.dp))

        PasswordTextField(
            placeHolderText = TextConstants.PASSWORD,
            value = password,
            isEnable = true,
            onTextFieldChanged = { loginViewModel.onLoginChanged(user, it) })

        Spacer(modifier = Modifier.size(14.dp))

        LoginButton(
            loginEnable = loginEnable,
            loginViewModel = loginViewModel
        )

        Spacer(modifier = Modifier.size(14.dp))

        if (authenticatingCredentials) {
            Text(
                text = TextConstants.VALIDATING,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color(0xFF383838)
                ),
                modifier = Modifier.padding(10.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LoginButton(
    loginEnable: Boolean,
    loginViewModel: LoginViewModel,
) {

    val authenticatingCredentials by loginViewModel.authenticatingCredentials.collectAsState()
    val focusManager = LocalFocusManager.current

    Button(
        onClick = {
            focusManager.clearFocus()
            loginViewModel.login()
            loginViewModel.onLoginChanged("","")
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1A80E5),
            disabledContainerColor = Color(0xFFB8CBFA),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        enabled = loginEnable, shape = MaterialTheme.shapes.medium
    ) {
        if (authenticatingCredentials) ProgressIndicator()
        else Text(
            text = TextConstants.LOGIN,
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