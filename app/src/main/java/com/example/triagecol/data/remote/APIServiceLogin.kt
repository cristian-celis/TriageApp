package com.example.triagecol.data.remote

import com.example.triagecol.domain.models.LoginModel
import com.example.triagecol.domain.models.dto.ApiResponse
import com.example.triagecol.domain.models.dto.StaffMemberDto
import com.example.triagecol.domain.models.dto.UserDto
import com.example.triagecol.utils.EndPointConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIServiceLogin {

    // LOGIN METHODS

    @POST(EndPointConstants.LOGIN)
    suspend fun login(@Body login: LoginModel): Response<UserDto>

    @POST(EndPointConstants.STAFF_LOGIN)
    suspend fun staffLogin(@Body login: LoginModel): Response<StaffMemberDto>

    @POST(EndPointConstants.ADMIN_LOGIN)
    suspend fun adminLogin(@Body login: LoginModel): Response<ApiResponse>
}