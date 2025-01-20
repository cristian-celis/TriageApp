package com.example.triage.domain.repository

import com.example.triage.domain.models.APIResult
import com.example.triage.domain.models.ApiResponse
import com.example.triage.domain.models.dto.ReportsDto
import com.example.triage.domain.models.dto.ReportsRequest
import com.example.triage.domain.models.dto.StaffDto
import com.example.triage.domain.models.dto.StaffMember
import com.example.triage.domain.models.dto.StaffMemberDto

interface StaffRepository {
    suspend fun getStaff(): APIResult<StaffDto>

    suspend fun addStaff(user: StaffMember): APIResult<ApiResponse?>

    suspend fun editStaff(user: StaffMember, userId: Int): APIResult<ApiResponse?>

    suspend fun deleteStaffMember(idUser: String): APIResult<ApiResponse?>

    suspend fun getStaffCount(): APIResult<Int>

    suspend fun getStaffMember(id: String): APIResult<StaffMemberDto>

    suspend fun getReports(date: ReportsRequest): APIResult<ReportsDto>
}