package com.example.triagecol.domain.models.dto

import com.google.gson.annotations.SerializedName

class PatientsDto : ArrayList<PatientDto>()

data class PatientDto(
    @SerializedName("id_patient") val id: Int,
    @SerializedName("id_number_pat") val idNumber: String,
    @SerializedName("name_pat") val name: String,
    @SerializedName("lastname_pat") val lastname: String,
    @SerializedName("age_pat") val age: String,
    @SerializedName("gender_pat") val gender: String,
    @SerializedName("temperature") val temperature: String,
    @SerializedName("heart_rate") val heartRate: String,
    @SerializedName("blood_oxygenation") val bloodOxygen: String
)

data class AddPatient(
    @SerializedName("id_number_pat") val idNumber: String,
    @SerializedName("name_pat") val name: String,
    @SerializedName("lastname_pat") val lastname: String,
    @SerializedName("age_pat") val age: String,
    @SerializedName("gender_pat") val gender: String
)

data class AddPatientSymptoms(
    @SerializedName("id_patient") val idNumber: String,
    @SerializedName("id_symptom") val idSymptom: String
)