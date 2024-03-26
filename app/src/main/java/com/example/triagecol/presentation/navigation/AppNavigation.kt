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
import com.example.triagecol.presentation.supervisor.SupervisorScreen
import com.example.triagecol.presentation.supervisor.SupervisorViewModel
import com.example.triagecol.presentation.supervisor.SymptomsViewModel
import com.example.triagecol.presentation.supervisor.VitalSignsScreen
import kotlinx.coroutines.supervisorScope

@Composable
fun AppNavigation(mainViewModel: MainViewModel) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val adminViewModel: AdminViewModel = hiltViewModel()
    val detailCardViewModel: DetailCardViewModel = hiltViewModel()
    val supervisorViewModel: SupervisorViewModel = hiltViewModel()
    val doctorViewModel: DoctorViewModel = hiltViewModel()
    val symptomsViewModel: SymptomsViewModel = hiltViewModel()

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.route) {
        composable(route = AppScreens.LoginScreen.route) {
            Log.d("prueba", "Login screen")
            if (mainViewModel.currentScreen.value.route != AppScreens.LoginScreen.route)
                mainViewModel.writeSaveLogin(UserPage.LOGIN, AppScreens.LoginScreen)
            LoginScreen(navController, loginViewModel)
        }
        composable(route = AppScreens.SupervisorScreen.route) {
            Log.d("prueba", "Supervisor screen")
            if (mainViewModel.currentScreen.value.route != AppScreens.SupervisorScreen.route)
                mainViewModel.writeSaveLogin(UserPage.SUPERVISOR, AppScreens.SupervisorScreen)
            supervisorViewModel.setUserData(loginViewModel.userData.value)
            SupervisorScreen(navController, supervisorViewModel)
        }
        composable(route = AppScreens.DoctorScreen.route){
            Log.d("prueba", "Doctor screen")
            if (mainViewModel.currentScreen.value.route != AppScreens.DoctorScreen.route)
                mainViewModel.writeSaveLogin(UserPage.DOCTOR, AppScreens.DoctorScreen)
            doctorViewModel.setUserData(loginViewModel.userData.value)
            DoctorScreen(navController, doctorViewModel)
        }
        composable(route = AppScreens.VitalSignsScreen.route){
            Log.d("prueba", "Vital Signs Screen")
            VitalSignsScreen(navController = navController, symptomsViewModel = symptomsViewModel)
        }
        composable(route = AppScreens.AdminScreen.route) {
            Log.d("prueba", "Admin screen")
            if (mainViewModel.currentScreen.value.route != AppScreens.AdminScreen.route && detailCardViewModel.detailState.value == DetailState.ENTERING) {
                mainViewModel.writeSaveLogin(UserPage.ADMIN, AppScreens.AdminScreen)
            }
            Log.d("prueba", "${detailCardViewModel.isChanged.value}")
            if (detailCardViewModel.detailState.value == DetailState.ENTERING || detailCardViewModel.isChanged.value) {
                Log.d("prueba", "Actualizando datos.")
                adminViewModel.getUserList()
                detailCardViewModel.setIsChanged(false)
                detailCardViewModel.setDetailState(DetailState.DONE)
            }
            AdminMainScreen(navController, adminViewModel)
        }
        composable(route = AppScreens.DetailCard.route) {
            Log.d("prueba", "Detail screen")
            if(!adminViewModel.clickOnAddButton.value)
                detailCardViewModel.setUserData(adminViewModel.userData.value)
            else detailCardViewModel.clearBoxes()

            DetailCard(navController, detailCardViewModel)
        }
        composable(route = AppScreens.SplashScreen.route) {
            Log.d("prueba", "SplashScreen")
            SplashScreen(navController)
        }
    }
}