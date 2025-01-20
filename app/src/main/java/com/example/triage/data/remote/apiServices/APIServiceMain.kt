package com.example.triage.data.remote.apiServices

import com.example.triage.domain.models.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface APIServiceMain {
    @GET()
    suspend fun initBack(): Response<ApiResponse>
}