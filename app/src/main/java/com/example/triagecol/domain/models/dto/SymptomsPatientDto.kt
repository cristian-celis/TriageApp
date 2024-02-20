package com.example.triagecol.domain.models.dto

import com.google.gson.annotations.SerializedName

class SymptomsPatientDto: ArrayList<SymptomPatient>()

data class SymptomPatient(
    @SerializedName("symptom_name") val symptomName: String,
    @SerializedName("description") val description: String
)