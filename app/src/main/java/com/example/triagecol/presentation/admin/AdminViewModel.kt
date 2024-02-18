package com.example.triagecol.presentation.admin

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triagecol.domain.usecases.RepositoryImpl
import com.example.triagecol.domain.models.dto.StaffDto
import com.example.triagecol.domain.models.dto.StaffMemberDto
import com.example.triagecol.domain.usecases.APIResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val repositoryImpl: RepositoryImpl
) : ViewModel() {

    var isGettingData by mutableStateOf(true)

    private val _userList = MutableStateFlow<List<StaffMemberDto>>(StaffDto())
    val userList: StateFlow<List<StaffMemberDto>> = _userList

    private val _userData =
        MutableStateFlow(StaffMemberDto(0, "", "", "", "", ""))
    val userData: StateFlow<StaffMemberDto> = _userData

    private val _error = MutableStateFlow<String>("")
    val error: StateFlow<String> = _error

    private val _isThereChange = MutableStateFlow(false)
    val isThereChange: StateFlow<Boolean> = _isThereChange

    fun getUserList() {
        isGettingData = true
        Log.d("prueba", "API CALL")
        viewModelScope.launch {
            when (val result = repositoryImpl.callToUserList()) {
                is APIResult.Success -> {
                    _error.value = ""
                    _userList.value = result.data
                }

                is APIResult.Error -> _error.value = result.exception.message!!
            }
            isGettingData = false
        }
    }

    fun setUserDataDetails(userData: StaffMemberDto? = null) {
        if(userData == null){
            _userData.value = StaffMemberDto(0,"","","","","")
        }
        else _userData.value = userData
    }
}