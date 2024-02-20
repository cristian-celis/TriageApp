package com.example.triagecol.data.remote

import com.example.triagecol.domain.models.dto.AddPatient
import com.example.triagecol.domain.models.PatientSymptomsModel
import com.example.triagecol.domain.models.dto.StaffMember
import com.example.triagecol.domain.models.dto.ApiResponse
import com.example.triagecol.domain.models.LoginModel
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

interface APIService {

    @GET("list/")
    suspend fun getPeople(): Response<List<StaffMemberDto>>

    @POST("addStaff/")
    suspend fun addStaff(@Body user: StaffMember): Response<ApiResponse>

    @POST("login/")
    suspend fun login(@Body login: LoginModel): Response<ApiResponse>

    @GET("staff/")
    suspend fun getStaff(): Response<StaffDto>

    @POST("editStaffMember/{id}")
    suspend fun editStaffMember(@Path("id") id: String, @Body user: StaffMember): Response<ApiResponse>

    @DELETE("deleteStaff/{id}")
    suspend fun deleteStaff(@Path("id") id: String): Response<ApiResponse>

    @PATCH("updateStaffMemberStatus/{id}")
    suspend fun updateStaffMemberStates(@Path("id") id: String, @Body status: String): Response<ApiResponse>

    @PATCH("updatePatientStatus/{id}")
    suspend fun updatePatientStatus(@Path("id") id: String, @Body status: String): Response<ApiResponse>

    @GET("patients/")
    suspend fun getPatients(): Response<PatientsDto>

    @POST("editPatient/{id}")
    suspend fun editPatient(@Path("id") id: String, @Body patient: AddPatient): Response<ApiResponse>

    @DELETE("deletePatient/{id}")
    suspend fun deletePatient(@Path("id") id: String): Response<ApiResponse>

    @GET("getStaffMember/{id}")
    suspend fun getStaffMember(@Path("id") id: String): Response<PatientDto>

    @GET("patient/{id}/symptoms")
    suspend fun getPatientSymptoms(@Path("id") id: String): Response<SymptomsPatientDto>

    @POST("addPatient/")
    suspend fun addPatient(@Body patient: AddPatient): Response<ApiResponse>

    @POST("addPatientSymptoms/")
    suspend fun addPatientSymptoms(@Body patientSymptomsModel: PatientSymptomsModel): Response<ApiResponse>

    /*
    https://triage-api.onrender.com/
     */
}