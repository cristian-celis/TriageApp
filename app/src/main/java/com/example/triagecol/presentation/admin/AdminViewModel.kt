package com.example.triagecol.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triagecol.domain.usecases.StaffRepositoryImpl
import com.example.triagecol.domain.models.dto.StaffDto
import com.example.triagecol.domain.models.dto.StaffMemberDto
import com.example.triagecol.domain.models.APIResult
import com.example.triagecol.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val staffRepositoryImpl: StaffRepositoryImpl
) : ViewModel() {

    private val _fetchingData = MutableStateFlow(true)
    val fetchingData: StateFlow<Boolean> = _fetchingData

    private val _userList = MutableStateFlow<List<StaffMemberDto>>(StaffDto())
    val userList: StateFlow<List<StaffMemberDto>> = _userList

    private val _userData =
        MutableStateFlow(StaffMemberDto(0, "", "", "", "", "", "", ""))
    val userData: StateFlow<StaffMemberDto> = _userData

    private val _clickOnAddButton = MutableStateFlow(false)
    val clickOnAddButton: StateFlow<Boolean> = _clickOnAddButton

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _successCall = MutableStateFlow(false)
    val successCall: StateFlow<Boolean> = _successCall

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    fun setDialog(showDialog: Boolean){
        _showDialog.value = showDialog
    }

    fun getUserList() {
        _fetchingData.value = true
        viewModelScope.launch {
            staffRepositoryImpl.getStaff().let {
                when (it) {
                    is APIResult.Success -> {
                        _successCall.value = true
                        _userList.value = it.data
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

    fun deleteAllPatients() {
        _fetchingData.value = true
        viewModelScope.launch {
            staffRepositoryImpl.deleteAllPatients().let {
                when (it) {
                    is APIResult.Success -> {
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

    fun clearError() {
        _error.value = ""
    }

    fun setClickOnAddButton(isClicked: Boolean) {
        _clickOnAddButton.value = isClicked
    }

    fun setUserDataDetails(userData: StaffMemberDto) {
        _userData.value = userData
    }
}