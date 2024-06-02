package com.example.triagecol.presentation.supervisor.addPatient

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triagecol.domain.models.dto.AddPatient
import com.example.triagecol.domain.models.dto.StaffMemberDto
import com.example.triagecol.domain.models.APIResult
import com.example.triagecol.domain.usecases.PatientRepository
import com.example.triagecol.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(
    private val patientRepository: PatientRepository
): ViewModel() {

    private val _id = MutableStateFlow("")
    val id: StateFlow<String> = _id

    private val _patient = MutableStateFlow<AddPatient>(
        AddPatient("", "","","","Femenino","","","")
    )
    val patient: StateFlow<AddPatient> = _patient

    private val _saveEnable = MutableStateFlow<Boolean>(false)
    val saveEnable: StateFlow<Boolean> = _saveEnable

    private val _isSavingData = MutableStateFlow(false)
    val isSavingData: StateFlow<Boolean> = _isSavingData

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _isValidData = MutableStateFlow(false)
    val isDataValid: StateFlow<Boolean> = _isValidData

    private val _isDialogShown = MutableStateFlow(false)
    val isDialogShown: StateFlow<Boolean> = _isDialogShown

    fun updatePatientData(
        data: String,
        title: PatientData
    ){
        _patient.update {
            when(title){
                PatientData.ID_NUMBER -> {it.copy(idNumber = data) }
                PatientData.NAME -> {it.copy(name = data)}
                PatientData.LASTNAME -> {it.copy(lastname = data)}
                PatientData.AGE -> {it.copy(age = data)}
                PatientData.SEX -> {it.copy(sex = data)}
                PatientData.TEMPERATURE -> {it.copy(temperature = data)}
                PatientData.HEART_RATE -> {it.copy(heartRate = data)}
                PatientData.BLOOD_OXYGEN -> {it.copy(bloodOxygen = data)}
            }
        }
        _saveEnable.value = saveButtonEnabled()
    }

    private fun saveButtonEnabled(): Boolean{
        val pat = _patient.value
        return pat.idNumber.isNotBlank() &&
                pat.name.isNotBlank() &&
                pat.lastname.isNotBlank() &&
                pat.age.isNotBlank() &&
                pat.sex.isNotBlank() &&
                pat.temperature.isNotBlank() &&
                pat.heartRate.isNotBlank() &&
                pat.bloodOxygen.isNotBlank()
    }

    fun savePatient(){
        if(!_isSavingData.value){
            _isSavingData.value = true
            viewModelScope.launch {
                patientRepository.savePatientData(_patient.value).let {
                    when(it){
                        is APIResult.Success -> {
                            _id.value = it.data.message
                            _isValidData.value = true
                            _error.value = ""
                        }
                        is APIResult.Error -> {
                            _error.value =
                                when(it.exception.message){
                                    null -> Constants.NULL_ERROR
                                    Constants.TIMEOUT -> Constants.TIMEOUT_ERROR
                                    else -> "${it.exception.message}"
                                }
                            Log.d(Constants.TAG, _error.value)
                            _isValidData.value = false
                        }
                    }
                    _isSavingData.value = false
                }
            }
        }
    }
    fun resetData(){
        _error.value = ""
        _patient.value = AddPatient("", "","","","Femenino","","","")
        _isValidData.value = false
    }

    fun setIsDialogShown(isDialogShown: Boolean){
        _isDialogShown.value = isDialogShown
    }
}

enum class PatientData {
    ID_NUMBER,
    NAME,
    LASTNAME,
    AGE,
    SEX,
    TEMPERATURE,
    HEART_RATE,
    BLOOD_OXYGEN
}