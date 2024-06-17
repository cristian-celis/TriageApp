package com.example.triage.presentation.supervisor.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triage.domain.models.APIResult
import com.example.triage.domain.models.GlobalObjects
import com.example.triage.domain.models.dto.PatientsDto
import com.example.triage.domain.models.dto.StaffMemberAccount
import com.example.triage.domain.usecases.PatientRepository
import com.example.triage.domain.usecases.StaffRepositoryImpl
import com.example.triage.utils.Errors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainSupervisorViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    private val staffRepositoryImpl: StaffRepositoryImpl
) : ViewModel() {

    private val _supervisorAccount =
        MutableStateFlow(GlobalObjects.StaffMemberAccount)
    val supervisorAccount: StateFlow<StaffMemberAccount> = _supervisorAccount

    private val _fetchingStaffMember = MutableStateFlow(false)
    val fetchingStaffMember: StateFlow<Boolean> = _fetchingStaffMember

    private val _patientList = MutableStateFlow<PatientsDto>(PatientsDto())
    val patientList: StateFlow<PatientsDto> = _patientList

    private val _showDialogForSignOff = MutableStateFlow(false)
    val showDialogForSignOff: StateFlow<Boolean> = _showDialogForSignOff

    private val _successCall = MutableStateFlow(false)
    val successCall: StateFlow<Boolean> = _successCall

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _fetchingPatients = MutableStateFlow(false)
    val fetchingPatients: StateFlow<Boolean> = _fetchingPatients

    private val _waitListUpdateEnabled = MutableStateFlow(false)
    val waitListUpdateEnabled: StateFlow<Boolean> = _waitListUpdateEnabled

    fun setDialogForSignOff(showDialog: Boolean) {
        _showDialogForSignOff.value = showDialog
    }

    fun getPatientList() {
        _fetchingPatients.value = true
        viewModelScope.launch {
            patientRepository.getPatientList().let {
                when (it) {
                    is APIResult.Success -> {
                        _patientList.value = it.data
                        _successCall.value = true
                        viewModelScope.launch {
                            getPatientsWaitingCount()
                        }
                        _error.value = ""
                    }

                    is APIResult.Error -> {
                        _successCall.value = false
                        _error.value =
                            when (it.exception.message) {
                                null -> Errors.NULL_ERROR
                                Errors.TIMEOUT -> Errors.TIMEOUT_ERROR
                                else -> "${it.exception.message}"
                            }
                    }
                }
            }
            _fetchingPatients.value = false
        }
    }

    private suspend fun getPatientsWaitingCount() {
        while (_waitListUpdateEnabled.value) {
            viewModelScope.launch {
                patientRepository.getPatientsWaitingCount().let {
                    when (it) {
                        is APIResult.Success -> {
                            _successCall.value = true
                            _waitListUpdateEnabled.value = _patientList.value.size == it.data
                            _error.value = ""
                        }

                        is APIResult.Error -> {
                            _successCall.value = false
                            _error.value =
                                when (it.exception.message) {
                                    null -> Errors.NULL_ERROR
                                    Errors.TIMEOUT -> Errors.TIMEOUT_ERROR
                                    else -> "${it.exception.message}"
                                }
                        }
                    }
                }
            }
            delay(120000L)
        }
    }

    fun getSupervisorData(idSupervisor: String) {
        _fetchingStaffMember.value = true
        viewModelScope.launch {
            Log.d(Errors.TAG, "IdSupervisor: $idSupervisor")
            staffRepositoryImpl.getStaffMember(idSupervisor).let { data ->
                when (data) {
                    is APIResult.Success -> {
                        _successCall.value = true
                        _supervisorAccount.value = StaffMemberAccount(
                            data.data.id,
                            data.data.idNumber,
                            "${data.data.name} ${data.data.lastname}",
                            "Desconectado"
                        )
                        _error.value = ""
                    }

                    is APIResult.Error -> {
                        _successCall.value = false
                        _error.value =
                            when (data.exception.message) {
                                null -> Errors.NULL_ERROR
                                Errors.TIMEOUT -> Errors.TIMEOUT_ERROR
                                else -> "${data.exception.message}"
                            }
                    }
                }
            }
            _fetchingStaffMember.value = false
        }
    }

    fun startUpdatePatientList() {
        _waitListUpdateEnabled.value = true
        viewModelScope.launch {
            getPatientsWaitingCount()
        }
    }

    fun stopUpdatingPatientList() {
        _waitListUpdateEnabled.value = false
    }

    fun clearUserData() {
        _waitListUpdateEnabled.value = false
        _supervisorAccount.value = GlobalObjects.StaffMemberAccount
    }
}