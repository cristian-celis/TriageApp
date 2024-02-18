package com.example.triagecol.domain.models

object EnumClasses {
    val role: Map<String, String> = mapOf(
        "Ocupado" to "Busy",
        "Disponible" to "Available",
        "Desconectado" to "Disconnected"
    )

    val doctorStates: Map<String, String> = mapOf(
        "DOCTOR" to "Doctor",
        "SUPERVISOR" to "Supervisor"
    )

    val patientStates: Map<String, String> = mapOf(
        "Esperando" to "Waiting",
        "Atentido" to "Attended"
    )

    val documentType: Map<Int, String> = mapOf(
        1 to "CC",
        2 to "TI",
        3 to "RC"
    )
}