package com.example.triagecol.presentation.doctor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triagecol.domain.models.APIResult
import com.example.triagecol.domain.models.dto.DoctorStatus
import com.example.triagecol.domain.models.dto.PatientDto
import com.example.triagecol.domain.models.dto.PriorityPatientDto
import com.example.triagecol.domain.models.dto.StaffMemberDto
import com.example.triagecol.domain.usecases.DoctorRepository
import com.example.triagecol.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorViewModel @Inject constructor(
    private val doctorRepository: DoctorRepository
) : ViewModel() {

    private val _doctorData =
        MutableStateFlow(StaffMemberDto(0, "", "", "", "", "", "", "Doctor"))
    val doctorData: StateFlow<StaffMemberDto> = _doctorData

    private val _patientData = MutableStateFlow(
        PriorityPatientDto(
            PatientDto(0, "", "", "", "", "", "", "", ""),
            emptyList()
        )
    )
    val patientData: StateFlow<PriorityPatientDto> = _patientData

    private val _updatingDocStatus = MutableStateFlow(false)
    val updatingDocStatus: StateFlow<Boolean> = _updatingDocStatus

    private val _isFetchingPatients = MutableStateFlow(false)
    val isFetchingPatients: StateFlow<Boolean> = _isFetchingPatients

    private val _doctorInConsultation = MutableStateFlow(false)
    val doctorInConsultation: StateFlow<Boolean> = _doctorInConsultation

    private val _isDoctorOnline = MutableStateFlow(false)
    val isDoctorOnline: StateFlow<Boolean> = _isDoctorOnline

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _patientsWaitingCount = MutableStateFlow<Int>(0)
    val patientsWaitingCount: StateFlow<Int> = _patientsWaitingCount

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private val _updatingPatientList = MutableStateFlow(false)
    val updatingPatientList: StateFlow<Boolean> = _updatingPatientList

    private val _showToastMessage = MutableStateFlow(false)
    val showToastMessage: StateFlow<Boolean> = _showToastMessage

    fun setShowDialog(showDialog: Boolean) {
        _showDialog.value = showDialog
    }

    fun assignPatient() {
        _isFetchingPatients.value = true
        viewModelScope.launch {
            doctorRepository.assignPatient().let {
                when (it) {
                    is APIResult.Success -> {
                        _patientData.value = it.data
                        _doctorInConsultation.value = true
                        _error.value = ""
                    }
                    is APIResult.Error -> {
                        _error.value =
                            when (it.exception.message) {
                                null -> Constants.NULL_ERROR
                                Constants.TIMEOUT -> Constants.TIMEOUT_ERROR
                                else -> "${it.exception.message}"
                            }
                        _showToastMessage.value = true
                    }
                }
            }
            _isFetchingPatients.value = false
        }
    }

    fun setShowToastMessage(){
        _showToastMessage.value = false
    }

    fun updateDoctorStatus(doctorOnline: Boolean) {
        _updatingDocStatus.value = true

        viewModelScope.launch {
            doctorRepository.updateDoctorStatus(
                DoctorStatus(
                    _doctorData.value.id,
                    getDoctorStatus(state = doctorOnline)
                )
            ).let {
                when (it) {
                    is APIResult.Success -> {
                        _isDoctorOnline.value = doctorOnline
                        _error.value = ""
                        _updatingPatientList.value = doctorOnline
                        viewModelScope.launch {
                            if (doctorOnline) getPatientsWaitingCount()
                        }
                    }

                    is APIResult.Error -> {
                        _error.value =
                            when (it.exception.message) {
                                null -> Constants.NULL_ERROR
                                Constants.TIMEOUT -> Constants.TIMEOUT_ERROR
                                else -> "${it.exception.message}"
                            }
                    }
                }
            }
            _updatingDocStatus.value = false
        }
    }

    private suspend fun getPatientsWaitingCount() {
        while (_updatingPatientList.value) {
            viewModelScope.launch {
                doctorRepository.getPatientsWaitingCount().let {
                    when (it) {
                        is APIResult.Success -> {
                            _patientsWaitingCount.value = it.data
                            _error.value = ""
                        }

                        is APIResult.Error -> {
                            _error.value =
                                when (it.exception.message) {
                                    null -> Constants.NULL_ERROR
                                    Constants.TIMEOUT -> Constants.TIMEOUT_ERROR
                                    else -> "${it.exception.message}"
                                }
                        }
                    }
                }
            }
            delay(120000L)
        }
    }

    fun clearAll() {
        setShowDialog(false)
        _doctorInConsultation.value = false
        _patientData.value = PriorityPatientDto(
            PatientDto(0, "", "", "", "", "", "", "", ""),
            emptyList()
        )
        if (_isDoctorOnline.value) updateDoctorStatus(false)
        _updatingPatientList.value = false
    }

    fun endConsultation() {
        _updatingPatientList.value = true
        viewModelScope.launch {
            getPatientsWaitingCount().let {
                if (_patientsWaitingCount.value == 0)
                    _showToastMessage.value = true
            }
        }
        _doctorInConsultation.value = false
        _patientData.value = PriorityPatientDto(
            PatientDto(0, "", "", "", "", "", "", "", ""),
            emptyList()
        )
    }

    private fun getDoctorStatus(state: Boolean) = if (state) "Conectado" else "Desconectado"

    fun setDoctorData(userData: StaffMemberDto) {
        _doctorData.value = userData
    }
}