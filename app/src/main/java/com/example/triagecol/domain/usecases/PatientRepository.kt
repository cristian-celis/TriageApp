package com.example.triagecol.domain.usecases

import android.util.Log
import com.example.triagecol.data.remote.APIServiceDoctor
import com.example.triagecol.data.remote.APIServicePatient
import com.example.triagecol.domain.models.APIResult
import com.example.triagecol.domain.models.dto.AddPatient
import com.example.triagecol.domain.models.dto.AddSymptoms
import com.example.triagecol.domain.models.ApiResponse
import com.example.triagecol.domain.models.dto.PatientsDto
import com.example.triagecol.domain.models.dto.StaffMemberDto
import com.example.triagecol.utils.Constants
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
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
                APIResult.Error(Exception(errorResponse.message))
            }
        }catch (e: TimeoutException){
            APIResult.Error(Exception(Constants.TIMEOUT_ERROR))
        } catch (e: HttpException) {
            APIResult.Error(java.lang.Exception("Revisa tu conexion a internet."))
        }catch (e: Exception){
            APIResult.Error(Exception("Error de conexion: ${e.message}"))
        }
    }

    suspend fun saveSymptomsPat(
        idNumberPat: String,
        symptomsList: ArrayList<Int>,
        pregnancy: Boolean,
        observations: String
    ): APIResult<ApiResponse> {
        return try {
            Log.d(Constants.TAG, "Informacion -> \n idNumber: $idNumberPat" +
                    "\n symptomsList: $symptomsList" +
                    "\n pregnancy: $pregnancy" +
                    "\n observations: $observations")
            val patient = AddSymptoms(idNumberPat.toInt(), symptomsList, pregnancy, observations)
            val call = retrofit.create(APIServicePatient::class.java).addPatientSymptoms(patient)
            if (call.isSuccessful) {
                APIResult.Success(call.body()!!)
            } else {
                val errorBody = call.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                Log.d(Constants.TAG, "ERROR: ${errorResponse.message}")
                APIResult.Error(Exception(errorResponse.message))
            }
        }catch (e: TimeoutException){
            APIResult.Error(Exception(Constants.TIMEOUT_ERROR))
        } catch (e: Exception) {
            APIResult.Error(Exception("Error de conexion: ${e.message}"))
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
                APIResult.Error(Exception(errorResponse.message))
            }
        }catch (e: TimeoutException){
            APIResult.Error(Exception(Constants.TIMEOUT_ERROR))
        } catch (e: IOException) {
            APIResult.Error(Exception(Constants.IO_EXCEPTION))
        } catch (e: Exception){
            APIResult.Error(Exception("Error de conexion: ${e.message}"))
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
        }catch (e: TimeoutException){
            APIResult.Error(Exception(Constants.TIMEOUT_ERROR))
        }catch (e: Exception){
            APIResult.Error(Exception("Error de conexion: ${e.message}"))
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
                APIResult.Error(Exception(errorResponse.message))
            }
        }catch (e: UnknownHostException){
            APIResult.Error(Exception("Error de conexión: Asegurate de tener acceso a internet"))
        }catch (e:Exception){
            APIResult.Error(e)
        }
    }

    suspend fun getSupervisorData(id: String): APIResult<StaffMemberDto>{
        return try{
            val call = retrofit.create(APIServicePatient::class.java).getStaffMember(id.toInt())
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