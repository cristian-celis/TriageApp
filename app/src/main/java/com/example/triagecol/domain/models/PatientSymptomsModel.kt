package com.example.triagecol.domain.models

import com.google.gson.annotations.SerializedName

data class PatientSymptomsModel(
    @SerializedName("numero_id") val id: Int,
    @SerializedName("id") val idSymptom: ArrayList<Int>
)