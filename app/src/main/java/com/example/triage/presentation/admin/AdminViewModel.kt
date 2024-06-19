package com.example.triage.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triage.domain.usecases.StaffRepositoryImpl
import com.example.triage.domain.models.dto.StaffDto
import com.example.triage.domain.models.dto.StaffMemberDto
import com.example.triage.domain.models.APIResult
import com.example.triage.domain.models.GlobalObjects.StaffMemberInit
import com.example.triage.domain.models.dto.ReportsDto
import com.example.triage.domain.models.dto.ReportsRequest
import com.example.triage.utils.Errors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val staffRepositoryImpl: StaffRepositoryImpl
) : ViewModel() {

    private val _fetchingData = MutableStateFlow(false)
    val fetchingData: StateFlow<Boolean> = _fetchingData

    private val _fetchingStaffCount = MutableStateFlow(false)
    val fetchingStaffCount: StateFlow<Boolean> = _fetchingStaffCount

    private val _userList = MutableStateFlow<List<StaffMemberDto>>(StaffDto())
    val userList: StateFlow<List<StaffMemberDto>> = _userList

    private val _showReportDialog = MutableStateFlow(false)
    val showReportDialog: StateFlow<Boolean> = _showReportDialog

    private val _reportResult = MutableStateFlow<ReportsDto>(ReportsDto(0,"0",0,0,0,0,0))
    val reportResult: StateFlow<ReportsDto> = _reportResult

    private val _successReportCall = MutableStateFlow(false)
    val successReportCall: StateFlow<Boolean> = _successReportCall

    private val _fetchingReport = MutableStateFlow(false)
    val fetchingReport: StateFlow<Boolean> = _fetchingReport

    private val _reportDate = MutableStateFlow(ReportsRequest(2024,6,16))
    val reportDate: StateFlow<ReportsRequest> = _reportDate

    private val _showCalendar = MutableStateFlow(false)
    val showCalendar: StateFlow<Boolean> = _showCalendar

    private var staffMemberData = StaffMemberInit

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _successCall = MutableStateFlow(false)
    val successCall: StateFlow<Boolean> = _successCall

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    fun setDialog(showDialog: Boolean){
        _showDialog.value = showDialog
    }

    fun setReportDialog(showDialog: Boolean){
        _showReportDialog.value = showDialog
    }

    fun getUserList() {
        _fetchingData.value = true
        viewModelScope.launch {
            staffRepositoryImpl.getStaff().let {
                when (it) {
                    is APIResult.Success -> {
                        _successCall.value = true
                        _userList.value = it.data
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
            _fetchingData.value = false
        }
    }

    fun getCountStaff(){
        _fetchingStaffCount.value = true
        viewModelScope.launch {
            staffRepositoryImpl.getStaffCount().let {
                when(it){
                    is APIResult.Success -> {
                        if(it.data != _userList.value.size){
                            getUserList()
                        }
                        _successCall.value = true
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
            _fetchingStaffCount.value = false
        }
    }

    private fun getReport(){
        _fetchingReport.value = true
        viewModelScope.launch {
            staffRepositoryImpl.getReports(_reportDate.value).let {
                when(it){
                    is APIResult.Success -> {
                        _reportResult.value = it.data
                        _successReportCall.value = true
                        _error.value = ""
                    }
                    is APIResult.Error -> {
                        _error.value =
                            when (it.exception.message) {
                                null -> Errors.NULL_ERROR
                                Errors.TIMEOUT -> Errors.TIMEOUT_ERROR
                                else -> "${it.exception.message}"
                            }
                        _successReportCall.value = false
                    }
                }
            }
            _fetchingReport.value = false
        }
    }

    fun setReportDate(year: Int, month: Int, day: Int){
        _reportDate.value = ReportsRequest(year, month, day)
        _reportResult.value = ReportsDto(0,"0",0,0,0,0,0)
        getReport()
    }

    fun clearError() {
        _error.value = ""
    }

    fun setShowCalendar(showCalendar: Boolean){
        _showCalendar.value = showCalendar
    }

    fun setUserDetails(userData: StaffMemberDto) {
        this.staffMemberData = userData
    }
}