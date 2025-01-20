package com.example.triage.domain.repository

import com.example.triage.domain.models.APIResult
import com.example.triage.domain.models.ApiResponse
import com.example.triage.domain.models.dto.AddPatientRequest
import com.example.triage.domain.models.dto.PatientsDto
import com.example.triage.presentation.supervisor.addPatient.PatientData

interface PatientRepository {
    suspend fun savePatientData(patientData: AddPatientRequest): APIResult<ApiResponse>

    suspend fun saveSymptomsPat(
        idNumberPat: String,
        symptomsList: ArrayList<Int>,
        pregnancy: Boolean,
        observations: String
    ): APIResult<ApiResponse>

    suspend fun getPatientList(): APIResult<PatientsDto>

    suspend fun getPatientsWaitingCount(): APIResult<Int>

    suspend fun deletePatient(id: String): APIResult<ApiResponse>
}