package com.example.triagecol.domain.usecases

import android.util.Log
import com.example.triagecol.data.remote.APIServiceDoctor
import com.example.triagecol.data.remote.APIServicePatient
import com.example.triagecol.domain.models.APIResult
import com.example.triagecol.domain.models.dto.AddPatient
import com.example.triagecol.domain.models.dto.AddSymptoms
import com.example.triagecol.domain.models.ApiResponse
import com.example.triagecol.domain.models.dto.PatientsDto
import com.example.triagecol.utils.Constants
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Retrofit
import javax.inject.Inject

class PatientRepository @Inject constructor(
    private val retrofit: Retrofit
) {

    suspend fun savePatientData(patientData: AddPatient): APIResult<ApiResponse> {
        return try {
            val call = retrofit.create(APIServicePatient::class.java).addPatient(patientData)
            if (call.isSuccessful)
                APIResult.Success(call.body()!!)
            else {
                val errorBody = call.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                //APIResult.Error(Exception(errorResponse.message))
                APIResult.Error(Exception("Error Desconocido"))
            }
        } catch (e: HttpException) {
            APIResult.Error(java.lang.Exception("Revisa tu conexion a internet."))
        }catch (e: Exception){
            APIResult.Error(java.lang.Exception("Error de Conexion."))
        }
    }

    suspend fun saveSymptomsPat(
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
                //APIResult.Error(Exception(errorResponse.message))
                APIResult.Error(Exception("Error Desconocido"))
            }
        } catch (e: Exception) {
            Log.d(Constants.TAG, "Error, excepcion: ${e.message}")
            APIResult.Error(Exception("Error de Conexion"))
        }
    }

    suspend fun getPatientList(): APIResult<PatientsDto>{
        return try {
            val call = retrofit.create(APIServicePatient::class.java).getPatientList()

            if(call.isSuccessful){
                APIResult.Success(call.body()!!)
            }else{
                val errorBody = call.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                //APIResult.Error(Exception(errorResponse.message))
                APIResult.Error(Exception("Error Desconocido"))
            }
        }catch (e: Exception){
            APIResult.Error(Exception("Error de conexion"))
        }
    }

    suspend fun getPatientsWaitingCount(): APIResult<Int>{
        return try {
            val call = retrofit.create(APIServiceDoctor::class.java).getPatientsWaitingCount()
            if(call.isSuccessful){
                APIResult.Success(call.body()!!)
            }else{
                APIResult.Error(Exception("Error Desconocido"))
            }
        }catch (e: Exception){
            APIResult.Error(Exception("Error de conexion"))
        }
    }

    suspend fun deletePatient(id: String): APIResult<ApiResponse>{
        return try{
            val call = retrofit.create(APIServicePatient::class.java).deletePatient(id.toInt())
            if(call.isSuccessful){
                APIResult.Success(call.body()!!)
            }else{
                val errorBody = call.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                //APIResult.Error(Exception(errorResponse.message))
                APIResult.Error(Exception("Error Desconocido"))
            }
        }catch (e: HttpException){
            APIResult.Error(java.lang.Exception("Revisa tu conexion a internet."))
        }catch (e: Throwable){
            APIResult.Error(java.lang.Exception("Error de conexion."))
        }
    }
}