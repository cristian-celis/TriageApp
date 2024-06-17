package com.example.triage.data.remote

import com.example.triage.domain.models.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface APIServiceMain {
    @GET()
    suspend fun initBack(): Response<ApiResponse>
}