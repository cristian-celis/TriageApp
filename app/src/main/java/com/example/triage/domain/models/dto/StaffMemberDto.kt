package com.example.triage.domain.models.dto

import com.google.gson.annotations.SerializedName

class StaffDto : ArrayList<StaffMemberDto>()

data class StaffMemberDto(
    @SerializedName("id_personal") val id: Int,
    @SerializedName("numero_documento") val idNumber: String,
    @SerializedName("nombre") val name: String,
    @SerializedName("apellido") val lastname: String,
    @SerializedName("usuario") val username: String,
    @SerializedName("telefono") val phoneNumber: String,
    @SerializedName("rol") val role: String,
    @SerializedName("estado") val staffStatus: String? = null
)
data class StaffMember(
    @SerializedName("numero_documento") val idNumber: String,
    @SerializedName("nombre") val name: String,
    @SerializedName("apellido") val lastname: String,
    @SerializedName("usuario") val username: String,
    @SerializedName("clave") val password: String,
    @SerializedName("telefono") val phoneNumber: String,
    @SerializedName("rol") val role: String,
    @SerializedName("estado") val staffStatus: String? = null
)
data class StaffStatus(
    @SerializedName("id_personal") val id: Int,
    @SerializedName("estado") val staffStatus: String
)

data class StaffMemberAccount(
    var id: Int,
    var idNumber: String,
    var fullName: String,
    var status: String
)