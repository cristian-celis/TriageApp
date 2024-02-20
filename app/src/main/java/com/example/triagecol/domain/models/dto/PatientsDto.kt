package com.example.triagecol.domain.models.dto

import com.google.gson.annotations.SerializedName

class PatientsDto : ArrayList<PatientDto>()

data class PatientDto(
    @SerializedName("id_patient") val id: Int,
    @SerializedName("id_number_patient") val idNumber: String,
    @SerializedName("name_patient") val name: String,
    @SerializedName("lastname_patient") val lastname: String,
    @SerializedName("patient_status") val status: String,
    @SerializedName("document_type") val documentType: String
)

data class AddPatient(
    val idNumber: String,
    val name: String,
    val lastname: String,
    val status: String,
    val documentType: String
)