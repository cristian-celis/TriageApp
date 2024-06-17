package com.example.triage.data.remote

import com.example.triage.domain.models.ApiResponse
import com.example.triage.domain.models.dto.StaffStatus
import com.example.triage.domain.models.dto.PriorityPatientDto
import com.example.triage.utils.EndPointConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface APIServiceDoctor {

    // DOCTOR METHODS

    @PATCH(EndPointConstants.UPDATE_DOCTOR_STATUS)
    suspend fun updateDoctorStatus(@Body staffStatus: StaffStatus): Response<ApiResponse>

    @GET(EndPointConstants.ASSIGN_PATIENT)
    suspend fun assignPatient(): Response<PriorityPatientDto>

    @GET(EndPointConstants.GET_PATIENTS_WAITING_COUNT)
    suspend fun getPatientsWaitingCount(): Response<Int>
}