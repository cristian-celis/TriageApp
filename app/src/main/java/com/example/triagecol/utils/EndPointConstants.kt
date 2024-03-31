package com.example.triagecol.utils

object EndPointConstants {
    const val BASE_URL = "https://triage-api.onrender.com/"

    const val UPDATE_DOCTOR_STATUS = "updateDoctorStatus/"
    const val GET_PATIENT = "patients/"
    const val GET_PAT_SYMPTOMS = "patient/symptoms"

    const val LOGIN = "login/"
    const val STAFF_LOGIN = "staffLogin/"
    const val ADMIN_LOGIN = "adminLogin/"

    const val ASSIGN_PATIENT = "assignPatient/"
    const val ADD_PATIENT_SYMPTOMS = "addPatientSymptoms/"
    const val ADD_VITAL_SIGNS = "addVitalSigns"
    const val UPDATE_PATIENT_STATUS = "updatePatientStatus/"
    const val ADD_PATIENT = "addPatient/"
    const val EDIT_PATIENT = "editPatient/{id}"
    const val DELETE_PATIENT = "deletePatient/"

    const val GET_STAFF = "staff/"
    const val ADD_STAFF = "addStaff/"
    const val GET_STAFF_MEMBER = "getStaffMember/"
    const val EDIT_STAFF_MEMBER = "editStaffMember/{id_staff}"
    const val DELETE_STAFF_MEMBER = "deleteStaffMember/{id_staff}"
}