package com.example.triagecol.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.traigecol.R
import com.example.triagecol.MainViewModel
import com.example.triagecol.presentation.navigation.AppScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, mainViewModel: MainViewModel) {

    LaunchedEffect(key1 = true) {
        delay(2000)
        navController.popBackStack()

        var nextScreen: AppScreens = AppScreens.LoginScreen

        when (mainViewModel.savedLogin.value) {
            "ADMIN" -> nextScreen = AppScreens.AdminScreen
            "LOGIN" -> nextScreen = AppScreens.LoginScreen
            "ASSISTANT" -> nextScreen = AppScreens.UserScreen
            "DOCTOR" -> nextScreen = AppScreens.AdminScreen
        }

        mainViewModel.setCurrentScreen(nextScreen)

        navController.navigate(nextScreen.route)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.triage_logo),
            contentDescription = "Logo App"
        )
    }
}