package com.example.triagecol.presentation.supervisor.addPatient

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triagecol.domain.models.dto.AddPatientRequest
import com.example.triagecol.domain.models.APIResult
import com.example.triagecol.domain.usecases.PatientRepository
import com.example.triagecol.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(
    private val patientRepository: PatientRepository
) : ViewModel() {

    private val _idPatient = MutableStateFlow("")
    val idPatient: StateFlow<String> = _idPatient

    private val _patient = MutableStateFlow<AddPatientRequest>(
        AddPatientRequest("", "", "", "", "Femenino", "", "", "")
    )
    val patient: StateFlow<AddPatientRequest> = _patient

    val temperatureError = MutableStateFlow(true)
    val heartRateError = MutableStateFlow(true)
    val bloodOxygenError = MutableStateFlow(true)

    private val _birthDialogShown = MutableStateFlow(false)
    val birthDialogShown: StateFlow<Boolean> = _birthDialogShown

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
    ) {
        _patient.update {
            when (title) {
                PatientData.ID_NUMBER -> {
                    it.copy(idNumber = data)
                }

                PatientData.NAME -> {
                    it.copy(name = data)
                }

                PatientData.LASTNAME -> {
                    it.copy(lastname = data)
                }

                PatientData.AGE -> {
                    it.copy(age = data)
                }

                PatientData.SEX -> {
                    it.copy(sex = data)
                }

                PatientData.TEMPERATURE -> {
                    it.copy(temperature = data)
                }

                PatientData.HEART_RATE -> {
                    it.copy(heartRate = data)
                }

                PatientData.BLOOD_OXYGEN -> {
                    it.copy(bloodOxygen = data)
                }
            }
        }
        _saveEnable.value = saveButtonEnabled()
    }

    fun checkVitalSigns(
        sign: String,
        title: Signs
    ) {
        if (sign.length > 1) {
            val signInt: Int = sign.toInt()
            when (title) {
                Signs.TEMPERATURE -> {
                    temperatureError.update { signInt in 28..43 }
                }

                Signs.HEART_RATE -> {
                    heartRateError.update { signInt in 40..200 }
                }

                Signs.BLOOD_OXYGEN -> {
                    bloodOxygenError.update { signInt in 80..100 }
                }
            }
        }
    }

    private fun saveButtonEnabled(): Boolean {
        val pat = _patient.value
        return pat.idNumber.isNotBlank() &&
                pat.name.isNotBlank() &&
                pat.lastname.isNotBlank() &&
                pat.age.isNotBlank() &&
                pat.temperature.isNotBlank() &&
                pat.heartRate.isNotBlank() &&
                pat.bloodOxygen.isNotBlank() &&
                temperatureError.value &&
                heartRateError.value &&
                bloodOxygenError.value
    }

    fun savePatient() {
        if (!_isSavingData.value) {
            _isSavingData.value = true
            viewModelScope.launch {
                patientRepository.savePatientData(_patient.value).let {
                    when (it) {
                        is APIResult.Success -> {
                            _idPatient.value = it.data.message
                            _isValidData.value = true
                            _error.value = ""
                        }

                        is APIResult.Error -> {
                            _error.value =
                                when (it.exception.message) {
                                    null -> Constants.NULL_ERROR
                                    Constants.TIMEOUT -> Constants.TIMEOUT_ERROR
                                    else -> "${it.exception.message}"
                                }
                            _isValidData.value = false
                        }
                    }
                    _isSavingData.value = false
                }
            }
        }
    }

    fun resetData() {
        _error.value = ""
        _patient.value = AddPatientRequest("", "", "", "", "Femenino", "", "", "")
        _isValidData.value = false
    }

    fun setIsDialogShown(isDialogShown: Boolean) {
        _isDialogShown.value = isDialogShown
    }

    fun setBirthDialogShown(birthDialogShown: Boolean) {
        _birthDialogShown.value = birthDialogShown
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

enum class Signs {
    TEMPERATURE,
    HEART_RATE,
    BLOOD_OXYGEN
}