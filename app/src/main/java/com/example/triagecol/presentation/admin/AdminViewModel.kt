package com.example.triagecol.presentation.admin

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triagecol.domain.usecases.StaffRepositoryImpl
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
    private val staffRepositoryImpl: StaffRepositoryImpl
) : ViewModel() {

    var isFetchingStaff by mutableStateOf(true)

    private val _userList = MutableStateFlow<List<StaffMemberDto>>(StaffDto())
    val userList: StateFlow<List<StaffMemberDto>> = _userList

    private val _userData =
        MutableStateFlow(StaffMemberDto(0, "", "", "", "", "","",""))
    val userData: StateFlow<StaffMemberDto> = _userData

    private val _apiResult = MutableStateFlow<String>("")
    val apiResult: StateFlow<String> = _apiResult

    private val _isThereChange = MutableStateFlow(false)
    val isThereChange: StateFlow<Boolean> = _isThereChange

    private val _clickOnAddButton = MutableStateFlow(false)
    val clickOnAddButton: StateFlow<Boolean> = _clickOnAddButton

    fun getUserList() {
        isFetchingStaff = true
        Log.d("prueba", "Fetching staff")
        viewModelScope.launch {
            when (val result = staffRepositoryImpl.getStaff()) {
                is APIResult.Success -> {
                    _apiResult.value = ""
                    _userList.value = result.data
                }

                is APIResult.Error -> _apiResult.value = result.exception.message!!
            }
            isFetchingStaff = false
        }
    }

    fun setClickOnAddButton(isClicked: Boolean){
        _clickOnAddButton.value = isClicked
    }

    fun setUserDataDetails(userData: StaffMemberDto) {
        _userData.value = userData
    }
}