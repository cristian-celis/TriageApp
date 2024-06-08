package com.example.triagecol

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triagecol.domain.UserPage
import com.example.triagecol.domain.datastore.DataStoreImpl
import com.example.triagecol.domain.usecases.MainRepository
import com.example.triagecol.presentation.navigation.AppScreens
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
            mainRepository.initApi()
        }
    }
}