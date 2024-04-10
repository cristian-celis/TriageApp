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
import com.example.triagecol.utils.EndPointConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface APIServiceStaff {

    // STAFF MEDICAL METHODS

    @GET(EndPointConstants.GET_STAFF)
    suspend fun getStaff(): Response<StaffDto>

    @POST(EndPointConstants.ADD_STAFF)
    suspend fun addStaff(@Body user: StaffMember): Response<ApiResponse>

    @GET(EndPointConstants.GET_STAFF_MEMBER)
    suspend fun getStaffMember(@Body id: String): Response<PatientDto>

    @POST(EndPointConstants.EDIT_STAFF_MEMBER)
    suspend fun editStaffMember(@Path("id") id: String, @Body user: StaffMember): Response<ApiResponse>

    @DELETE(EndPointConstants.DELETE_STAFF_MEMBER)
    suspend fun deleteStaff(@Path("id") id: String): Response<ApiResponse>

    /*
    https://triage-api.onrender.com/
     */
}