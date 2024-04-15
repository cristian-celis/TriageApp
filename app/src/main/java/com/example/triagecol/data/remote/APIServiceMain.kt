package com.example.triagecol.data.remote

import com.example.triagecol.domain.models.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface APIServiceMain {
    @GET()
    suspend fun initBack(): Response<ApiResponse>
}