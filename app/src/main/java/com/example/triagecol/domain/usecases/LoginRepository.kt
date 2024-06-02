package com.example.triagecol.domain.usecases

import android.net.http.NetworkException
import android.util.Log
import com.example.triagecol.data.remote.APIServiceLogin
import com.example.triagecol.domain.models.APIResult
import com.example.triagecol.domain.models.dto.LoginModel
import com.example.triagecol.domain.models.ApiResponse
import com.example.triagecol.domain.models.dto.UserDto
import com.example.triagecol.utils.Constants
import com.google.gson.Gson
import retrofit2.Retrofit
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
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
                //APIResult.Error(Exception(errorResponse.message))
                APIResult.Error(Exception("Error Desconocido"))
            }
        }catch (e:UnknownHostException){
            APIResult.Error(Exception("Error de conexión: Asegurate de tener acceso a internet"))
        }
        catch (e:Exception){
            APIResult.Error(Exception("Error de Conexion"))
        }
    }
}