package com.example.triagecol.domain.models.dto

import com.google.gson.annotations.SerializedName


data class UserDto(
    @SerializedName("user") val user: User,
    @SerializedName("accountType") val accountType: Int
)

data class User(
    @SerializedName("id_staff") val idStaff: Int?,
    @SerializedName("id_number_staff") val idNumberStaff: Int?,
    @SerializedName("id_admin") val idAdmin: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("lastname") val lastname: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("doctor_status") val doctorStatus: String?
)


fun User.toStaffMemberDto(): StaffMemberDto = StaffMemberDto(
    id = this.idStaff!!,
    idNumber = this.idNumberStaff.toString(),
    name = this.name!!,
    lastname = this.lastname!!,
    username = this.username!!,
    password = "",
    phoneNumber = this.phoneNumber!!,
    role = this.role!!,
    doctorStatus = this.doctorStatus
)