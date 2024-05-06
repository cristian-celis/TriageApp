package com.example.triagecol.data.remote

import com.example.triagecol.domain.models.ApiResponse
import com.example.triagecol.domain.models.dto.DoctorStatus
import com.example.triagecol.domain.models.dto.PriorityPatientDto
import com.example.triagecol.utils.EndPointConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface APIServiceDoctor {

    // DOCTOR METHODS

    @PATCH(EndPointConstants.UPDATE_DOCTOR_STATUS)
    suspend fun updateDoctorStatus(@Body doctorStatus: DoctorStatus): Response<ApiResponse>

    @POST(EndPointConstants.ASSIGN_PATIENT)
    suspend fun assignPatient(): Response<PriorityPatientDto>

    @GET(EndPointConstants.GET_PATIENTS_WAITING_COUNT)
    suspend fun getPatientsWaitingCount(): Response<Int>
}