package com.example.triagecol.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triagecol.domain.models.dto.LoginModel
import com.example.triagecol.domain.models.dto.StaffMemberDto
import com.example.triagecol.domain.models.dto.toStaffMemberDto
import com.example.triagecol.domain.models.APIResult
import com.example.triagecol.domain.usecases.LoginRepository
import com.example.triagecol.presentation.navigation.AppScreens
import com.example.triagecol.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val regex = "^[a-zA-Z0-9]*$".toRegex()

    private val _user = MutableStateFlow("")
    val user: StateFlow<String> = _user

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    private val _isValidCredentials = MutableStateFlow(false)
    val isValidCredentials: StateFlow<Boolean> = _isValidCredentials

    private val _userData = MutableStateFlow(StaffMemberDto(0, "", "", "", "", "", "", ""))
    val userData: StateFlow<StaffMemberDto> = _userData

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
            loginRepository.login(LoginModel(_user.value, _password.value)).let {
                when (it) {
                    is APIResult.Success -> {
                        _userLoggedIn.value =
                            when (it.data.accountType) {
                                1 -> {
                                    _userData.value = it.data.user.toStaffMemberDto()
                                    when (it.data.user.role) {
                                        Constants.SUPERVISOR -> AppScreens.SupervisorScreen
                                        Constants.DOCTOR -> AppScreens.DoctorScreen
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
                                null -> Constants.NULL_ERROR
                                Constants.TIMEOUT -> Constants.TIMEOUT_ERROR
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