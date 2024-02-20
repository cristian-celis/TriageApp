package com.example.triagecol.domain.models.dto

import com.google.gson.annotations.SerializedName

class StaffDto : ArrayList<StaffMemberDto>()

data class StaffMemberDto(
    @SerializedName("id_staff") val id: Int,
    @SerializedName("id_document_staff") val idNumber: String,
    @SerializedName("name") val name: String,
    @SerializedName("lastname") val lastname: String,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("role") val role: String,
    @SerializedName("doctor_states") val doctorStates: String? = null
)
data class StaffMember(
    @SerializedName("id_document_staff") val idNumber: String,
    val name: String,
    val lastname: String,
    val username: String,
    val password: String,
    @SerializedName("phone_number") val phoneNumber: String,
    val role: String,
    val doctorStates: String? = null
)

fun StaffMemberDto.toStaffMember(): StaffMember {
    return StaffMember(
        idNumber = this.idNumber,
        name = this.name,
        lastname = this.lastname,
        username = this.username,
        password = this.password,
        phoneNumber = this.phoneNumber,
        role = this.role,
        doctorStates = this.doctorStates
    )
}