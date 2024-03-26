package com.example.triagecol.data.remote

import com.example.triagecol.domain.models.LoginModel
import com.example.triagecol.domain.models.dto.ApiResponse
import com.example.triagecol.domain.models.dto.StaffMemberDto
import com.example.triagecol.domain.models.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIServiceLogin {

    // LOGIN METHODS

    @POST("login/")
    suspend fun login(@Body login: LoginModel): Response<UserDto>

    @POST("staffLogin/")
    suspend fun staffLogin(@Body login: LoginModel): Response<StaffMemberDto>

    @POST("adminLogin/")
    suspend fun adminLogin(@Body login: LoginModel): Response<ApiResponse>
}