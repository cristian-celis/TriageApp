package com.example.triagecol.domain.usecases

import android.util.Log
import com.example.triagecol.data.remote.APIServiceLogin
import com.example.triagecol.domain.models.LoginModel
import com.example.triagecol.domain.models.dto.ApiResponse
import com.example.triagecol.domain.models.dto.StaffMemberDto
import com.example.triagecol.domain.models.dto.UserDto
import retrofit2.Retrofit
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val retrofit: Retrofit
) {

    suspend fun staffLogin(infoLogin: LoginModel): APIResult<StaffMemberDto>{
        return try {
            val call = retrofit.create(APIServiceLogin::class.java).staffLogin(infoLogin)
            val reply = call.body()
            if(call.isSuccessful){
                Log.d("prueba", "uyzna: $reply")
                APIResult.Success(reply!!)
            }else{
                APIResult.Error(java.lang.Exception("Datos incorrectos"))
            }
        }catch (e: Exception){
            APIResult.Error(e)
        }
    }

    suspend fun login(infoLogin: LoginModel): APIResult<UserDto>{
        return try {
            val call = retrofit.create(APIServiceLogin::class.java).login(infoLogin)
            Log.d("prueba", "Resultado Login: $call")
            if(call.isSuccessful){
                APIResult.Success(call.body()!!)
            }else{
                APIResult.Error(java.lang.Exception("Datos incorrectos."))
            }
        }catch (e:Exception){
            APIResult.Error(e)
        }
    }

    suspend fun adminLogin(infoLogin: LoginModel): APIResult<ApiResponse>{
        return try {
            val call = retrofit.create(APIServiceLogin::class.java).adminLogin(infoLogin)
            val reply = call.body()
            Log.d("prueba", "what happend: $call, \n o aqui?: $reply")
            if(call.isSuccessful){
                Log.d("prueba", "uyzna admin: $reply")
                APIResult.Success(reply!!)
            }else{
                APIResult.Error(java.lang.Exception("Datos incorrectos admin"))
            }
        }catch (e: Exception){
            APIResult.Error(e)
        }
    }
}