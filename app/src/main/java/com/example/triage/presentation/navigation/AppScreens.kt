package com.example.triage.presentation.navigation

sealed class AppScreens(val route: String){
    object LoginScreen: AppScreens("login_screen")
    object DoctorScreen: AppScreens("doctor_screen/{id}"){
        fun addUserId(id: String) = "doctor_screen/$id"
    }
    object MainSupervisorScreen: AppScreens("main_supervisor_screen/{id}"){
        fun addUserId(id: String) = "main_supervisor_screen/$id"
    }
    object AddPatientScreen: AppScreens("add_patient_screen")
    object AddSymptomsScreen: AppScreens("add_symptoms_screen/{id}/{sex}"){
        fun setInitialValues(id: String, sex: String) = "add_symptoms_screen/$id/$sex"
    }
    object AdminScreen: AppScreens("admin_screen")
    object DetailCard: AppScreens("detail_card/{id}/{editTitle}"){
        fun setInitialValues(id: String, title: Boolean) = "detail_card/$id/$title"
    }
    object SplashScreen: AppScreens("splash_screen")
}
