package com.example.triagecol

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.triagecol.domain.UserPage
import com.example.triagecol.domain.datastore.DataStoreImpl
import com.example.triagecol.presentation.navigation.AppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreImpl: DataStoreImpl
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
}