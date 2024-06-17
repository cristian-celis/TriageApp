package com.example.triage.domain.usecases

import android.util.Log
import com.example.triage.data.remote.APIServiceMain
import com.example.triage.domain.models.APIResult
import com.example.triage.utils.Errors
import retrofit2.Retrofit
import java.net.UnknownHostException
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val retrofit: Retrofit
) {

    suspend fun initApi() {
        try {
            Log.d(Errors.TAG, "Iniciando Back")
            retrofit.create(APIServiceMain::class.java).initBack()
        }catch (e:UnknownHostException){
            APIResult.Error(Exception("Error de conexión: Asegurate de tener acceso a internet"))
        }catch (e:Exception){
            APIResult.Error(e)
        }
    }
}