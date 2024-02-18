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