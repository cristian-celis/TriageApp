package com.example.triagecol.domain.usecases

import android.util.Log
import com.example.triagecol.data.remote.APIServiceLogin
import com.example.triagecol.data.remote.APIServiceMain
import com.example.triagecol.domain.models.APIResult
import com.example.triagecol.domain.models.dto.ApiResponse
import com.example.triagecol.utils.Constants
import com.google.gson.Gson
import retrofit2.Retrofit
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val retrofit: Retrofit
) {

    suspend fun initBack() {
        try {
            Log.d(Constants.TAG, "Iniciando Back")
            retrofit.create(APIServiceMain::class.java).initBack()
        } catch (e: Exception) {
            APIResult.Error(e)
        }
    }
}