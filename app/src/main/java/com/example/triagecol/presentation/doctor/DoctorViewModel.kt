package com.example.triagecol.presentation.doctor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triagecol.domain.models.APIResult
import com.example.triagecol.domain.models.dto.DoctorStatus
import com.example.triagecol.domain.models.dto.PatientDto
import com.example.triagecol.domain.models.dto.PatientsDto
import com.example.triagecol.domain.models.dto.PriorityPatientDto
import com.example.triagecol.domain.models.dto.StaffMemberDto
import com.example.triagecol.domain.usecases.DoctorRepository
import com.example.triagecol.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorViewModel @Inject constructor(
    private val doctorRepository: DoctorRepository
) : ViewModel() {

    private val _doctorData =
        MutableStateFlow(StaffMemberDto(0, "", "", "", "", "", "", "Supervisor"))
    val doctorData: StateFlow<StaffMemberDto> = _doctorData

    private val _patientData = MutableStateFlow(
        PriorityPatientDto(
            PatientDto(0, "", "", "", "", "", "", "", ""),
            patientSymptoms()
        )
    )
    val patientData: StateFlow<PriorityPatientDto> = _patientData

    private val _changingDoctorState = MutableStateFlow(false)
    val changingDoctorState: StateFlow<Boolean> = _changingDoctorState

    private val _isFetchingPatients = MutableStateFlow(false)
    val isFetchingPatients: StateFlow<Boolean> = _isFetchingPatients

    private val _doctorInConsultation = MutableStateFlow(false)
    val doctorInConsultation: StateFlow<Boolean> = _doctorInConsultation

    private val _isDoctorOnline = MutableStateFlow(false)
    val isDoctorOnline: StateFlow<Boolean> = _isDoctorOnline

    private val _isSuccessData = MutableStateFlow(false)
    val isSuccessData: StateFlow<Boolean> = _isSuccessData

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    fun assignPatient() {
        _isFetchingPatients.value = true
        Log.d(Constants.TAG, "Llamado API asignar paciente")
        viewModelScope.launch {
            doctorRepository.assignPatient().let {
                when (it) {
                    is APIResult.Success -> {
                        Log.d(Constants.TAG, "Llamada Exitosa: ${it.data}")
                        _patientData.value = it.data
                        _doctorInConsultation.value = true
                        _isSuccessData.value = true
                        _error.value = ""
                    }

                    is APIResult.Error -> {
                        _error.value =
                            when (it.exception.message) {
                                null -> Constants.NULL_ERROR
                                Constants.TIMEOUT -> Constants.TIMEOUT_ERROR
                                else -> "${it.exception.message}"
                            }
                        _isSuccessData.value = false
                    }
                }
            }
            _isFetchingPatients.value = false
        }
    }

    fun updateDoctorStatus(doctorOnline: Boolean) {
        _changingDoctorState.value = true

        viewModelScope.launch {
            doctorRepository.updateDoctorStatus(
                DoctorStatus(
                    _doctorData.value.id,
                    getDoctorStatus()
                )
            ).let {
                when (it) {
                    is APIResult.Success -> {
                        _isDoctorOnline.value = doctorOnline
                        _isSuccessData.value = true
                        _error.value = ""
                    }

                    is APIResult.Error -> {
                        _error.value =
                            when (it.exception.message) {
                                null -> Constants.NULL_ERROR
                                Constants.TIMEOUT -> Constants.TIMEOUT_ERROR
                                else -> "${it.exception.message}"
                            }
                        _isSuccessData.value = false
                    }
                }
            }
            _changingDoctorState.value = false
        }
    }

    fun endConsultation() {
        _doctorInConsultation.value = false
        _patientData.value = PriorityPatientDto(
            PatientDto(0, "", "", "", "", "", "", "", ""),
            emptyList()
        )
    }

    fun setDoctorInConsultation(doctorInConsultation: Boolean) {
        _doctorInConsultation.value = doctorInConsultation
    }

    private fun getDoctorStatus(): String {
        return if (_isDoctorOnline.value) "Conectado" else "Desconectado"
    }

    fun setDoctorData(userData: StaffMemberDto) {
        Log.d(Constants.TAG, "$userData")
        _doctorData.value = userData
    }
}