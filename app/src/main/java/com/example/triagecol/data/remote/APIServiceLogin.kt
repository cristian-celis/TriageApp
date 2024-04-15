package com.example.triagecol.data.remote

import com.example.triagecol.domain.models.dto.LoginModel
import com.example.triagecol.domain.models.dto.UserDto
import com.example.triagecol.utils.EndPointConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIServiceLogin {

    // LOGIN METHODS

    @POST(EndPointConstants.LOGIN)
    suspend fun login(@Body login: LoginModel): Response<UserDto>
}