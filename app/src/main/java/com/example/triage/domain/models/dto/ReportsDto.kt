package com.example.triage.domain.models.dto

import com.google.gson.annotations.SerializedName

data class ReportsDto(
    @SerializedName("pacientesAtendidos") val patientsAttended: Int,
    @SerializedName("tiempoPromedio") val averageTime: String,
    @SerializedName("pacientesUrgentes") val urgentPatient: Int
)

data class ReportsRequest(
    val year: Int,
    val month: Int,
    val day: Int
){
    override fun toString(): String {
        return "$day-$month-$year"
    }
}