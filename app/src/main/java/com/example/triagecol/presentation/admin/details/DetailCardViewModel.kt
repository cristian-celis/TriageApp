package com.example.triagecol.presentation.admin.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triagecol.domain.usecases.StaffRepositoryImpl
import com.example.triagecol.domain.models.dto.StaffMember
import com.example.triagecol.domain.models.dto.StaffMemberDto
import com.example.triagecol.domain.usecases.APIResult
import com.example.triagecol.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailCardViewModel @Inject constructor(
    private val staffRepositoryImpl: StaffRepositoryImpl
) : ViewModel() {

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

    private val _saveEnable = MutableStateFlow<Boolean>(false)
    val saveEnable: StateFlow<Boolean> = _saveEnable

    private val _editMode = MutableStateFlow(false)
    val editMode: StateFlow<Boolean> = _editMode

    private val _addMode = MutableStateFlow(true)
    val addMode: StateFlow<Boolean> = _addMode

    private val _detailMode = MutableStateFlow<DetailMode>(DetailMode.ENTERING)
    val detailMode: StateFlow<DetailMode> = _detailMode

    private val _userData =
        MutableStateFlow(StaffMemberDto(0, "", "", "", "Medico", "", "", ""))
    val userData: StateFlow<StaffMemberDto> = _userData

    private val _detailState = MutableStateFlow(DetailState.ENTERING)
    val detailState: StateFlow<DetailState> = _detailState

    private val _isApiRequestPending = MutableStateFlow(false)
    val isApiRequestPending: StateFlow<Boolean> = _isApiRequestPending

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _successCall = MutableStateFlow(false)
    val successCall: StateFlow<Boolean> = _successCall

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

    fun setUserData(userData: StaffMemberDto) {
        _name.value = userData.name
        _lastname.value = userData.lastname
        _idNumber.value = userData.idNumber
        _phoneNumber.value = userData.phoneNumber
        _password.value = ""
        _role.value = userData.role
        _userData.value = userData

        _detailMode.value = DetailMode.ENTERING
    }

    private fun validDataHasChanged(): Boolean {
        return (_idNumber.value != _userData.value.idNumber ||
                _name.value != _userData.value.name ||
                _lastname.value != _userData.value.lastname ||
                _password.value != _userData.value.password ||
                _phoneNumber.value != _userData.value.phoneNumber ||
                _role.value != _userData.value.role
                )
    }

    private fun validCredentials(): Boolean {
        return if (_editMode.value) validDataHasChanged()
        else {
            _idNumber.value.isNotBlank()
                    && _name.value.isNotBlank()
                    && _lastname.value.isNotBlank()
                    && _password.value.isNotBlank()
                    && _phoneNumber.value.isNotBlank()
                    && _role.value.isNotBlank()
        }
    }

    fun editUser() {
        if (!_isApiRequestPending.value) {
            _isApiRequestPending.value = true
            viewModelScope.launch {
                staffRepositoryImpl.editStaff(
                    staffMemberObj(),
                    _userData.value.id
                ).let {
                    when (it) {
                        is APIResult.Success -> {
                            _detailState.value = DetailState.SAVED
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
                _isApiRequestPending.value = false
            }
        }
    }

    private fun staffMemberObj(): StaffMember{
        return StaffMember(
            _idNumber.value,
            _name.value,
            _lastname.value,
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
                            _detailState.value = DetailState.SAVED
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
                _isApiRequestPending.value = false
            }
        }
    }

    fun setDetailState(detailState: DetailState) {
        _detailState.value = detailState
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
                                    null -> Constants.NULL_ERROR
                                    Constants.TIMEOUT -> Constants.TIMEOUT_ERROR
                                    else -> "${it.exception.message}"
                                }
                        }
                    }
                    _isApiRequestPending.value = false
                }
            }
        }
    }

    fun setRole(role: String) {
        _role.value = role
        _saveEnable.value = validCredentials()
    }

    fun setEditMode() {
        _editMode.value = true
        _addMode.value = false
    }

    fun setAddMode() {
        _addMode.value = true
        _editMode.value = false
    }

    fun setDetailMode(detailMode: DetailMode) {
        _detailMode.value = detailMode
    }

    fun resetData() {
        setUserData(StaffMemberDto(0, "", "", "", "Medico", "", "", ""))
        _successCall.value = false
        _error.value = ""
    }
}