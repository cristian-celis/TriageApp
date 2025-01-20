package com.example.triage.data.remote.repositoriesImpl

import com.example.triage.data.remote.apiServices.APIServiceMain
import com.example.triage.domain.models.APIResult
import com.example.triage.domain.repository.MainRepository
import retrofit2.Retrofit
import java.net.UnknownHostException
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
): MainRepository {

    override suspend fun initApi() {
        try {
            retrofit.create(APIServiceMain::class.java).initBack()
        }catch (e:UnknownHostException){
            APIResult.Error(Exception("Error de conexión: Asegurate de tener acceso a internet"))
        }catch (e:Exception){
            APIResult.Error(e)
        }
    }
}