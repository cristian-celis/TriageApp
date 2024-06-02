package com.example.triagecol.data.remote

import com.example.triagecol.domain.models.dto.AddPatient
import com.example.triagecol.domain.models.dto.AddSymptoms
import com.example.triagecol.domain.models.ApiResponse
import com.example.triagecol.domain.models.dto.PatientsDto
import com.example.triagecol.utils.EndPointConstants
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
    suspend fun addPatient(@Body patient: AddPatient): Response<ApiResponse>

    @GET(EndPointConstants.GET_PATIENT_LIST)
    suspend fun getPatientList(): Response<PatientsDto>

    @DELETE(EndPointConstants.DELETE_PATIENT)
    suspend fun deletePatient(@Path("id") id: Int): Response<ApiResponse>
}