package com.example.triagecol.presentation.supervisor.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triagecol.domain.models.APIResult
import com.example.triagecol.domain.models.dto.PatientsDto
import com.example.triagecol.domain.models.dto.StaffMemberDto
import com.example.triagecol.domain.usecases.PatientRepository
import com.example.triagecol.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _patientListUpdated = MutableStateFlow(false)
    val patientListUpdated: StateFlow<Boolean> = _patientListUpdated

    fun updateUserData(userData: StaffMemberDto){
        _userData.value = userData
    }

    fun setDialogForSignOff(showDialog: Boolean){
        _showDialogForSignOff.value = showDialog
    }

    fun setPatientListUpdated(newValue: Boolean){
        _patientListUpdated.value = newValue
    }

    fun getPatientList(){
        _fetchingData.value = true
        viewModelScope.launch {
            patientRepository.getPatientList().let {
                when (it){
                    is APIResult.Success -> {
                        _patientList.value = it.data
                        _patientListUpdated.value = true
                        _successCall.value = true
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
            _fetchingData.value = false
        }
    }

    fun clearUserData(){
        _userData.value = StaffMemberDto(0,"","","","","","","Supervisor")
    }
}