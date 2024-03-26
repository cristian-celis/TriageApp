package com.example.triagecol.domain.usecases

import com.example.triagecol.data.remote.APIServicePatient
import com.example.triagecol.domain.models.dto.AddPatient
import com.example.triagecol.domain.models.dto.ApiResponse
import retrofit2.Retrofit
import javax.inject.Inject

class PatientRepository @Inject constructor(
    private val retrofit: Retrofit
) {

    suspend fun savePatientData(patientData: AddPatient): APIResult<ApiResponse>{
        return try {
            val call = retrofit.create(APIServicePatient::class.java).addPatient(patientData)

            if(call.isSuccessful){
                APIResult.Success(call.body()!!)
            }else{
                APIResult.Error(java.lang.Exception("Algo Fallo"))
            }

        }catch (e: Exception){
            APIResult.Error(e)
        }
    }
}