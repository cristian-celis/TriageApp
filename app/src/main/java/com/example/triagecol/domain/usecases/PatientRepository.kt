package com.example.triagecol.domain.usecases

import android.util.Log
import com.example.triagecol.data.remote.APIServicePatient
import com.example.triagecol.domain.models.APIResult
import com.example.triagecol.domain.models.PatientSymptomsModel
import com.example.triagecol.domain.models.dto.AddPatient
import com.example.triagecol.domain.models.dto.ApiResponse
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Retrofit
import javax.inject.Inject

class PatientRepository @Inject constructor(
    private val retrofit: Retrofit
) {

    suspend fun savePatientData(patientData: AddPatient): APIResult<ApiResponse> {
        return try {
            Log.d("prueba", "Guardando Informacion Basica del Paciente: $patientData")
            val call = retrofit.create(APIServicePatient::class.java).addPatient(patientData)

            if (call.isSuccessful)
                APIResult.Success(call.body()!!)
            else {
                val errorBody = call.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                APIResult.Error(Exception(errorResponse.message))
            }

        } catch (e: HttpException) {
            APIResult.Error(java.lang.Exception("Revisa tu conexion a internet."))
        }catch (e: Throwable){
            APIResult.Error(java.lang.Exception("idk men, otro tipo de error"))
        }
    }

    suspend fun saveSymptomsPat(
        idNumberPat: String,
        symptomsList: ArrayList<Int>
    ): APIResult<ApiResponse> {
        return try {
            val patient = PatientSymptomsModel(idNumberPat.toInt(), symptomsList)
            val call = retrofit.create(APIServicePatient::class.java).addPatientSymptoms(patient)
            Log.d("prueba", "Que paso?: ${call.body()}")
            if (call.isSuccessful) {
                APIResult.Success(call.body()!!)
            } else {
                val errorBody = call.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                APIResult.Error(Exception(errorResponse.message))
            }
        } catch (e: Exception) {
            APIResult.Error(e)
        }
    }
}