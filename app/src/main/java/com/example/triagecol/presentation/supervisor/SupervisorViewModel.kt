package com.example.triagecol.presentation.supervisor

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupervisorViewModel @Inject constructor(
    private val patientRepository: PatientRepository
): ViewModel() {

    private val _userData = MutableStateFlow(StaffMemberDto(0,"","","","","","","Supervisor"))
    val userData: StateFlow<StaffMemberDto> = _userData

    private val _id = MutableStateFlow("")
    val id: StateFlow<String> = _id

    private val _idNumber = MutableStateFlow<String>("")
    val idNumber: StateFlow<String> = _idNumber

    private val _name = MutableStateFlow<String>("")
    val name: StateFlow<String> = _name

    private val _lastname = MutableStateFlow<String>("")
    val lastname: StateFlow<String> = _lastname

    private val _age = MutableStateFlow("")
    val age: StateFlow<String> = _age

    private val _gender = MutableStateFlow<String>("")
    val gender: StateFlow<String> = _gender

    private val _temperature = MutableStateFlow("")
    val temperature: StateFlow<String> = _temperature

    private val _heartRate = MutableStateFlow("")
    val heartRate: StateFlow<String> = _heartRate

    private val _bloodOxygen = MutableStateFlow("")
    val bloodOxygen: StateFlow<String> = _bloodOxygen

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

    private val _showDialogForSignOff = MutableStateFlow(false)
    val showDialogForSignOff: StateFlow<Boolean> = _showDialogForSignOff

    fun setDialogForSignOff(showDialog: Boolean){
        _showDialogForSignOff.value = showDialog
    }

    fun updateUserData(
        idNumber: String,
        name: String,
        lastname: String,
        age: String
    ) {
        _idNumber.value = idNumber
        _name.value = name
        _lastname.value = lastname
        _age.value = age
        _saveEnable.value = checkDataEntered()
    }

    fun setGender(gender: String){
        _gender.value = gender
    }

    fun updateVitalSigns(
        temperature: String,
        heartRate: String,
        bloodOxygen: String
    ){
        _temperature.value = temperature
        _heartRate.value = heartRate
        _bloodOxygen.value = bloodOxygen
        _saveEnable.value = checkDataEntered()
    }

    fun savePatient(){
        if(!_isSavingData.value){
            _isSavingData.value = true
            val patient = AddPatient(
                _idNumber.value, _name.value,
                _lastname.value, _age.value,
                _gender.value, _temperature.value,
                _heartRate.value, _bloodOxygen.value)
            viewModelScope.launch {
                patientRepository.savePatientData(patient).let {
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

    private fun checkDataEntered(): Boolean{
        return _idNumber.value.isNotBlank()
            && _name.value.isNotBlank()
            && _lastname.value.isNotBlank()
            && _age.value.isNotBlank()
            && _temperature.value.isNotBlank()
            && _heartRate.value.isNotBlank()
            && _bloodOxygen.value.isNotBlank()
    }

    fun setUserData(userData: StaffMemberDto){
        _userData.value = userData
    }

    fun resetData(){
        _error.value = ""
        _isValidData.value = false

        _idNumber.value = ""
        _name.value = ""
        _lastname.value = ""
        _age.value = ""
        _gender.value = ""
        _temperature.value = ""
        _heartRate.value = ""
        _bloodOxygen.value = ""
        _isValidData.value = false
    }

    fun setIsDialogShown(isDialogShown: Boolean){
        _isDialogShown.value = isDialogShown
    }
}