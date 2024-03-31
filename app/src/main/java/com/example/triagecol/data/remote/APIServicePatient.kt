package com.example.triagecol.data.remote

import com.example.triagecol.domain.models.PatientSymptomsModel
import com.example.triagecol.domain.models.PatientVitalSigns
import com.example.triagecol.domain.models.dto.AddPatient
import com.example.triagecol.domain.models.dto.ApiResponse
import com.example.triagecol.utils.EndPointConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface APIServicePatient {

    // PATIENT METHODS

    @POST(EndPointConstants.ASSIGN_PATIENT)
    suspend fun assignPatient(): Response<ApiResponse>

    @POST(EndPointConstants.ADD_PATIENT_SYMPTOMS)
    suspend fun addPatientSymptoms(@Body patientSymptomsModel: PatientSymptomsModel): Response<ApiResponse>

    @POST(EndPointConstants.ADD_VITAL_SIGNS)
    suspend fun addVitalSigns(@Body patientVitalSigns: PatientVitalSigns): Response<ApiResponse>

    @PATCH(EndPointConstants.UPDATE_PATIENT_STATUS)
    suspend fun updatePatientStatus(@Body id: String, @Body status: String): Response<ApiResponse>

    @POST(EndPointConstants.ADD_PATIENT)
    suspend fun addPatient(@Body patient: AddPatient): Response<ApiResponse>

    @POST(EndPointConstants.EDIT_PATIENT)
    suspend fun editPatient(@Path("id") id: String, @Body patient: AddPatient): Response<ApiResponse>

    @DELETE(EndPointConstants.DELETE_PATIENT)
    suspend fun deletePatient(@Path("id") id: String): Response<ApiResponse>
}