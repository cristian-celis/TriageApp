package com.example.triage.domain.repository

import com.example.triage.domain.models.APIResult
import com.example.triage.domain.models.ApiResponse
import com.example.triage.domain.models.dto.PriorityPatientDto
import com.example.triage.domain.models.dto.StaffStatus

interface DoctorRepository {
    suspend fun assignPatient(): APIResult<PriorityPatientDto>

    suspend fun updateDoctorStatus(staffStatus: StaffStatus): APIResult<ApiResponse>

    suspend fun getPatientsWaitingCount(): APIResult<Int>
}