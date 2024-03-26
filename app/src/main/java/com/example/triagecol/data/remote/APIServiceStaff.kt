package com.example.triagecol.data.remote

import com.example.triagecol.domain.models.dto.AddPatient
import com.example.triagecol.domain.models.PatientSymptomsModel
import com.example.triagecol.domain.models.dto.StaffMember
import com.example.triagecol.domain.models.dto.ApiResponse
import com.example.triagecol.domain.models.dto.PatientDto
import com.example.triagecol.domain.models.dto.PatientsDto
import com.example.triagecol.domain.models.dto.StaffDto
import com.example.triagecol.domain.models.dto.StaffMemberDto
import com.example.triagecol.domain.models.dto.SymptomsPatientDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface APIServiceStaff {

    // STAFF MEDICAL METHODS

    @GET("staff/")
    suspend fun getStaff(): Response<StaffDto>

    @POST("addStaff/")
    suspend fun addStaff(@Body user: StaffMember): Response<ApiResponse>

    @GET("getStaffMember/")
    suspend fun getStaffMember(@Body id: String): Response<PatientDto>

    @POST("editStaffMember/{id_staff}")
    suspend fun editStaffMember(@Path("id_staff") id: String, @Body user: StaffMember): Response<ApiResponse>

    @DELETE("deleteStaffMember/{id_staff}")
    suspend fun deleteStaff(@Path("id_staff") id: String): Response<ApiResponse>

    /*
    https://triage-api.onrender.com/
     */
}