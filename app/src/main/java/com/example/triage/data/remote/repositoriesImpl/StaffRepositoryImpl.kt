package com.example.triage.data.remote.repositoriesImpl

import com.example.triage.data.remote.apiServices.APIServiceStaff
import com.example.triage.domain.models.APIResult
import com.example.triage.domain.models.dto.StaffMember
import com.example.triage.domain.models.ApiResponse
import com.example.triage.domain.models.dto.ReportsDto
import com.example.triage.domain.models.dto.ReportsRequest
import com.example.triage.domain.models.dto.StaffDto
import com.example.triage.domain.models.dto.StaffMemberDto
import com.example.triage.domain.repository.StaffRepository
import com.google.gson.Gson
import retrofit2.Response
import retrofit2.Retrofit
import java.net.UnknownHostException
import javax.inject.Inject

class StaffRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
): StaffRepository {

    override suspend fun getStaff(): APIResult<StaffDto> {
        return try {
            val call = retrofit.create(APIServiceStaff::class.java).getStaff()
            val userList = call.body()
            if (call.isSuccessful) {
                APIResult.Success(userList!!)
            } else {
                val errorBody = call.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                APIResult.Error(Exception(errorResponse.message))
            }
        }catch (e:UnknownHostException){
            APIResult.Error(Exception("Error de conexión: Asegurate de tener acceso a internet"))
        }catch (e:Exception){
            APIResult.Error(e)
        }
    }

    override suspend fun addStaff(user: StaffMember): APIResult<ApiResponse?> {
        return try {
            val response: Response<ApiResponse> =
                retrofit.create(APIServiceStaff::class.java).addStaff(user)

            if (response.isSuccessful) {
                APIResult.Success(response.body())
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                APIResult.Error(Exception(errorResponse.message))
            }
        }catch (e:UnknownHostException){
            APIResult.Error(Exception("Error de conexión: Asegurate de tener acceso a internet"))
        }catch (e:Exception){
            APIResult.Error(e)
        }
    }

    override suspend fun editStaff(user: StaffMember, userId: Int): APIResult<ApiResponse?> {
        return try {
            val response: Response<ApiResponse> = retrofit.create(APIServiceStaff::class.java)
                .editStaffMember(userId.toString(), user)

            if (response.isSuccessful && response.body() != null) {
                APIResult.Success(response.body())
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                APIResult.Error(Exception(errorResponse.message))
            }
        }catch (e: UnknownHostException){
            APIResult.Error(Exception("Error de conexión: Asegurate de tener acceso a internet"))
        }catch (e:Exception){
            APIResult.Error(e)
        }
    }

    override suspend fun deleteStaffMember(idUser: String): APIResult<ApiResponse?> {
        return try {
            val response: Response<ApiResponse> = retrofit.create(APIServiceStaff::class.java).deleteStaff(idUser)
            if (response.isSuccessful && response.body() != null) {
                APIResult.Success(response.body())
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                APIResult.Error(Exception(errorResponse.message))
            }
        }catch (e:UnknownHostException){
            APIResult.Error(Exception("Error de conexión: Asegurate de tener acceso a internet"))
        }catch (e:Exception){
            APIResult.Error(e)
        }
    }

    override suspend fun getStaffCount(): APIResult<Int>{
        return try {
            val call = retrofit.create(APIServiceStaff::class.java).getStaffCount()
            if(call.isSuccessful){
                APIResult.Success(call.body()!!)
            }else{
                val errorBody = call.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                APIResult.Error(Exception(errorResponse.message))
            }
        }catch (e:UnknownHostException){
            APIResult.Error(Exception("Error de conexión: Asegurate de tener acceso a internet"))
        }catch (e:Exception){
            APIResult.Error(e)
        }
    }

    override suspend fun getStaffMember(id: String): APIResult<StaffMemberDto>{
        return try{
            val call = retrofit.create(APIServiceStaff::class.java).getStaffMember(id.toInt())
            if(call.isSuccessful){
                APIResult.Success(call.body()!!)
            }else{
                val errorBody = call.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                APIResult.Error(Exception(errorResponse.message))
            }
        }catch (e: UnknownHostException){
            APIResult.Error(Exception("Error de conexión: Asegurate de tener acceso a internet"))
        }catch (e:Exception){
            APIResult.Error(e)
        }
    }

    override suspend fun getReports(date: ReportsRequest): APIResult<ReportsDto>{
        return try {
            val call = retrofit.create(APIServiceStaff::class.java).getReports(date)
            if(call.isSuccessful){
                APIResult.Success(call.body()!!)
            }else{
                val errorBody = call.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                APIResult.Error(Exception(errorResponse.message))
            }
        }catch (e:Exception){
            APIResult.Error(e)
        }
    }
}