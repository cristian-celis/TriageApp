package com.example.triage.presentation.login

import android.content.pm.ActivityInfo
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.triage.presentation.common.PasswordTextField
import com.example.triage.presentation.common.TextFieldComponent
import com.example.triage.presentation.navigation.AppScreens
import com.example.triage.utils.StringResources

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel) {

    val user: String by loginViewModel.user.collectAsState()
    val password: String by loginViewModel.password.collectAsState()
    val loginEnable: Boolean by loginViewModel.loginEnable.observeAsState(initial = false)
    val userLoggedIn by loginViewModel.userLoggedIn.collectAsState()
    val error by loginViewModel.error.collectAsState()

    val isValidCredentials by loginViewModel.isValidCredentials.collectAsState()
    val authenticatingCredentials by loginViewModel.authenticatingCredentials.collectAsState()
    val showToastMessage by loginViewModel.showToastMessage.collectAsState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    if (isValidCredentials) {
        loginViewModel.clearError()
        navController.navigate(
            when (userLoggedIn) {
                is AppScreens.AdminScreen -> {
                    AppScreens.AdminScreen.route
                }
                is AppScreens.DoctorScreen -> {
                    AppScreens.DoctorScreen.addUserId(loginViewModel.userData.id.toString())
                }
                is AppScreens.MainSupervisorScreen -> {
                    AppScreens.MainSupervisorScreen.addUserId(loginViewModel.userData.id.toString())
                }
                else -> {
                    AppScreens.LoginScreen.route
                }
            }
        ) {
            popUpTo(AppScreens.LoginScreen.route) { inclusive = true }
        }
        loginViewModel.setValidCredentials(false)
    }

    if (showToastMessage) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        loginViewModel.setShowToastMessage()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_app),
                contentDescription = null,
                modifier = Modifier
                    .size(170.dp)
                    .padding(top = 20.dp, bottom = 10.dp)
            )
        }

        Surface(
            modifier = Modifier.padding(15.dp),
            shape = RoundedCornerShape(5.dp),
            shadowElevation = 5.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(start = 8.dp, end = 8.dp, top = 20.dp, bottom = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Inicio de Sesión",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                TextFieldComponent(
                    modifier = Modifier.padding(bottom = 14.dp),
                    labelTitle = StringResources.USERNAME,
                    helpText = "Usuario",
                    value = user
                ) { loginViewModel.onLoginChanged(if (it.length < 11) it else user, password) }

                Spacer(modifier = Modifier.size(8.dp))

                PasswordTextField(
                    placeHolderText = StringResources.PASSWORD,
                    value = password,
                    onTextFieldChanged = { loginViewModel.onLoginChanged(user, it) })

                LoginButton(
                    modifier = Modifier.padding(vertical = 18.dp),
                    loginEnable = loginEnable,
                    loginViewModel = loginViewModel
                )

                AnimatedVisibility(visible = authenticatingCredentials) {
                    Text(
                        text = StringResources.VALIDATING,
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

    }
}

@Composable
fun LoginButton(
    modifier: Modifier = Modifier,
    loginEnable: Boolean,
    loginViewModel: LoginViewModel,
) {

    val authenticatingCredentials by loginViewModel.authenticatingCredentials.collectAsState()
    val focusManager = LocalFocusManager.current

    Button(
        onClick = {
            focusManager.clearFocus()
            loginViewModel.login()
            loginViewModel.onLoginChanged("", "")
        },
        modifier = modifier
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
            text = StringResources.LOGIN,
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