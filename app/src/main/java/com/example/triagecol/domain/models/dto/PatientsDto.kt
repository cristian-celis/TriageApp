package com.example.triagecol.domain.models.dto

import com.google.gson.annotations.SerializedName

class PatientsDto : ArrayList<PatientDto>()

data class PatientDto(
    @SerializedName("id") val id: Int,
    @SerializedName("numero_id") val idNumber: String,
    @SerializedName("nombre") val name: String,
    @SerializedName("apellido") val lastname: String,
    @SerializedName("edad") val age: String,
    @SerializedName("genero") val gender: String,
    @SerializedName("temperatura") val temperature: String,
    @SerializedName("frecuencia_cardiaca") val heartRate: String,
    @SerializedName("oxigenacion_sangre") val bloodOxygen: String
)

data class AddPatient(
    @SerializedName("numero_id") val idNumber: String,
    @SerializedName("nombre") val name: String,
    @SerializedName("apellido") val lastname: String,
    @SerializedName("edad") val age: String,
    @SerializedName("genero") val gender: String,
    @SerializedName("temperatura") val temperature: String,
    @SerializedName("frecuencia_cardiaca") val heartRate: String,
    @SerializedName("oxigenacion_sangre") val bloodOxygen: String
)

data class AddPatientSymptoms(
    @SerializedName("id_paciente") val idNumber: String,
    @SerializedName("id_sintoma") val idSymptom: String
)