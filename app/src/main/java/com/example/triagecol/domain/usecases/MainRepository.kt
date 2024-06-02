package com.example.triagecol.domain.usecases

import android.util.Log
import com.example.triagecol.data.remote.APIServiceMain
import com.example.triagecol.domain.models.APIResult
import com.example.triagecol.utils.Constants
import retrofit2.Retrofit
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val retrofit: Retrofit
) {

    suspend fun initApi() {
        try {
            Log.d(Constants.TAG, "Iniciando Back")
            retrofit.create(APIServiceMain::class.java).initBack()
        } catch (e: Exception) {
            APIResult.Error(Exception("Error de Conexion"))
        }
    }
}