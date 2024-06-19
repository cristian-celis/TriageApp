package com.example.triage.domain.models.dto

import com.google.gson.annotations.SerializedName

class PatientsDto : ArrayList<PatientDtoForList>()

data class PatientDtoForList(
    @SerializedName("triage") val triage: Int,
    @SerializedName("nombre") val name: String,
    @SerializedName("apellido") val lastname: String,
    @SerializedName("tipo_documento") val idType: String,
    @SerializedName("numero_documento") val idNumber: String
)

data class PatientDto(
    @SerializedName("id_paciente") val id: Int,
    @SerializedName("tipo_documento") val idType: String,
    @SerializedName("numero_documento") val idNumber: String,
    @SerializedName("nombre") val name: String,
    @SerializedName("apellido") val lastname: String,
    @SerializedName("edad") val age: String,
    @SerializedName("sexo") val sex: String,
    @SerializedName("temperatura") val temperature: String,
    @SerializedName("frecuencia_cardiaca") val heartRate: String,
    @SerializedName("oxigenacion_sangre") val bloodOxygen: String,
    @SerializedName("embarazo") val pregnancy: Int,
    @SerializedName("observaciones") val observations: String,
    @SerializedName("triage") val triage: Int
)

data class AddSymptoms(
    @SerializedName("id_paciente") val id: Int,
    @SerializedName("listaSintomas") val symptomsList: ArrayList<Int>,
    @SerializedName("embarazo") val pregnancy: Boolean,
    @SerializedName("observaciones") val observations: String
)

data class SymptomDto(
    @SerializedName("id_paciente") val id: Int,
    @SerializedName("nombre_sintoma") val symptomName: String
)

data class PriorityPatientDto(
    @SerializedName("paciente") val priorityPatient: PatientDto,
    @SerializedName("sintomasPaciente") val patientSymptoms: List<SymptomDto> = emptyList()
)

data class AddPatientRequest(
    @SerializedName("tipo_documento") val idType: String,
    @SerializedName("numero_documento") var idNumber: String,
    @SerializedName("nombre") var name: String,
    @SerializedName("apellido") var lastname: String,
    @SerializedName("edad") var age: String,
    @SerializedName("sexo") var sex: String,
    @SerializedName("temperatura") var temperature: String,
    @SerializedName("frecuencia_cardiaca") var heartRate: String,
    @SerializedName("oxigenacion_sangre") var bloodOxygen: String
)