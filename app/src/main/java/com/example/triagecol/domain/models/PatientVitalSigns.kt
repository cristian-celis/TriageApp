package com.example.triagecol.domain.models

import com.google.gson.annotations.SerializedName

data class PatientVitalSigns(
    @SerializedName("id") val id: Int,
    @SerializedName("temperatura") val temperature: Float,
    @SerializedName("frecuencia_cardiaca") val heartRate: Int,
    @SerializedName("oxigenacion_sangre") val bloodOxygen: Int
)
