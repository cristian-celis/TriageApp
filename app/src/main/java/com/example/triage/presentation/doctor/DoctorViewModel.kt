package com.example.triage.presentation.doctor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triage.domain.models.APIResult
import com.example.triage.domain.models.GlobalObjects
import com.example.triage.domain.models.GlobalObjects.PatientInit
import com.example.triage.domain.models.dto.StaffStatus
import com.example.triage.domain.models.dto.PriorityPatientDto
import com.example.triage.domain.models.dto.StaffMemberAccount
import com.example.triage.domain.usecases.DoctorRepository
import com.example.triage.domain.usecases.StaffRepositoryImpl
import com.example.triage.utils.Errors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorViewModel @Inject constructor(
    private val doctorRepository: DoctorRepository,
    private val staffRepositoryImpl: StaffRepositoryImpl
) : ViewModel() {

    private val _doctorAccount =
        MutableStateFlow(GlobalObjects.StaffMemberAccount)
    val doctorAccount: StateFlow<StaffMemberAccount> = _doctorAccount

    private val _patientData = MutableStateFlow(
        PriorityPatientDto(
            PatientInit,
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

    private val _showDialogForSignOff = MutableStateFlow(false)
    val showDialogForSignOff: StateFlow<Boolean> = _showDialogForSignOff

    private val _fetchingStaffMember = MutableStateFlow(false)
    val fetchingStaffMember: StateFlow<Boolean> = _fetchingStaffMember

    private val _successCall = MutableStateFlow(false)
    val successCall: StateFlow<Boolean> = _successCall

    fun setDialogForSignOff(showDialog: Boolean){
        _showDialogForSignOff.value = showDialog
    }

    fun assignPatient() {
        _isFetchingPatients.value = true
        viewModelScope.launch {
            doctorRepository.assignPatient().let {
                when (it) {
                    is APIResult.Success -> {
                        _patientData.value = it.data
                        _doctorInConsultation.value = true
                        _successCall.value = true
                        _error.value = ""
                    }
                    is APIResult.Error -> {
                        _error.value =
                            when (it.exception.message) {
                                null -> Errors.NULL_ERROR
                                Errors.TIMEOUT -> Errors.TIMEOUT_ERROR
                                else -> "${it.exception.message}"
                            }
                        _successCall.value = false
                    }
                }
            }
            _isFetchingPatients.value = false
        }
    }

    fun updateDoctorStatus(doctorOnline: Boolean) {
        _updatingDocStatus.value = true

        viewModelScope.launch {
            doctorRepository.updateDoctorStatus(
                StaffStatus(
                    _doctorAccount.value.id,
                    getDoctorStatus(state = doctorOnline)
                )
            ).let {
                when (it) {
                    is APIResult.Success -> {
                        _isDoctorOnline.value = doctorOnline
                        _successCall.value = true
                        _error.value = ""
                        viewModelScope.launch {
                            if (doctorOnline) getPatientsWaitingCount()
                        }
                    }

                    is APIResult.Error -> {
                        _error.value =
                            when (it.exception.message) {
                                null -> Errors.NULL_ERROR
                                Errors.TIMEOUT -> Errors.TIMEOUT_ERROR
                                else -> "${it.exception.message}"
                            }
                        _successCall.value = false
                    }
                }
            }
            _updatingDocStatus.value = false
        }
    }

    private suspend fun getPatientsWaitingCount() {
        while (!_doctorInConsultation.value && _isDoctorOnline.value) {
            viewModelScope.launch {
                doctorRepository.getPatientsWaitingCount().let {
                    when (it) {
                        is APIResult.Success -> {
                            _successCall.value = true
                            _patientsWaitingCount.value = it.data
                            _error.value = ""
                        }
                        is APIResult.Error -> {
                            _error.value =
                                when (it.exception.message) {
                                    null -> Errors.NULL_ERROR
                                    Errors.TIMEOUT -> Errors.TIMEOUT_ERROR
                                    else -> "${it.exception.message}"
                                }
                            _successCall.value = false
                        }
                    }
                }
            }
            delay(120000L)
        }
    }

    fun getDoctorData(idSupervisor: String) {
        _fetchingStaffMember.value = true
        viewModelScope.launch {
            Log.d(Errors.TAG, "IdDoctor: $idSupervisor")
            staffRepositoryImpl.getStaffMember(idSupervisor).let { data ->
                when (data) {
                    is APIResult.Success -> {
                        _successCall.value = true
                        _doctorAccount.value = StaffMemberAccount(
                            data.data.id,
                            data.data.idNumber,
                            "${data.data.name} ${data.data.lastname}",
                            "Desconectado"
                        )
                        _error.value = ""
                    }
                    is APIResult.Error -> {
                        _error.value =
                            when (data.exception.message) {
                                null -> Errors.NULL_ERROR
                                Errors.TIMEOUT -> Errors.TIMEOUT_ERROR
                                else -> "${data.exception.message}"
                            }
                        _successCall.value = false
                    }
                }
            }
            _fetchingStaffMember.value = false
        }
    }

    fun clearError(){
        _error.value = ""
    }

    fun clearAll() {
        setDialogForSignOff(false)
        _doctorInConsultation.value = false
        if (_isDoctorOnline.value) updateDoctorStatus(false)

        _patientData.value = PriorityPatientDto(
            PatientInit,
            emptyList()
        )
        _error.value = ""
        _successCall.value = false
    }

    fun endConsultation() {
        _doctorInConsultation.value = false
        viewModelScope.launch {
            getPatientsWaitingCount()
        }
        _patientData.value = PriorityPatientDto(
            PatientInit,
            emptyList()
        )
    }

    private fun getDoctorStatus(state: Boolean) = if (state) "Conectado" else "Desconectado"
}