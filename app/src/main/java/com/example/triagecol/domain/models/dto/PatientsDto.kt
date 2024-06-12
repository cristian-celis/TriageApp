package com.example.triagecol.domain.models.dto

import com.google.gson.annotations.SerializedName

class PatientsDto : ArrayList<PatientDtoForList>()

data class PatientDtoForList(
    @SerializedName("prioridad") val priorityPatient: Int,
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
    @SerializedName("sexo") val sex: String,
    @SerializedName("temperatura") val temperature: String,
    @SerializedName("frecuencia_cardiaca") val heartRate: String,
    @SerializedName("oxigenacion_sangre") val bloodOxygen: String,
    @SerializedName("embarazo") val pregnancy: Int,
    @SerializedName("observaciones") val observations: String
)

data class Patient(
    @SerializedName("id") val id: Int,
    @SerializedName("numero_id") val idNumber: String,
    @SerializedName("nombre") val name: String,
    @SerializedName("apellido") val lastname: String,
    @SerializedName("edad") val age: String,
    @SerializedName("sexo") val sex: String,
    @SerializedName("temperatura") val temperature: String,
    @SerializedName("frecuencia_cardiaca") val heartRate: String,
    @SerializedName("oxigenacion_sangre") val bloodOxygen: String,
    @SerializedName("embarazo") val pregnancy: Boolean,
    @SerializedName("observaciones") val observations: String
)

fun PatientDto.toPatient(): Patient = Patient(
    id = this.id,
    idNumber = this.idNumber,
    name = this.name,
    lastname = this.lastname,
    age = this.age,
    sex = this.sex,
    temperature = this.temperature,
    heartRate = this.heartRate,
    bloodOxygen = this.bloodOxygen,
    pregnancy = this.pregnancy == 1,
    observations = this.observations
)

data class AddSymptoms(
    @SerializedName("id") val id: Int,
    @SerializedName("listaSintomas") val symptomsList: ArrayList<Int>,
    @SerializedName("embarazo") val pregnancy: Boolean,
    @SerializedName("observaciones") val observations: String
)

data class SymptomDto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre_sintoma") val symptomName: String
)

data class PriorityPatientDto(
    @SerializedName("pacientePrioritario") val priorityPatient: PatientDto,
    @SerializedName("sintomasPaciente") val patientSymptoms: List<SymptomDto> = emptyList()
)

data class AddPatientRequest(
    @SerializedName("numero_id") var idNumber: String,
    @SerializedName("nombre") var name: String,
    @SerializedName("apellido") var lastname: String,
    @SerializedName("edad") var age: String,
    @SerializedName("sexo") var sex: String,
    @SerializedName("temperatura") var temperature: String,
    @SerializedName("frecuencia_cardiaca") var heartRate: String,
    @SerializedName("oxigenacion_sangre") var bloodOxygen: String
)