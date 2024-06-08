package com.example.triagecol.domain.usecases

import android.util.Log
import com.example.triagecol.data.remote.APIServiceMain
import com.example.triagecol.domain.models.APIResult
import com.example.triagecol.utils.Constants
import retrofit2.Retrofit
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val retrofit: Retrofit
) {

    suspend fun initApi() {
        try {
            Log.d(Constants.TAG, "Iniciando Back")
            retrofit.create(APIServiceMain::class.java).initBack()
        }catch (e:UnknownHostException){
            APIResult.Error(Exception("Error de conexión: Asegurate de tener acceso a internet"))
        }catch (e:Exception){
            APIResult.Error(e)
        }
    }
}