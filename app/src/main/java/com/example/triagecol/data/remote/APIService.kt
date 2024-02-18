package com.example.triagecol.data.remote

import com.example.triagecol.domain.models.dto.AddUserDtoItem
import com.example.triagecol.domain.models.dto.ApiResponse
import com.example.triagecol.domain.models.dto.StaffMemberDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIService {

    @GET("list/")
    suspend fun getPeople(): Response<List<StaffMemberDto>>

    @POST("add/")
    suspend fun addUser(@Body user: StaffMemberDto): Response<ApiResponse>

    @POST("edit/{id}")
    suspend fun editUser(@Path("id") id: String, @Body user: AddUserDtoItem): Response<ApiResponse>

    @GET("delete/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<ApiResponse>

    @GET("addPatient/")
    suspend fun addPatient(@Body patient: )

    /*
    https://triage-api.onrender.com/
     */
}