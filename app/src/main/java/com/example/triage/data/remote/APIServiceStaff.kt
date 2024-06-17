package com.example.triage.data.remote

import com.example.triage.domain.models.dto.StaffMember
import com.example.triage.domain.models.ApiResponse
import com.example.triage.domain.models.dto.ReportsDto
import com.example.triage.domain.models.dto.ReportsRequest
import com.example.triage.domain.models.dto.StaffDto
import com.example.triage.domain.models.dto.StaffMemberDto
import com.example.triage.utils.EndPointConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIServiceStaff {

    // STAFF MEDICAL METHODS

    @GET(EndPointConstants.GET_STAFF)
    suspend fun getStaff(): Response<StaffDto>

    @POST(EndPointConstants.ADD_STAFF)
    suspend fun addStaff(@Body user: StaffMember): Response<ApiResponse>

    @POST(EndPointConstants.EDIT_STAFF_MEMBER)
    suspend fun editStaffMember(@Path("id") id: String, @Body user: StaffMember): Response<ApiResponse>

    @DELETE(EndPointConstants.DELETE_STAFF_MEMBER)
    suspend fun deleteStaff(@Path("id") id: String): Response<ApiResponse>

    @DELETE(EndPointConstants.DELETE_ALL_PATIENTS)
    suspend fun deleteAllPatients(): Response<ApiResponse>

    @GET(EndPointConstants.REGISTERED_STAFF)
    suspend fun getStaffCount(): Response<Int>

    @GET(EndPointConstants.GET_STAFF_MEMBER)
    suspend fun getStaffMember(@Path("id") id: Int): Response<StaffMemberDto>

    @POST(EndPointConstants.GET_REPORTS)
    suspend fun getReports(@Body reportsRequest: ReportsRequest): Response<ReportsDto>

    /*
    https://triage-api.onrender.com/
     */
}