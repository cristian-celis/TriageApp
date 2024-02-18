package com.example.triagecol.presentation.navigation

sealed class AppScreens(val route: String){
    object LoginScreen: AppScreens("login_screen")
    object UserScreen: AppScreens("user_screen")
    object AdminScreen: AppScreens("admin_screen")
    object DetailCard: AppScreens("detail_card")
    object SplashScreen: AppScreens("splash_screen")
}
