package com.example.triage.domain.models.dto

import com.google.gson.annotations.SerializedName


data class UserDto(
    @SerializedName("usuario") val user: User,
    @SerializedName("tipoCuenta") val accountType: Int
)

data class User(
    @SerializedName("id_personal") val idStaff: Int?,
    @SerializedName("numero_documento") val idNumberStaff: Int?,
    @SerializedName("id_admin") val idAdmin: Int?,
    @SerializedName("nombre") val name: String?,
    @SerializedName("apellido") val lastname: String?,
    @SerializedName("usuario") val username: String?,
    @SerializedName("telefono") val phoneNumber: String?,
    @SerializedName("rol") val role: String?,
    @SerializedName("estado") val staffStatus: String?
)


fun User.toStaffMemberDto(): StaffMemberDto = StaffMemberDto(
    id = this.idStaff!!,
    idNumber = this.idNumberStaff.toString(),
    name = this.name!!,
    lastname = this.lastname!!,
    username = this.username!!,
    phoneNumber = this.phoneNumber!!,
    role = this.role!!,
    staffStatus = this.staffStatus
)