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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainSupervisorViewModel @Inject constructor(
    private val patientRepository: PatientRepository
): ViewModel() {

    private val _userData = MutableStateFlow(StaffMemberDto(0,"","","","","","","Supervisor"))
    val userData: StateFlow<StaffMemberDto> = _userData

    private val _patientList = MutableStateFlow<PatientsDto>(PatientsDto())
    val patientList: StateFlow<PatientsDto> = _patientList

    private val _showDialogForSignOff = MutableStateFlow(false)
    val showDialogForSignOff: StateFlow<Boolean> = _showDialogForSignOff

    private val _successCall = MutableStateFlow(false)
    val successCall: StateFlow<Boolean> = _successCall

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _fetchingData = MutableStateFlow(false)
    val fetchingData: StateFlow<Boolean> = _fetchingData

    private val _listUpdated = MutableStateFlow(false)
    val listUpdated: StateFlow<Boolean> = _listUpdated

    //389, 512
    fun updateUserData(userData: StaffMemberDto){
        _userData.value = userData
    }

    fun setDialogForSignOff(showDialog: Boolean){
        _showDialogForSignOff.value = showDialog
    }

    fun getPatientList(){
        _fetchingData.value = true
        viewModelScope.launch {
            patientRepository.getPatientList().let {
                when (it){
                    is APIResult.Success -> {
                        _patientList.value = it.data
                        _successCall.value = true
                        _listUpdated.value = true
                        viewModelScope.launch {
                            getPatientsWaitingCount()
                        }
                    }
                    is APIResult.Error -> {
                        _listUpdated.value = false
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
            _fetchingData.value = false
        }
    }
    private suspend fun getPatientsWaitingCount(){
        while(_listUpdated.value){
            viewModelScope.launch {
                patientRepository.getPatientsWaitingCount().let {
                    when(it){
                        is APIResult.Success -> {
                            _successCall.value = true
                            _listUpdated.value = _patientList.value.size == it.data
                            Log.d(Constants.TAG, "MainSupervisorViewModel -> Lista Espera Actualizada: " +
                                    "${_listUpdated.value} - Se tiene: ${_patientList.value.size} - Llega: ${it.data}")
                            _error.value = ""
                        }
                        is APIResult.Error -> {
                            _listUpdated.value = false
                            _error.value =
                                when (it.exception.message) {
                                    null -> Constants.NULL_ERROR
                                    Constants.TIMEOUT -> Constants.TIMEOUT_ERROR
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


    fun clearUserData(){
        _listUpdated.value = false
        _error.value = "Fuera de Linea."
        _userData.value = StaffMemberDto(0,"","","","","","","Supervisor")
    }
}