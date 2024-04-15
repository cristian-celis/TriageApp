package com.example.triagecol.data.remote

import com.example.triagecol.domain.models.dto.AddPatient
import com.example.triagecol.domain.models.dto.AddSymptoms
import com.example.triagecol.domain.models.ApiResponse
import com.example.triagecol.utils.EndPointConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIServicePatient {

    // PATIENT METHODS


    @POST(EndPointConstants.ADD_PATIENT_SYMPTOMS)
    suspend fun addPatientSymptoms(@Body symptomsList: AddSymptoms): Response<ApiResponse>

    @POST(EndPointConstants.ADD_PATIENT)
    suspend fun addPatient(@Body patient: AddPatient): Response<ApiResponse>
}