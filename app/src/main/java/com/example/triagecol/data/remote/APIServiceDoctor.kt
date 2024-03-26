package com.example.triagecol.data.remote

import com.example.triagecol.domain.models.dto.ApiResponse
import com.example.triagecol.domain.models.dto.PatientsDto
import com.example.triagecol.domain.models.dto.SymptomsPatientDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface APIServiceDoctor {

    // DOCTOR METHODS

    @PATCH("updateDoctorStatus/")
    suspend fun updateStaffMemberStates(@Body id: String, @Body status: String): Response<ApiResponse>

    @GET("patients/")
    suspend fun getPatients(): Response<PatientsDto>

    @GET("patient/symptoms")
    suspend fun getPatientSymptoms(@Body id: String): Response<SymptomsPatientDto>
}