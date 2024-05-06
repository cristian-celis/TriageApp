package com.example.triagecol.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.triagecol.MainViewModel
import com.example.triagecol.presentation.admin.AdminViewModel
import com.example.triagecol.domain.UserPage
import com.example.triagecol.presentation.SplashScreen
import com.example.triagecol.presentation.admin.AdminMainScreen
import com.example.triagecol.presentation.admin.details.DetailState
import com.example.triagecol.presentation.admin.details.DetailCard
import com.example.triagecol.presentation.admin.details.DetailCardViewModel
import com.example.triagecol.presentation.doctor.DoctorScreen
import com.example.triagecol.presentation.doctor.DoctorViewModel
import com.example.triagecol.presentation.login.LoginScreen
import com.example.triagecol.presentation.login.LoginViewModel
import com.example.triagecol.presentation.supervisor.addPatient.PatientScreen
import com.example.triagecol.presentation.supervisor.addPatient.PatientViewModel
import com.example.triagecol.presentation.supervisor.addSymptoms.SymptomsViewModel
import com.example.triagecol.presentation.supervisor.addSymptoms.SymptomsScreen
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
    val doctorViewModel: DoctorViewModel = hiltViewModel()
    val symptomsViewModel: SymptomsViewModel = hiltViewModel()

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.SplashScreen.route)
    {
        composable(route = AppScreens.LoginScreen.route) {
            Log.d(Constants.TAG, "Login screen")
            if (mainViewModel.currentScreen.value.route != AppScreens.LoginScreen.route)
                mainViewModel.writeSaveLogin(UserPage.LOGIN, AppScreens.LoginScreen)
            LoginScreen(navController, loginViewModel)
        }
        composable(route = AppScreens.SupervisorScreen.route) {
            Log.d(Constants.TAG, "Supervisor screen")
            if (mainViewModel.currentScreen.value.route != AppScreens.SupervisorScreen.route)
                mainViewModel.writeSaveLogin(UserPage.SUPERVISOR, AppScreens.SupervisorScreen)
            mainSupervisorViewModel.updateUserData(loginViewModel.userData.value)
            if(!mainSupervisorViewModel.fetchingData.value) mainSupervisorViewModel.getPatientList()
            MainSupervisorScreen(navController, mainSupervisorViewModel)
        }
        composable(route = AppScreens.PatientScreen.route){
            Log.d(Constants.TAG, "Patient Screen")
            PatientScreen(navController = navController, patientViewModel = patientViewModel)
        }
        composable(route = AppScreens.DoctorScreen.route) {
            Log.d(Constants.TAG, "Doctor screen")
            if (mainViewModel.currentScreen.value.route != AppScreens.DoctorScreen.route)
                mainViewModel.writeSaveLogin(UserPage.DOCTOR, AppScreens.DoctorScreen)
            doctorViewModel.setDoctorData(loginViewModel.userData.value)
            DoctorScreen(navController, doctorViewModel)
        }
        composable(route = AppScreens.SymptomsScreen.route) {
            Log.d(Constants.TAG, "Vital Signs Screen")
            symptomsViewModel.setIdPatient(patientViewModel.id.value)
            SymptomsScreen(navController = navController, symptomsViewModel = symptomsViewModel)
        }
        composable(route = AppScreens.AdminScreen.route) {
            Log.d(Constants.TAG, "Admin screen")
            if (mainViewModel.currentScreen.value.route != AppScreens.AdminScreen.route) {
                mainViewModel.writeSaveLogin(UserPage.ADMIN, AppScreens.AdminScreen)
            }
            if (detailCardViewModel.detailState.value == DetailState.ENTERING) {
                Log.d(Constants.TAG, "Actualizando datos.")
                adminViewModel.getUserList()
                detailCardViewModel.setDetailState(DetailState.DONE)
            }
            AdminMainScreen(navController, adminViewModel)
        }
        composable(route = AppScreens.DetailCard.route) {
            Log.d(Constants.TAG, "Detail screen")
            if (!adminViewModel.clickOnAddButton.value)
                detailCardViewModel.setUserData(adminViewModel.userData.value)
            else
                detailCardViewModel.resetData()
            DetailCard(navController, detailCardViewModel)
        }
        composable(route = AppScreens.SplashScreen.route) {
            Log.d(Constants.TAG, "SplashScreen")
            SplashScreen(navController)
        }
    }
}