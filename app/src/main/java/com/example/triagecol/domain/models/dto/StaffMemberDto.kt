package com.example.triagecol.domain.models.dto

import com.google.gson.annotations.SerializedName

class StaffDto : ArrayList<StaffMemberDto>()

data class StaffMemberDto(
    @SerializedName("id") val id: Int,
    @SerializedName("numero_id") val idNumber: String,
    @SerializedName("nombre") val name: String,
    @SerializedName("apellido") val lastname: String,
    @SerializedName("usuario") val username: String,
    @SerializedName("telefono") val phoneNumber: String,
    @SerializedName("rol") val role: String,
    @SerializedName("estado") val doctorStatus: String? = null
)
data class StaffMember(
    @SerializedName("numero_id") val idNumber: String,
    @SerializedName("nombre") val name: String,
    @SerializedName("apellido") val lastname: String,
    @SerializedName("usuario") val username: String,
    @SerializedName("clave") val password: String,
    @SerializedName("telefono") val phoneNumber: String,
    @SerializedName("rol") val role: String,
    @SerializedName("estado") val doctorStatus: String? = null
)
data class DoctorStatus(
    @SerializedName("id") val id: Int,
    @SerializedName("estado") val doctorStatus: String
)