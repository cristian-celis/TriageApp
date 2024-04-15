package com.example.triagecol

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triagecol.domain.UserPage
import com.example.triagecol.domain.datastore.DataStoreImpl
import com.example.triagecol.domain.usecases.MainRepository
import com.example.triagecol.presentation.navigation.AppScreens
import com.example.triagecol.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreImpl: DataStoreImpl,
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _savedLogin = MutableStateFlow<String?>(null)
    val savedLogin: StateFlow<String?> = _savedLogin

    private val _currentScreen = MutableStateFlow<AppScreens>(AppScreens.LoginScreen)
    val currentScreen: StateFlow<AppScreens> = _currentScreen

    init {
        viewModelScope.launch {
            dataStoreImpl.readSaveLogin.take(1).collect{ savedLoginValue ->
                _savedLogin.value = savedLoginValue
            }
        }
    }

    fun writeSaveLogin(userPage: UserPage, newCurrentPage: AppScreens) {
        viewModelScope.launch {
            _currentScreen.value = newCurrentPage
            dataStoreImpl.writeSaveLogin(userPage)
        }
    }

    fun initBack(){
        viewModelScope.launch {
            mainRepository.initApi()
        }
    }
}