package com.example.triagecol.utils

object EndPointConstants {
    const val BASE_URL = "https://triage-api.onrender.com/"

    const val LOGIN = "iniciarSesion/"

    const val ASSIGN_PATIENT = "medico/asignarPaciente" // /medico/asignarPaciente

    const val ADD_PATIENT = "paciente/agregarPaciente" // /paciente/agregar
    const val ADD_PATIENT_SYMPTOMS = "paciente/agregarSintomas" // /paciente/agregarSintomas
    const val GET_PATIENT_LIST = "paciente/obtenerPacientes" // /paciente/obtenerPacientes
    const val DELETE_ALL_PATIENTS = "/paciente/eliminarPacientes"

    const val GET_STAFF = "personal/obtenerPersonal" // /personal/obtenerPersonal
    const val ADD_STAFF = "personal/agregarMiembro" // /personal/agregarMiembro
    const val GET_STAFF_MEMBER = "personal/obtenerMiembro" // /personal/obtenerMiembro
    const val EDIT_STAFF_MEMBER = "personal/editarMiembro/{id}" // /personal/editarMiembro/:id
    const val DELETE_STAFF_MEMBER = "personal/eliminarMiembro/{id}" // /personal/eliminarMiembro/:id
    const val UPDATE_DOCTOR_STATUS = "personal/actualizarEstadoMedico" // /personal/actualizarEstadoMedico

    const val GET_PATIENTS_WAITING_COUNT = "paciente/cantidadPacientes" //paciente/cantidadPacientes
}