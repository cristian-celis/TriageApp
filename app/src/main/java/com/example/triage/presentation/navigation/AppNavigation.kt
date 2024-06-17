package com.example.triage.presentation.navigation

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.triage.MainViewModel
import com.example.triage.presentation.admin.AdminViewModel
import com.example.triage.domain.UserPage
import com.example.triage.presentation.SplashScreen
import com.example.triage.presentation.admin.AdminMainScreen
import com.example.triage.presentation.admin.detailsScreen.DetailCard
import com.example.triage.presentation.admin.detailsScreen.DetailCardViewModel
import com.example.triage.presentation.doctor.DoctorScreen
import com.example.triage.presentation.doctor.DoctorViewModel
import com.example.triage.presentation.login.LoginScreen
import com.example.triage.presentation.login.LoginViewModel
import com.example.triage.presentation.supervisor.addPatient.PatientScreen
import com.example.triage.presentation.supervisor.addPatient.PatientViewModel
import com.example.triage.presentation.supervisor.addSymptoms.SymptomsScreen
import com.example.triage.presentation.supervisor.addSymptoms.SymptomsViewModel
import com.example.triage.presentation.supervisor.main.MainSupervisorScreen
import com.example.triage.presentation.supervisor.main.MainSupervisorViewModel
import com.example.triage.utils.Errors

@Composable
fun AppNavigation(mainViewModel: MainViewModel) {

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
            val loginViewModel: LoginViewModel = hiltViewModel()
            Log.d(Errors.TAG, "Login screen")
            SetPortraitOrientationOnly()
            mainViewModel.writeSaveLogin(UserPage.LOGIN, AppScreens.LoginScreen)
            LoginScreen(navController, loginViewModel)
        }
        composable(
            route = AppScreens.DoctorScreen.route,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) {
            Log.d(Errors.TAG, "Doctor screen")
            val doctorViewModel: DoctorViewModel = hiltViewModel()
            val id = it.arguments?.getString("id") ?: ""
            DoctorScreen(
                navController = navController,
                doctorViewModel = doctorViewModel, id
            )
        }
        composable(
            route = AppScreens.MainSupervisorScreen.route,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) {
            Log.d(Errors.TAG, "Main Supervisor screen")
            val mainSupervisorViewModel: MainSupervisorViewModel = hiltViewModel()
            EnableScreenOrientation()
            val id = it.arguments?.getString("id") ?: ""
            MainSupervisorScreen(
                navController = navController,
                mainSupervisorViewModel = mainSupervisorViewModel,
                id
            )
        }
        composable(route = AppScreens.AddPatientScreen.route) {
            val patientViewModel: PatientViewModel = hiltViewModel()
            Log.d(Errors.TAG, "Patient Screen")
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
            val symptomsViewModel: SymptomsViewModel = hiltViewModel()
            Log.d(Errors.TAG, "Vital Signs Screen")
            val idPatient = it.arguments?.getString("id") ?: ""
            val sexPatient = it.arguments?.getString("sex") ?: "Masculino"
            symptomsViewModel.setInitialValues(idPatient, sexPatient)
            SymptomsScreen(navController = navController, symptomsViewModel = symptomsViewModel)
        }

        composable(route = AppScreens.AdminScreen.route) {
            val adminViewModel: AdminViewModel = hiltViewModel()
            Log.d(Errors.TAG, "Admin screen")
            mainViewModel.writeSaveLogin(UserPage.ADMIN, AppScreens.AdminScreen)
            AdminMainScreen(navController, adminViewModel)
        }
        composable(
            route = AppScreens.DetailCard.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                },
                navArgument("editTitle") {
                    type = NavType.BoolType
                })
        ) {
            val detailCardViewModel: DetailCardViewModel = hiltViewModel()
            Log.d(Errors.TAG, "Detail screen")
            val id = it.arguments?.getString("id") ?: ""
            val editTitle = it.arguments?.getBoolean("editTitle") ?: false
            DetailCard(navController, detailCardViewModel, editTitle, id)
        }
        composable(route = AppScreens.SplashScreen.route) {
            Log.d(Errors.TAG, "SplashScreen")
            SplashScreen(navController)
        }
    }
}