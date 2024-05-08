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

    private val _updatingDoctorState = MutableStateFlow(false)
    val updatingDoctorState: StateFlow<Boolean> = _updatingDoctorState

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

    private val _patientsWaitingCount = MutableStateFlow<Int>(0)
    val patientsWaitingCount: StateFlow<Int> = _patientsWaitingCount

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private val _updatingPatientList = MutableStateFlow(false)
    val updatingPatientList: StateFlow<Boolean> = _updatingPatientList

    fun setShowDialog(showDialog: Boolean){
        _showDialog.value = showDialog
    }

    fun assignPatient() {
        _isFetchingPatients.value = true
        Log.d(Constants.TAG, "Llamado API asignar paciente")
        viewModelScope.launch {
            doctorRepository.assignPatient().let {
                when (it) {
                    is APIResult.Success -> {
                        Log.d(Constants.TAG, "Llamada Exitosa: ${it.data}")
                        try {
                            _patientData.value = it.data
                            _doctorInConsultation.value = true
                            _isSuccessData.value = true
                            _error.value = ""
                        }catch (e:Exception){
                            _error.value = "Error al intentar setear los resultados del paciente. Paciente obtenido: ${it.data}. Error en cuestion: ${e.message}"
                        }
                        Log.d(Constants.TAG, "4")
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
        _updatingDoctorState.value = true

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
                        _isSuccessData.value = true
                        _error.value = ""
                        _updatingPatientList.value = doctorOnline
                        viewModelScope.launch {
                            if(doctorOnline) getPatientsWaitingCount()
                        }
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
            _updatingDoctorState.value = false
        }
    }

    private suspend fun getPatientsWaitingCount(){
        while(_updatingPatientList.value){
            viewModelScope.launch {
                doctorRepository.getPatientsWaitingCount().let {
                    when(it){
                        is APIResult.Success -> {
                            _isSuccessData.value = true
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
                            _isSuccessData.value = false
                        }
                    }
                }
            }
            delay(120000L)
        }
    }

    fun clearAll(){
        setShowDialog(false)
        _doctorInConsultation.value = false
        _patientData.value = PriorityPatientDto(
            PatientDto(0, "", "", "", "", "", "", "", ""),
            emptyList()
        )
        if(_isDoctorOnline.value) updateDoctorStatus(false)
        _updatingPatientList.value = false
    }

    fun endMedicalConsultation() {
        _updatingPatientList.value = true
        viewModelScope.launch {
            getPatientsWaitingCount()
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