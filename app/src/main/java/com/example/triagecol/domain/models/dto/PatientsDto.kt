package com.example.triagecol.domain.models.dto

import com.google.gson.annotations.SerializedName

class PatientsDto : ArrayList<PatientDtoForList>()

data class PatientDtoForList(
    @SerializedName("nombre") val name: String,
    @SerializedName("apellido") val lastname: String,
    @SerializedName("numero_id") val idNumber: String
)

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

data class AddSymptoms(
    @SerializedName("id") val id: Int,
    @SerializedName("listaSintomas") val symptomsList: ArrayList<Int>
)

data class SymptomDto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre_sintoma") val symptomName: String
)

data class PriorityPatientDto(
    @SerializedName("pacientePrioritario") val priorityPatient: PatientDto,
    @SerializedName("sintomasPaciente") val patientSymptoms: List<SymptomDto> = emptyList()
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