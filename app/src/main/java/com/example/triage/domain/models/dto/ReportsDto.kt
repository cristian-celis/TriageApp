package com.example.triage.domain.models.dto

import com.google.gson.annotations.SerializedName

data class ReportsDto(
    @SerializedName("pacientesAtendidos") val patientsAttended: Int,
    @SerializedName("promedioEspera") val averageTime: String,
    @SerializedName("triage_I") val triageI: Int,
    @SerializedName("triage_II") val triageII: Int,
    @SerializedName("triage_III") val triageIII: Int,
    @SerializedName("triage_IV") val triageIV: Int,
    @SerializedName("triage_V") val triageV: Int
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