package com.example.triagecol.presentation.admin.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triagecol.domain.usecases.RepositoryImpl
import com.example.triagecol.domain.models.dto.AddUserDtoItem
import com.example.triagecol.domain.models.dto.ApiResponse
import com.example.triagecol.domain.models.dto.StaffMemberDto
import com.example.triagecol.domain.usecases.APIResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailCardViewModel @Inject constructor(
    private val repositoryImpl: RepositoryImpl
) : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _lastname = MutableLiveData<String>()
    val lastname: LiveData<String> = _lastname

    private val _idNumber = MutableLiveData<String>()
    val idNumber: LiveData<String> = _idNumber

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _typePerson = MutableStateFlow<String>("medico")
    val typePerson: StateFlow<String> = _typePerson

    private val _username = MutableLiveData<String>()

    private val _saveEnable = MutableLiveData<Boolean>()
    val saveEnable: LiveData<Boolean> = _saveEnable

    private val _editMode = MutableStateFlow(false)
    val editMode: StateFlow<Boolean> = _editMode

    private val _addMode = MutableStateFlow(true)
    val addMode: StateFlow<Boolean> = _addMode

    private val _detailMode = MutableStateFlow<DetailMode>(DetailMode.NONE)
    val detailMode: StateFlow<DetailMode> = _detailMode

    private val _userData =
        MutableStateFlow(StaffMemberDto(0, "", "", "", "medico", ""))
    val userData: StateFlow<StaffMemberDto> = _userData

    private val _detailState = MutableStateFlow(DetailState.ENTERING)
    val detailState: StateFlow<DetailState> = _detailState

    private val _responseAPI =
        MutableStateFlow<APIResult<ApiResponse?>>(APIResult.Error(Exception("Initial value")))
    val responseAPI: StateFlow<APIResult<ApiResponse?>> = _responseAPI

    private val _isAddingOrEditingUsers = MutableStateFlow(false)
    val isAddingOrEditingUsers: StateFlow<Boolean> = _isAddingOrEditingUsers

    fun onUserDataChanged(
        name: String, lastname: String, idNumber: String, password: String,
    ) {
        _name.value = name
        _lastname.value = lastname
        _password.value = password
        _idNumber.value = idNumber
        _username.value = idNumber
        _saveEnable.value = validCredentials(name, lastname, password)
    }

    fun setTypePerson(typePerson: String){ _typePerson.value = typePerson }

    fun setUserData(userData: StaffMemberDto) {
        _name.value = userData.name
        _lastname.value = userData.lastname
        _password.value = userData.password
        _typePerson.value = userData.type_person
        _userData.value = userData

        _detailMode.value = DetailMode.NONE
    }

    private fun validNewUserData(): Boolean {
        return (_name.value != _userData.value.name ||
                _lastname.value != _userData.value.lastname ||
                _password.value != _userData.value.password ||
                _typePerson.value != _userData.value.type_person
                )
    }

    private fun validCredentials(name: String, lastname: String, password: String): Boolean {
        return if (_editMode.value) validNewUserData() else name.isNotBlank() && password.isNotBlank() && lastname.isNotBlank()
    }

    fun editUser() {
        if (!_isAddingOrEditingUsers.value) {
            Log.d("prueba", "Editando...")
            _isAddingOrEditingUsers.value = true
            _userData.value.name = _name.value!!
            _userData.value.lastname = _lastname.value!!
            _userData.value.password = _password.value!!
            _userData.value.type_person = _typePerson.value

            viewModelScope.launch {
                repositoryImpl.editUser(_userData.value).let {
                    _detailState.value = DetailState.SAVED
                    _responseAPI.value = it
                    _isAddingOrEditingUsers.value = false
                }
            }
        }
    }

    fun addUser() {
        if (!_isAddingOrEditingUsers.value) {
            Log.d("prueba", "Agregando...")
            _isAddingOrEditingUsers.value = true

            viewModelScope.launch {
                repositoryImpl.addUser(
                    AddUserDtoItem(
                        _lastname.value!!,
                        _name.value!!,
                        _password.value!!,
                        _typePerson.value,
                        _username.value!!
                    )
                ).let {
                    _detailState.value = DetailState.SAVED
                    _responseAPI.value = it
                    _isAddingOrEditingUsers.value = false
                }
            }
        }
    }

    fun setDetailState(detailState: DetailState) { _detailState.value = detailState }

    fun deleteUser(idUser: String) {
        if(!_isAddingOrEditingUsers.value){
            Log.d("prueba", "Eliminando $idUser...")
            _isAddingOrEditingUsers.value = true
            viewModelScope.launch {
                repositoryImpl.deleteUser(idUser).let {
                    _detailState.value = DetailState.SAVED
                    _responseAPI.value = it
                    _isAddingOrEditingUsers.value = false
                }
            }
        }
    }

    fun setEditMode(){
        _editMode.value = true
        _addMode.value = false
    }

    fun setAddMode(){
        _addMode.value = true
        _editMode.value = false
    }

    fun setDetailMode(detailMode: DetailMode){
        Log.d("prueba", "Set DetailMode ${detailMode}")
        _detailMode.value = detailMode
    }
}