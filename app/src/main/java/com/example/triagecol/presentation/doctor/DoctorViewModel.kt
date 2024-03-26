package com.example.triagecol.presentation.doctor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triagecol.domain.models.dto.StaffMemberDto
import com.example.triagecol.domain.usecases.DoctorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorViewModel @Inject constructor(
    private val doctorRepository: DoctorRepository
): ViewModel(){

    private val _areTherePatients = MutableStateFlow(false)
    val areTherePatients: StateFlow<Boolean> = _areTherePatients

    private val _userData = MutableStateFlow(StaffMemberDto(0,"","","","","","","Supervisor"))
    val userData: StateFlow<StaffMemberDto> = _userData

    private val _isFetchingPatients = MutableStateFlow(false)
    val isFetchingPatients: StateFlow<Boolean> = _isFetchingPatients

    fun toggleWaitingPatientsStatus(areTherePatients: Boolean){
        _areTherePatients.value = areTherePatients
    }

    fun refreshPatientList(){
        _isFetchingPatients.value = true
        Log.d("prueba", "Fetching Patients List")
        viewModelScope.launch {

        }
    }

    fun setUserData(userData: StaffMemberDto) {
        _userData.value = userData
    }
}