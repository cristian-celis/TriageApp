package com.example.triagecol.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.triagecol.presentation.login.LoginScreen
import com.example.triagecol.presentation.user.UserScreen

@Composable
fun AppNavigation(mainViewModel: MainViewModel) {

    val adminViewModel: AdminViewModel = hiltViewModel()
    val detailCardViewModel: DetailCardViewModel = hiltViewModel()

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.route) {
        composable(route = AppScreens.LoginScreen.route) {
            if (mainViewModel.currentScreen.value.route != AppScreens.LoginScreen.route) mainViewModel.writeSaveLogin(
                UserPage.LOGIN,
                AppScreens.LoginScreen
            )
            LoginScreen(navController)
        }
        composable(route = AppScreens.UserScreen.route) {
            mainViewModel.writeSaveLogin(UserPage.ASSISTANT, AppScreens.UserScreen)
            UserScreen(navController)
        }
        composable(route = AppScreens.AdminScreen.route) {
            if (mainViewModel.currentScreen.value.route != AppScreens.AdminScreen.route) {
                if (detailCardViewModel.detailState.value == DetailState.ENTERING)
                    mainViewModel.writeSaveLogin(UserPage.ADMIN, AppScreens.AdminScreen)
            }
            if (detailCardViewModel.detailState.value == DetailState.ENTERING) {
                adminViewModel.getUserList()
                detailCardViewModel.setDetailState(DetailState.DONE)
            }
            AdminMainScreen(navController)
        }
        composable(route = AppScreens.DetailCard.route) {
            if (detailCardViewModel.userData.value != adminViewModel.userData.value) {
                detailCardViewModel.setUserData(adminViewModel.userData.value)
            }
            DetailCard(navController, detailCardViewModel)
        }
        composable(route = AppScreens.SplashScreen.route) {
            SplashScreen(navController, mainViewModel)
        }
    }
}