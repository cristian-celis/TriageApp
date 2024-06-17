package com.example.triage.domain.usecases

import com.example.triage.data.remote.APIServiceDoctor
import com.example.triage.domain.models.APIResult
import com.example.triage.domain.models.ApiResponse
import com.example.triage.domain.models.dto.StaffStatus
import com.example.triage.domain.models.dto.PriorityPatientDto
import com.google.gson.Gson
import retrofit2.Retrofit
import java.net.UnknownHostException
import javax.inject.Inject

class DoctorRepository @Inject constructor(
    private val retrofit: Retrofit
) {

    suspend fun assignPatient(): APIResult<PriorityPatientDto> {
        return try {
            val call = retrofit.create(APIServiceDoctor::class.java).assignPatient()

            if (call.isSuccessful) {
                APIResult.Success(call.body()!!)
            } else {
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

    suspend fun updateDoctorStatus(staffStatus: StaffStatus): APIResult<ApiResponse> {
        return try {
            val call = retrofit.create(APIServiceDoctor::class.java).updateDoctorStatus(staffStatus)

            if (call.isSuccessful) {
                APIResult.Success(call.body()!!)
            } else {
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

    suspend fun getPatientsWaitingCount(): APIResult<Int>{
        return try {
            val call = retrofit.create(APIServiceDoctor::class.java).getPatientsWaitingCount()
            if(call.isSuccessful){
                APIResult.Success(call.body()!!)
            }else{
                val errorBody = call.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                APIResult.Error(Exception(errorResponse.message))
            }
        }catch (e: UnknownHostException){
            APIResult.Error(Exception("Error de conexión: Asegurate de tener acceso a internet"))
        }catch (e:Exception){
            APIResult.Error(e)
        }
    }
}