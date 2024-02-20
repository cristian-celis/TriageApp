package com.example.triagecol.domain.models

import com.google.gson.annotations.SerializedName

data class PatientSymptomsModel(
    @SerializedName("id_patient") val id: Int,
    @SerializedName("id_symptom") val idSymptom: ArrayList<Int>
)
