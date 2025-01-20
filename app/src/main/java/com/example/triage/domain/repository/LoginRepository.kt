package com.example.triage.domain.repository

import com.example.triage.domain.models.APIResult
import com.example.triage.domain.models.dto.LoginModel
import com.example.triage.domain.models.dto.UserDto

interface LoginRepository {
    suspend fun login(infoLogin: LoginModel): APIResult<UserDto>
}