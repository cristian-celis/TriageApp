package com.example.triagecol.domain.usecases

import android.util.Log
import com.example.triagecol.data.remote.APIServiceLogin
import com.example.triagecol.domain.models.APIResult
import com.example.triagecol.domain.models.dto.LoginModel
import com.example.triagecol.domain.models.ApiResponse
import com.example.triagecol.domain.models.dto.UserDto
import com.google.gson.Gson
import retrofit2.Retrofit
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val retrofit: Retrofit
) {

    suspend fun login(infoLogin: LoginModel): APIResult<UserDto> {
        return try {
            val call = retrofit.create(APIServiceLogin::class.java).login(infoLogin)
            Log.d("prueba", "Resultado Login: $call")
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