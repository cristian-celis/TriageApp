package com.example.triagecol.data.remote

import com.example.triagecol.domain.models.LoginModel
import com.example.triagecol.domain.models.dto.ApiResponse
import com.example.triagecol.domain.models.dto.UserDto
import com.example.triagecol.utils.EndPointConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIServiceMain {
    @GET()
    suspend fun initBack(): Response<ApiResponse>
}