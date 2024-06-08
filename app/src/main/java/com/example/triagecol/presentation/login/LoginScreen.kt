package com.example.triagecol.presentation.login

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.shadow
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
    val userLoggedIn by loginViewModel.userLoggedIn.collectAsState()
    val error by loginViewModel.error.collectAsState()

    val isValidCredentials by loginViewModel.isValidCredentials.collectAsState()
    val authenticatingCredentials by loginViewModel.authenticatingCredentials.collectAsState()
    val showToastMessage by loginViewModel.showToastMessage.collectAsState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    /*BackHandler(true) {
        (context as? Activity)?.finish()
    }*/

    if (isValidCredentials) {
        loginViewModel.clearError()
        if (userLoggedIn == AppScreens.MainSupervisorScreen) {
            navController.navigate(AppScreens.MainSupervisorScreen.addUserId(loginViewModel.userData.id.toString())) {
                popUpTo(AppScreens.LoginScreen.route) { inclusive = true }
            }
        } else {
            navController.navigate(userLoggedIn.route) {
                popUpTo(AppScreens.LoginScreen.route) { inclusive = true }
            }
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
                    text = "Iniciar Sesión",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                TextFieldComponent(
                    placeHolderText = TextConstants.USERNAME,
                    value = user,
                    isTextFieldEnable = false
                ) { loginViewModel.onLoginChanged(if (it.length < 11) it else user, password) }

                Spacer(modifier = Modifier.size(8.dp))

                PasswordTextField(
                    placeHolderText = TextConstants.PASSWORD,
                    value = password,
                    isEnable = true,
                    onTextFieldChanged = { loginViewModel.onLoginChanged(user, it) })

                LoginButton(
                    modifier = Modifier.padding(vertical = 18.dp),
                    loginEnable = loginEnable,
                    loginViewModel = loginViewModel
                )

                AnimatedVisibility(visible = authenticatingCredentials) {
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