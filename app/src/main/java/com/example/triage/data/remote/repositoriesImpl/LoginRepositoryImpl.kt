package com.example.triage.data.remote.repositoriesImpl

import com.example.triage.data.remote.apiServices.APIServiceLogin
import com.example.triage.domain.models.APIResult
import com.example.triage.domain.models.dto.LoginModel
import com.example.triage.domain.models.ApiResponse
import com.example.triage.domain.models.dto.UserDto
import com.example.triage.domain.repository.LoginRepository
import com.google.gson.Gson
import retrofit2.Retrofit
import java.net.UnknownHostException
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
): LoginRepository {

    override suspend fun login(infoLogin: LoginModel): APIResult<UserDto> {
        return try {
            val call = retrofit.create(APIServiceLogin::class.java).login(infoLogin)
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
}