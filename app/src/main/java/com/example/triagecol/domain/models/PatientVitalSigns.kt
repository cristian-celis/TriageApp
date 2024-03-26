package com.example.triagecol.domain.models

import com.google.gson.annotations.SerializedName

data class PatientVitalSigns(
    @SerializedName("id_patient") val id: Int,
    @SerializedName("temperature") val temperature: Float,
    @SerializedName("heart_rate") val heartRate: Int,
    @SerializedName("blood_oxygenation") val bloodOxygen: Int
)
