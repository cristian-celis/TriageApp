package com.example.triage.data.remote

import com.example.triage.domain.models.dto.AddPatientRequest
import com.example.triage.domain.models.dto.AddSymptoms
import com.example.triage.domain.models.ApiResponse
import com.example.triage.domain.models.dto.PatientsDto
import com.example.triage.utils.EndPointConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIServicePatient {

    // PATIENT METHODS


    @POST(EndPointConstants.ADD_PATIENT_SYMPTOMS)
    suspend fun addPatientSymptoms(@Body symptomsList: AddSymptoms): Response<ApiResponse>

    @POST(EndPointConstants.ADD_PATIENT)
    suspend fun addPatient(@Body patient: AddPatientRequest): Response<ApiResponse>

    @GET(EndPointConstants.GET_PATIENT_LIST)
    suspend fun getPatientList(): Response<PatientsDto>

    @DELETE(EndPointConstants.DELETE_PATIENT)
    suspend fun deletePatient(@Path("id") id: Int): Response<ApiResponse>
}