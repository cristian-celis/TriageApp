package com.example.triagecol.presentation.navigation

sealed class AppScreens(val route: String){
    object LoginScreen: AppScreens("login_screen")
    object SupervisorScreen: AppScreens("supervisor_screen")
    object PatientScreen: AppScreens("patient_screen")
    object DoctorScreen: AppScreens("doctor_screen")
    object SymptomsScreen: AppScreens("symptoms_screen")
    object AdminScreen: AppScreens("admin_screen")
    object DetailCard: AppScreens("detail_card")
    object SplashScreen: AppScreens("splash_screen")
}
