package com.example.triagecol.domain.models.dto

import com.google.gson.annotations.SerializedName

class SymptomsPatientDto: ArrayList<SymptomPatient>()

data class SymptomPatient(
    @SerializedName("nombre_sintoma") val symptomName: String
)