package com.example.triagecol.utils

object EndPointConstants {
    const val BASE_URL = "https://triage-api.onrender.com/"

    const val LOGIN = "iniciarSesion/"

    const val ASSIGN_PATIENT = "medico/asignarPaciente"

    const val ADD_PATIENT = "paciente/agregarPaciente"
    const val ADD_PATIENT_SYMPTOMS = "paciente/agregarSintomas"
    const val GET_PATIENT_LIST = "paciente/obtenerPacientes"
    const val DELETE_ALL_PATIENTS = "/paciente/eliminarPacientes"
    const val DELETE_PATIENT = "/paciente/eliminarPaciente/{id}"

    const val GET_STAFF = "personal/obtenerPersonal"
    const val ADD_STAFF = "personal/agregarMiembro"
    const val EDIT_STAFF_MEMBER = "personal/editarMiembro/{id}"
    const val DELETE_STAFF_MEMBER = "personal/eliminarMiembro/{id}"
    const val UPDATE_DOCTOR_STATUS = "personal/actualizarEstadoMedico"
    const val REGISTERED_STAFF = "/personal/cantidadPersonal"

    const val GET_STAFF_MEMBER = "personal/obtenerMiembro/{id}"

    const val GET_PATIENTS_WAITING_COUNT = "paciente/cantidadPacientes"
}