package com.example.triagecol.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triagecol.domain.models.LoginModel
import com.example.triagecol.domain.models.dto.StaffMemberDto
import com.example.triagecol.domain.models.dto.toStaffMemberDto
import com.example.triagecol.domain.usecases.APIResult
import com.example.triagecol.domain.usecases.LoginRepository
import com.example.triagecol.presentation.navigation.AppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _user = MutableLiveData<String>()
    val user: LiveData<String> = _user

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

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

    fun onLoginChanged(user: String, password: String) {
        _user.value = user
        _password.value = password
        _loginEnable.value = validCredentials(user, password)
    }

    private fun validCredentials(user: String, password: String): Boolean =
        user.isNotBlank() && password.isNotBlank()

    fun validStaffLogin() {
        Log.d("prueba", "Valid")
        _authenticatingCredentials.value = true
        viewModelScope.launch {
            loginRepository.staffLogin(LoginModel(_user.value!!, _password.value!!)).let {
                when (it) {
                    is APIResult.Success -> {
                        Log.d("prueba", "Datos obtenidos. ${it.data}")
                        _userData.value = it.data
                        _error.value = ""
                        _isValidCredentials.value = true
                        _userLoggedIn.value =
                            when (it.data.role) {
                                "Supervisor" -> AppScreens.SupervisorScreen
                                "Doctor" -> AppScreens.DoctorScreen
                                else -> AppScreens.LoginScreen
                            }
                    }

                    is APIResult.Error -> {
                        _error.value =
                            when (it.exception.message){
                                null -> "Hubo un error."
                                "timeout" -> "Error de Conexion, intentalo de nuevo."
                                else -> "${it.exception.message}"
                            }
                        _isValidCredentials.value = false
                    }
                }
                _authenticatingCredentials.value = false
            }
        }
    }

    fun login() {
        Log.d("prueba", "LoginViewModel Login")
        _authenticatingCredentials.value = true
        if (_user.value != null && _password.value != null) {
            viewModelScope.launch {
                loginRepository.login(LoginModel(_user.value!!, _password.value!!)).let {
                    when (it) {
                        is APIResult.Success -> {
                            _userLoggedIn.value =
                                when (it.data.accountType) {
                                    1 -> {
                                        _userData.value = it.data.user.toStaffMemberDto()
                                        when (it.data.user.role) {
                                            "Supervisor" -> AppScreens.SupervisorScreen
                                            "Doctor" -> AppScreens.DoctorScreen
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
                                if (it.exception.message != null) "${it.exception.message}" else "Hubo un error."
                            _isValidCredentials.value = false
                        }
                    }
                    _authenticatingCredentials.value = false
                }
            }
        } else {
            _error.value = "Debe rellenar el usuario y la contraseña."
        }
    }

    fun validAdminLogin() {
        Log.d("prueba", "ValidAdmin")
        _authenticatingCredentials.value = true
        viewModelScope.launch {
            loginRepository.adminLogin(LoginModel(_user.value!!, _password.value!!)).let {
                when (it) {
                    is APIResult.Success -> {
                        Log.d("prueba", "Cuenta administrador permitida")
                        _isValidCredentials.value = true
                        _userLoggedIn.value = AppScreens.AdminScreen
                    }

                    is APIResult.Error -> {
                        Log.d("prueba", "Error validacion admin: ${it.exception.message}")
                        _isValidCredentials.value = false
                    }
                }
                _authenticatingCredentials.value = false
            }
        }
    }

    fun setValidCredentials(isValidCredentials: Boolean) {
        _isValidCredentials.value = isValidCredentials
        _userLoggedIn.value = AppScreens.LoginScreen
    }

    fun clearError(){
        _error.value = ""
    }
}