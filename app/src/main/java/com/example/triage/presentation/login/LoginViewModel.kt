package com.example.triage.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triage.domain.models.dto.LoginModel
import com.example.triage.domain.models.dto.toStaffMemberDto
import com.example.triage.domain.models.APIResult
import com.example.triage.domain.models.GlobalObjects.StaffMemberInit
import com.example.triage.data.remote.repositoriesImpl.LoginRepositoryImpl
import com.example.triage.presentation.navigation.AppScreens
import com.example.triage.utils.Errors
import com.example.triage.utils.StringResources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepositoryImpl: LoginRepositoryImpl
) : ViewModel() {

    private val _user = MutableStateFlow("")
    val user: StateFlow<String> = _user

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    private val _isValidCredentials = MutableStateFlow(false)
    val isValidCredentials: StateFlow<Boolean> = _isValidCredentials

    var userData = StaffMemberInit

    private val _userLoggedIn = MutableStateFlow<AppScreens>(AppScreens.LoginScreen)
    val userLoggedIn: StateFlow<AppScreens> = _userLoggedIn

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _authenticatingCredentials = MutableStateFlow(false)
    val authenticatingCredentials: StateFlow<Boolean> = _authenticatingCredentials

    private val _showToastMessage = MutableStateFlow(false)
    val showToastMessage: StateFlow<Boolean> = _showToastMessage

    fun onLoginChanged(user: String, password: String) {
        _user.value = user
        _password.value = password
        _loginEnable.value = validCredentials(user, password)
    }

    private fun validCredentials(user: String, password: String): Boolean =
        user.isNotBlank() && password.isNotBlank()

    fun login() {
        _authenticatingCredentials.value = true
        viewModelScope.launch {
            loginRepositoryImpl.login(LoginModel(_user.value, _password.value)).let {
                when (it) {
                    is APIResult.Success -> {
                        _userLoggedIn.value =
                            when (it.data.accountType) {
                                1 -> {
                                    userData = it.data.user.toStaffMemberDto()
                                    when (it.data.user.role) {
                                        StringResources.SUPERVISOR -> AppScreens.MainSupervisorScreen
                                        StringResources.DOCTOR -> AppScreens.DoctorScreen
                                        else -> AppScreens.LoginScreen
                                    }
                                }

                                2 -> AppScreens.AdminScreen
                                else -> AppScreens.LoginScreen
                            }
                        _isValidCredentials.value = true
                    }

                    is APIResult.Error -> {
                        _error.value =
                            when (it.exception.message) {
                                null -> Errors.NULL_ERROR
                                Errors.TIMEOUT -> Errors.TIMEOUT_ERROR
                                else -> "${it.exception.message}"
                            }
                        _showToastMessage.value = true
                        _isValidCredentials.value = false
                    }
                }
            }
            _authenticatingCredentials.value = false
        }
    }

    fun setValidCredentials(isValidCredentials: Boolean) {
        _isValidCredentials.value = isValidCredentials
        _userLoggedIn.value = AppScreens.LoginScreen
    }

    fun setShowToastMessage(){
        _showToastMessage.value = false
    }

    fun clearError() {
        _error.value = ""
    }
}