package com.example.triagecol.utils

object EndPointConstants {
    const val BASE_URL = "https://triage-api.onrender.com/"

    const val UPDATE_DOCTOR_STATUS = "personal/actualizarEstadoMedico" // /personal/actualizarEstadoMedico (PATCH)
    const val GET_PATIENT_LIST = "paciente/obtenerPacientes" // /paciente/obtenerPacientes (GET)
    const val GET_PATIENT = "paciente/obtenerPaciente" // /paciente/obtenerPaciente (GET)
    const val GET_PAT_SYMPTOMS = "paciente/obtenerSintomas" // /paciente/obtenerSintomas (GET)

    const val LOGIN = "iniciarSesion/"

    const val ASSIGN_PATIENT = "medico/asignarPaciente" // /medico/asignarPaciente (POST)
    const val ADD_PATIENT_SYMPTOMS = "paciente/agregarSintomas" // /paciente/agregarSintomas (POST)
    const val ADD_VITAL_SIGNS = "paciente/agregarSignosVitales" // /paciente/agregarSignosVitales (POST)
    const val ADD_PATIENT = "paciente/agregarPaciente" // /paciente/agregar (POST)
    const val EDIT_PATIENT = "paciente/editarPaciente/{id}" // /paciente/editarPaciente/:id (POST)
    const val DELETE_PATIENT = "paciente/eliminarPaciente/{id}" // /paciente/eliminarPaciente/:id (DELETE)

    const val GET_STAFF = "personal/obtenerPersonal" // /personal/obtenerPersonal (GET)
    const val ADD_STAFF = "personal/agregarMiembro" // /personal/agregarMiembro (POST)
    const val GET_STAFF_MEMBER = "personal/obtenerMiembro" // /personal/obtenerMiembro (GET)
    const val EDIT_STAFF_MEMBER = "personal/editarMiembro/{id}" // /personal/editarMiembro/:id (POST)
    const val DELETE_STAFF_MEMBER = "personal/eliminarMiembro/{id}" // /personal/eliminarMiembro/:id (DELETE)
}