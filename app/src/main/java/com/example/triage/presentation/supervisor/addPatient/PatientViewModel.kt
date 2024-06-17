package com.example.triage.presentation.supervisor.addPatient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triage.domain.models.dto.AddPatientRequest
import com.example.triage.domain.models.APIResult
import com.example.triage.domain.models.GlobalObjects.PatientRequestInit
import com.example.triage.domain.usecases.PatientRepository
import com.example.triage.utils.Errors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        PatientRequestInit
    )
    val patient: StateFlow<AddPatientRequest> = _patient

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
                PatientData.ID_TYPE -> {
                    it.copy(idType = data)
                }
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

    private fun saveButtonEnabled(): Boolean {
        val pat = _patient.value
        return pat.idNumber.isNotBlank() &&
                pat.name.isNotBlank() &&
                pat.lastname.isNotBlank() &&
                pat.age.isNotBlank() &&
                pat.temperature.isNotBlank() &&
                pat.heartRate.isNotBlank() &&
                pat.bloodOxygen.isNotBlank() &&
                pat.temperature.toFloat() in 28.0..43.0 &&
                pat.heartRate.toInt() in 40..200 &&
                pat.bloodOxygen.toInt() in 80..200
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
                                    null -> Errors.NULL_ERROR
                                    Errors.TIMEOUT -> Errors.TIMEOUT_ERROR
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
        _patient.value = PatientRequestInit
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
    ID_TYPE,
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