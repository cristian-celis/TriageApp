package com.example.triage.data.remote.repositoriesImpl

import android.util.Log
import com.example.triage.data.remote.apiServices.APIServiceDoctor
import com.example.triage.data.remote.apiServices.APIServicePatient
import com.example.triage.domain.models.APIResult
import com.example.triage.domain.models.dto.AddPatientRequest
import com.example.triage.domain.models.dto.AddSymptoms
import com.example.triage.domain.models.ApiResponse
import com.example.triage.domain.models.dto.PatientsDto
import com.example.triage.domain.repository.PatientRepository
import com.example.triage.utils.Errors
import com.google.gson.Gson
import retrofit2.Retrofit
import java.net.UnknownHostException
import javax.inject.Inject

class PatientRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
): PatientRepository {

    override suspend fun savePatientData(patientData: AddPatientRequest): APIResult<ApiResponse> {
        return try {
            val call = retrofit.create(APIServicePatient::class.java).addPatient(patientData)
            if (call.isSuccessful)
                APIResult.Success(call.body()!!)
            else {
                val errorBody = call.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                APIResult.Error(Exception(errorResponse.message))
            }
        }catch (e: Exception){
            APIResult.Error(e)
        }
    }

    override suspend fun saveSymptomsPat(
        idNumberPat: String,
        symptomsList: ArrayList<Int>,
        pregnancy: Boolean,
        observations: String
    ): APIResult<ApiResponse> {
        return try {
            val patient = AddSymptoms(idNumberPat.toInt(), symptomsList, pregnancy, observations)
            val call = retrofit.create(APIServicePatient::class.java).addPatientSymptoms(patient)
            if (call.isSuccessful) {
                APIResult.Success(call.body()!!)
            } else {
                val errorBody = call.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                Log.d(Errors.TAG, "ERROR: ${errorResponse.message}")
                APIResult.Error(Exception(errorResponse.message))
            }
        }catch (e: Exception) {
            APIResult.Error(e)
        }
    }

    override suspend fun getPatientList(): APIResult<PatientsDto>{
        return try {
            val call = retrofit.create(APIServicePatient::class.java).getPatientList()

            if(call.isSuccessful){
                APIResult.Success(call.body()!!)
            }else{
                val errorBody = call.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                APIResult.Error(Exception(errorResponse.message))
            }
        }catch (e: Exception){
            APIResult.Error(e)
        }
    }

    override suspend fun getPatientsWaitingCount(): APIResult<Int>{
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
        }catch (e: Exception){
            APIResult.Error(e)
        }
    }

    override suspend fun deletePatient(id: String): APIResult<ApiResponse>{
        return try{
            val call = retrofit.create(APIServicePatient::class.java).deletePatient(id.toInt())
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