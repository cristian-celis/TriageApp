package com.example.triage.presentation.admin.detailsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triage.domain.usecases.StaffRepositoryImpl
import com.example.triage.domain.models.dto.StaffMember
import com.example.triage.domain.models.dto.StaffMemberDto
import com.example.triage.domain.models.APIResult
import com.example.triage.domain.models.GlobalObjects
import com.example.triage.utils.Errors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailCardViewModel @Inject constructor(
    private val staffRepositoryImpl: StaffRepositoryImpl
) : ViewModel() {

    private val _staffMemberData = MutableStateFlow(GlobalObjects.StaffMemberInit)
    val staffMemberData: StateFlow<StaffMemberDto> = _staffMemberData

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _lastname = MutableStateFlow<String>("")
    val lastname: StateFlow<String> = _lastname

    private val _idNumber = MutableStateFlow<String>("")
    val idNumber: StateFlow<String> = _idNumber

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _role = MutableStateFlow("Medico")
    val role: StateFlow<String> = _role

    private val _fetchingStaffMember = MutableStateFlow(false)
    val fetchingStaffMember: StateFlow<Boolean> = _fetchingStaffMember

    private val _saveEnable = MutableStateFlow<Boolean>(false)
    val saveEnable: StateFlow<Boolean> = _saveEnable

    var editMode: Boolean = false

    private val _isApiRequestPending = MutableStateFlow(false)
    val isApiRequestPending: StateFlow<Boolean> = _isApiRequestPending

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _successCall = MutableStateFlow(false)
    val successCall: StateFlow<Boolean> = _successCall

    private val _successStaffMemberCall = MutableStateFlow(false)
    val successStaffMemberCall: StateFlow<Boolean> = _successStaffMemberCall

    fun onUserDataChanged(
        idNumber: String,
        name: String,
        lastname: String,
        password: String,
        phoneNumber: String,
    ) {
        _idNumber.value = idNumber
        _name.value = name
        _lastname.value = lastname
        _password.value = password
        _phoneNumber.value = phoneNumber
        _saveEnable.value = validCredentials()
    }

    private fun setStaffMemberData(staffMemberData: StaffMemberDto) {
        _name.value = staffMemberData.name
        _lastname.value = staffMemberData.lastname
        _idNumber.value = staffMemberData.idNumber
        _phoneNumber.value = staffMemberData.phoneNumber
        _password.value = ""
        _role.value = staffMemberData.role
        _staffMemberData.value = staffMemberData
    }

    private fun validDataHasChanged(): Boolean {
        return _idNumber.value != _staffMemberData.value.idNumber ||
                _name.value != _staffMemberData.value.name ||
                _lastname.value != _staffMemberData.value.lastname ||
                _phoneNumber.value != _staffMemberData.value.phoneNumber ||
                _role.value != _staffMemberData.value.role ||
                _password.value.isNotBlank()
    }

    private fun validCredentials(): Boolean {
        return if (editMode) validDataHasChanged()
        else {
            _idNumber.value.isNotBlank()
                    && _name.value.isNotBlank()
                    && _lastname.value.isNotBlank()
                    && _password.value.isNotBlank()
                    && _phoneNumber.value.isNotBlank()
        }
    }

    fun editUser() {
        if (!_isApiRequestPending.value) {
            _isApiRequestPending.value = true
            viewModelScope.launch {
                staffRepositoryImpl.editStaff(
                    staffMemberObj(),
                    _staffMemberData.value.id
                ).let {
                    when (it) {
                        is APIResult.Success -> {
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
                _isApiRequestPending.value = false
            }
        }
    }

    private fun staffMemberObj(): StaffMember{
        return StaffMember(
            _idNumber.value,
            _name.value.uppercase(),
            _lastname.value.uppercase(),
            _idNumber.value,
            _password.value,
            _phoneNumber.value,
            _role.value
        )
    }

    fun addUser() {
        if (!_isApiRequestPending.value) {
            _isApiRequestPending.value = true

            viewModelScope.launch {
                staffRepositoryImpl.addStaff(
                    staffMemberObj()
                ).let {
                    when (it) {
                        is APIResult.Success -> {
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
                _isApiRequestPending.value = false
            }
        }
    }

    fun deleteUser(idUser: String) {
        if (!_isApiRequestPending.value) {
            _isApiRequestPending.value = true

            viewModelScope.launch {
                staffRepositoryImpl.deleteStaffMember(idUser).let {
                    when (it) {
                        is APIResult.Success -> {
                            _successCall.value = true
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
                    _isApiRequestPending.value = false
                }
            }
        }
    }

    fun getStaffMemberData(idStaffMember: String) {
        _fetchingStaffMember.value = true
        viewModelScope.launch {
            Log.d(Errors.TAG, "IdStaffMember: $idStaffMember")
            staffRepositoryImpl.getStaffMember(idStaffMember).let { data ->
                when (data) {
                    is APIResult.Success -> {
                        _successStaffMemberCall.value = true
                        setStaffMemberData(data.data)
                        _error.value = ""
                    }
                    is APIResult.Error -> {
                        _error.value =
                            when (data.exception.message) {
                                null -> Errors.NULL_ERROR
                                Errors.TIMEOUT -> Errors.TIMEOUT_ERROR
                                else -> "${data.exception.message}"
                            }
                        _successStaffMemberCall.value = false
                    }
                }
            }
            _fetchingStaffMember.value = false
        }
    }

    fun setRole(role: String) {
        _role.value = role
        _saveEnable.value = validCredentials()
    }

    fun resetData() {
        setStaffMemberData(GlobalObjects.StaffMemberInit)
        _successCall.value = false
        _error.value = ""
    }
}