package com.example.triagecol.presentation.navigation

import android.content.pm.ActivityInfo
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.triagecol.MainViewModel
import com.example.triagecol.presentation.admin.AdminViewModel
import com.example.triagecol.domain.UserPage
import com.example.triagecol.presentation.SplashScreen
import com.example.triagecol.presentation.admin.AdminMainScreen
import com.example.triagecol.presentation.admin.details.DetailCard
import com.example.triagecol.presentation.admin.details.DetailCardViewModel
import com.example.triagecol.presentation.doctor.DoctorScreen
import com.example.triagecol.presentation.doctor.DoctorViewModel
import com.example.triagecol.presentation.login.LoginScreen
import com.example.triagecol.presentation.login.LoginViewModel
import com.example.triagecol.presentation.supervisor.addPatient.PatientScreen
import com.example.triagecol.presentation.supervisor.addPatient.PatientViewModel
import com.example.triagecol.presentation.supervisor.addSymptoms.SymptomsScreen
import com.example.triagecol.presentation.supervisor.addSymptoms.SymptomsViewModel
import com.example.triagecol.presentation.supervisor.main.MainSupervisorScreen
import com.example.triagecol.presentation.supervisor.main.MainSupervisorViewModel
import com.example.triagecol.utils.Constants

@Composable
fun AppNavigation(mainViewModel: MainViewModel) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val adminViewModel: AdminViewModel = hiltViewModel()
    val detailCardViewModel: DetailCardViewModel = hiltViewModel()

    val mainSupervisorViewModel: MainSupervisorViewModel = hiltViewModel()
    val patientViewModel: PatientViewModel = hiltViewModel()
    val symptomsViewModel: SymptomsViewModel = hiltViewModel()
    val doctorViewModel: DoctorViewModel = hiltViewModel()

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        enterTransition = { slideInVertically { it / 16 } + fadeIn() },
        exitTransition = { fadeOut(tween(100)) },
        popEnterTransition = { slideInVertically { it / 16 } + fadeIn() },
        popExitTransition = { fadeOut(tween(100)) },
        startDestination = AppScreens.SplashScreen.route
    )
    {
        composable(route = AppScreens.LoginScreen.route) {
            Log.d(Constants.TAG, "Login screen")
            mainViewModel.writeSaveLogin(UserPage.LOGIN, AppScreens.LoginScreen)
            LoginScreen(navController, loginViewModel)
        }
        composable(route = AppScreens.DoctorScreen.route) {
            LaunchedEffect(key1 = true) {
                ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
            }
            Log.d(Constants.TAG, "Doctor screen")
            doctorViewModel.setDoctorData(loginViewModel.userData)
            DoctorScreen(navController, doctorViewModel)
        }
        composable(
            route = AppScreens.MainSupervisorScreen.route,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) {
            val id = it.arguments?.getString("id") ?: ""
            MainSupervisorScreen(
                navController = navController,
                mainSupervisorViewModel = mainSupervisorViewModel,
                id
            )
        }
        composable(route = AppScreens.AddPatientScreen.route) {
            Log.d(Constants.TAG, "Patient Screen")
            PatientScreen(navController = navController, patientViewModel = patientViewModel)
        }
        composable(
            route = AppScreens.AddSymptomsScreen.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                },
                navArgument("sex") {
                    type = NavType.StringType
                })
        ) {
            Log.d(Constants.TAG, "Vital Signs Screen")
            val idPatient = it.arguments?.getString("id") ?: ""
            val sexPatient = it.arguments?.getString("sex") ?: "Masculino"
            if (symptomsViewModel.idPatient.isBlank() && idPatient.isNotBlank()) {
                symptomsViewModel.setInitialValues(idPatient, sexPatient)
            }
            SymptomsScreen(navController = navController, symptomsViewModel = symptomsViewModel)
        }

        composable(route = AppScreens.AdminScreen.route) {
            Log.d(Constants.TAG, "Admin screen")
            mainViewModel.writeSaveLogin(UserPage.ADMIN, AppScreens.AdminScreen)
            AdminMainScreen(navController, adminViewModel)
        }
        composable(
            route = AppScreens.DetailCard.route,
            arguments = listOf(navArgument("editTitle") {
                type = NavType.BoolType
            })
        ) {
            Log.d(Constants.TAG, "Detail screen")
            val editTitle = it.arguments?.getBoolean("editTitle") ?: false
            if (editTitle)
                detailCardViewModel.setUserData(adminViewModel.userData)
            else
                detailCardViewModel.resetData()
            DetailCard(navController, detailCardViewModel, editTitle)
        }
        composable(route = AppScreens.SplashScreen.route) {
            Log.d(Constants.TAG, "probando")
            Log.d(Constants.TAG, "SplashScreen")
            SplashScreen(navController)
        }
    }
}