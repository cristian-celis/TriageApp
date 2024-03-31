package com.example.triagecol.data.remote

import com.example.triagecol.domain.models.dto.ApiResponse
import com.example.triagecol.domain.models.dto.PatientsDto
import com.example.triagecol.domain.models.dto.SymptomsPatientDto
import com.example.triagecol.utils.EndPointConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface APIServiceDoctor {

    // DOCTOR METHODS

    @PATCH(EndPointConstants.UPDATE_DOCTOR_STATUS)
    suspend fun updateStaffMemberStates(@Body id: String, @Body status: String): Response<ApiResponse>

    @GET(EndPointConstants.GET_PATIENT)
    suspend fun getPatients(): Response<PatientsDto>

    @GET(EndPointConstants.GET_PAT_SYMPTOMS)
    suspend fun getPatientSymptoms(@Body id: String): Response<SymptomsPatientDto>
}