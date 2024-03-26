package com.example.triagecol.data.remote

import com.example.triagecol.domain.models.PatientSymptomsModel
import com.example.triagecol.domain.models.PatientVitalSigns
import com.example.triagecol.domain.models.dto.AddPatient
import com.example.triagecol.domain.models.dto.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface APIServicePatient {

    // PATIENT METHODS

    @POST("assignPatient/")
    suspend fun assignPatient(): Response<ApiResponse>

    @POST("addPatientSymptoms/")
    suspend fun addPatientSymptoms(@Body patientSymptomsModel: PatientSymptomsModel): Response<ApiResponse>

    @POST("addVitalSigns")
    suspend fun addVitalSigns(@Body patientVitalSigns: PatientVitalSigns): Response<ApiResponse>

    @PATCH("updatePatientStatus/")
    suspend fun updatePatientStatus(@Body id: String, @Body status: String): Response<ApiResponse>

    @POST("addPatient/")
    suspend fun addPatient(@Body patient: AddPatient): Response<ApiResponse>

    @POST("editPatient/{id}")
    suspend fun editPatient(@Path("id") id: String, @Body patient: AddPatient): Response<ApiResponse>

    @DELETE("deletePatient/")
    suspend fun deletePatient(@Path("id") id: String): Response<ApiResponse>
}