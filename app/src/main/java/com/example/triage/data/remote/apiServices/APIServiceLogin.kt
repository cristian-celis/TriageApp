package com.example.triage.data.remote.apiServices

import com.example.triage.domain.models.dto.LoginModel
import com.example.triage.domain.models.dto.UserDto
import com.example.triage.utils.EndPointConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIServiceLogin {

    @POST(EndPointConstants.LOGIN)
    suspend fun login(@Body login: LoginModel): Response<UserDto>
}