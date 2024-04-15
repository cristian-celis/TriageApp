package com.example.triagecol.domain.usecases

import android.util.Log
import com.example.triagecol.data.remote.APIServiceStaff
import com.example.triagecol.domain.models.APIResult
import com.example.triagecol.domain.models.dto.StaffMember
import com.example.triagecol.domain.models.ApiResponse
import com.example.triagecol.domain.models.dto.StaffDto
import com.google.gson.Gson
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class StaffRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
) {

    suspend fun getStaff(): APIResult<StaffDto> {
        return try {
            Log.d("prueba", "Obteniendo usuarios")
            val call = retrofit.create(APIServiceStaff::class.java).getStaff()
            val userList = call.body()
            if (call.isSuccessful) {
                APIResult.Success(userList!!)
            } else {
                APIResult.Error(java.lang.Exception("No se obtuvo la lista de usuarios"))
            }
        } catch (e: Exception) {
            APIResult.Error(e)
        }
    }

    suspend fun addStaff(user: StaffMember): APIResult<ApiResponse?> {
        return try {
            Log.d("prueba", "${user}")
            Log.d("prueba", "Agregando nuevo usuario")
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
        } catch (e: Exception) {
            APIResult.Error(java.lang.Exception("Revisa tu conexion a internet."))
        }
    }

    suspend fun editStaff(user: StaffMember, userId: Int): APIResult<ApiResponse?> {
        return try {
            Log.d("prueba", "user: $user, userId: ${userId}")
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
        } catch (e: Exception) {
            APIResult.Error(e)
        }
    }

    suspend fun deleteStaffMember(idUser: String): APIResult<ApiResponse?> {
        return try {
            Log.d("prueba", "Eliminando usuario: $idUser")
            val response: Response<ApiResponse> = retrofit.create(APIServiceStaff::class.java).deleteStaff(idUser)
            if (response.isSuccessful && response.body() != null) {
                APIResult.Success(response.body())
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                APIResult.Error(Exception(errorResponse.message))
            }
        } catch (e: Exception) {
            Log.d("prueba", e.message!!)
            APIResult.Error(e)
        }
    }

    suspend fun deleteAllPatients(): APIResult<ApiResponse?>{
        return try {
            val response: Response<ApiResponse> = retrofit.create(APIServiceStaff::class.java).deleteAllPatients()
            if (response.isSuccessful && response.body() != null) {
                APIResult.Success(response.body())
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                APIResult.Error(Exception(errorResponse.message))
            }
        } catch (e: Exception) {
            APIResult.Error(e)
        }
    }
}