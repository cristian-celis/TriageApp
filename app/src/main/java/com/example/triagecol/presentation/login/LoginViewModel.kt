package com.example.triagecol.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _user = MutableLiveData<String>()
    val user: LiveData<String> = _user

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    fun onLoginChanged(user: String, password: String) {
        _user.value = user
        _password.value = password
        _loginEnable.value = validCredentials(user, password)
    }

    private fun validCredentials(user: String, password: String): Boolean =
        user.isNotBlank() && password.isNotBlank()
}