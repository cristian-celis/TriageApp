package com.example.triagecol.domain.usecases

import android.util.Log
import com.example.triagecol.data.remote.APIService
import com.example.triagecol.domain.models.dto.AddUserDtoItem
import com.example.triagecol.domain.models.dto.ApiResponse
import com.example.triagecol.domain.models.dto.StaffMemberDto
import com.example.triagecol.domain.models.dto.toAddUserDtoItem
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
) {

    suspend fun callToUserList(): APIResult<List<StaffMemberDto>> {
        return try {
            Log.d("prueba", "Obteniendo usuarios")
            val call = retrofit.create(APIService::class.java).getPeople()
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

    suspend fun addUser(user: AddUserDtoItem): APIResult<ApiResponse?> {
        return try {
            Log.d("prueba", "Agregando nuevo usuario")
            val response: Response<ApiResponse> =
                retrofit.create(APIService::class.java).addUser(user)

            if (response.isSuccessful) {
                APIResult.Success(response.body())
            } else {
                Log.d("prueba", "Algo salio mal... IDK")
                APIResult.Error(java.lang.Exception("No se agrego el usuario"))
            }
        } catch (e: Exception) {
            Log.d("prueba", "Excepcion: ${e.message}")
            APIResult.Error(e)
        }
    }

    suspend fun editUser(user: StaffMemberDto): APIResult<ApiResponse?> {
        return try {
            val response: Response<ApiResponse> = retrofit.create(APIService::class.java)
                .editUser(user.id.toString(), user.toAddUserDtoItem())

            if (response.isSuccessful && response.body() != null) {
                APIResult.Success(response.body())
            } else {
                APIResult.Error(java.lang.Exception("No se edito el usuario"))
            }
        } catch (e: Exception) {
            APIResult.Error(e)
        }
    }

    suspend fun deleteUser(idUser: String): APIResult<ApiResponse?> {
        return try {
            Log.d("prueba", "Agregando nuevo usuario")
            val response: Response<ApiResponse> = retrofit.create(APIService::class.java).deleteUser(idUser)

            if (response.isSuccessful && response.body() != null) {
                APIResult.Success(response.body())
            } else {
                APIResult.Error(java.lang.Exception("No se elimino el usuario"))
            }
        } catch (e: Exception) {
            APIResult.Error(e)
        }
    }
}