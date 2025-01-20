package com.example.triage.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triage.domain.UserPage
import com.example.triage.data.local.datastore.DataStoreImpl
import com.example.triage.data.remote.repositoriesImpl.MainRepositoryImpl
import com.example.triage.presentation.navigation.AppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreImpl: DataStoreImpl,
    private val mainRepositoryImpl: MainRepositoryImpl
) : ViewModel() {

    private val _savedLogin = MutableStateFlow<String?>(null)
    val savedLogin: StateFlow<String?> = _savedLogin

    private var currentScreen: AppScreens = AppScreens.LoginScreen

    init {
        viewModelScope.launch {
            dataStoreImpl.readSaveLogin.take(1).collect{ savedLoginValue ->
                _savedLogin.value = savedLoginValue
            }
        }
    }

    fun writeSaveLogin(userPage: UserPage, newCurrentScreen: AppScreens) {
        if(currentScreen != newCurrentScreen){
            currentScreen = newCurrentScreen
            viewModelScope.launch {
                dataStoreImpl.writeSaveLogin(userPage)
            }
        }
    }

    fun initAPI(){
        viewModelScope.launch {
            mainRepositoryImpl.initApi()
        }
    }
}