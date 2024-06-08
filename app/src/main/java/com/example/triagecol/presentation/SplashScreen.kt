package com.example.triagecol.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Constraints
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.traigecol.R
import com.example.triagecol.MainViewModel
import com.example.triagecol.presentation.doctor.DoctorViewModel
import com.example.triagecol.presentation.navigation.AppScreens
import com.example.triagecol.utils.Constants
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    val mainViewModel: MainViewModel = hiltViewModel()

    LaunchedEffect(key1 = true) {
        delay(700)
        navController.popBackStack()

        val nextScreen = when (mainViewModel.savedLogin.value) {
            "ADMIN" ->  AppScreens.AdminScreen
            "LOGIN" ->  AppScreens.LoginScreen
            else -> {AppScreens.LoginScreen}
        }

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