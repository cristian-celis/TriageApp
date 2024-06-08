package com.example.triagecol.presentation.supervisor.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triagecol.domain.models.APIResult
import com.example.triagecol.domain.models.dto.PatientsDto
import com.example.triagecol.domain.models.dto.StaffMemberDto
import com.example.triagecol.domain.usecases.PatientRepository
import com.example.triagecol.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainSupervisorViewModel @Inject constructor(
    private val patientRepository: PatientRepository
) : ViewModel() {

    private val _idNumber = MutableStateFlow("")
    val idNumber: StateFlow<String> = _idNumber

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

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

    private val _updatingPatientList = MutableStateFlow(false)
    val updatingPatientList: StateFlow<Boolean> = _updatingPatientList

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
                    }

                    is APIResult.Error -> {
                        _successCall.value = false
                        _error.value =
                            when (it.exception.message) {
                                null -> Constants.NULL_ERROR
                                Constants.TIMEOUT -> Constants.TIMEOUT_ERROR
                                else -> "${it.exception.message}"
                            }
                    }
                }
            }
            _fetchingPatients.value = false
        }
    }

    private suspend fun getPatientsWaitingCount() {
        while (_updatingPatientList.value) {
            viewModelScope.launch {
                patientRepository.getPatientsWaitingCount().let {
                    when (it) {
                        is APIResult.Success -> {
                            _successCall.value = true
                            _updatingPatientList.value = _patientList.value.size == it.data
                            _error.value = ""
                        }

                        is APIResult.Error -> {
                            _successCall.value = false
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

    fun getSupervisorData(idSupervisor: String) {
        _fetchingStaffMember.value = true
        viewModelScope.launch {
            Log.d(Constants.TAG, "IdSupervisor: $idSupervisor")
            patientRepository.getSupervisorData(idSupervisor).let { data ->
                when (data) {
                    is APIResult.Success -> {
                        _successCall.value = true
                        _idNumber.value = data.data.idNumber
                        _name.value = "${data.data.name} ${data.data.lastname}"
                        _error.value = ""
                    }

                    is APIResult.Error -> {
                        _successCall.value = false
                        _error.value =
                            when (data.exception.message) {
                                null -> Constants.NULL_ERROR
                                Constants.TIMEOUT -> Constants.TIMEOUT_ERROR
                                else -> "${data.exception.message}"
                            }
                    }
                }
            }
            _fetchingStaffMember.value = false
        }
    }

    fun startUpdatePatientList() {
        _updatingPatientList.value = true
        viewModelScope.launch {
            getPatientsWaitingCount()
        }
    }

    fun stopUpdatingPatientList() {
        _updatingPatientList.value = false
    }

    fun clearUserData() {
        _updatingPatientList.value = false
        _idNumber.value = ""
        _name.value = ""
    }
}