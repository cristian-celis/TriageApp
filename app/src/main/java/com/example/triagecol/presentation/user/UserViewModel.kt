package com.example.triagecol.presentation.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(): ViewModel() {

    private val _name = MutableStateFlow<String>("")
    val name: StateFlow<String> = _name

    private val _lastname = MutableStateFlow<String>("")
    val lastname: StateFlow<String> = _lastname

    private val _documentType = MutableStateFlow<String>("")
    val documentType: StateFlow<String> = _documentType

    private val _idNumber = MutableStateFlow<String>("")
    val idNumber: StateFlow<String> = _idNumber

    private val _chestPain = MutableStateFlow(false)
    val chestPain: StateFlow<Boolean> = _chestPain

    private val _breathingDiff = MutableStateFlow(false)
    val breathingDiff: StateFlow<Boolean> = _breathingDiff

    private val _consciousnessAlt = MutableStateFlow(false)
    val consciousnessAlt: StateFlow<Boolean> = _consciousnessAlt

    private val _suddenWeakness = MutableStateFlow(false)
    val suddenWeakness: StateFlow<Boolean> = _suddenWeakness

    private val _sevAbdPain = MutableStateFlow(false)
    val sevAbdPain: StateFlow<Boolean> = _sevAbdPain

    private val _sevTrauma = MutableStateFlow(false)
    val sevTrauma: StateFlow<Boolean> = _sevTrauma

    private val _saveEnable = MutableLiveData<Boolean>()
    val saveEnable: LiveData<Boolean> = _saveEnable

    fun updateUserData(
        name: String,
        lastname: String,
        documentType: String,
        idNumber: String,
    ) {
        _name.value = name
        _lastname.value = lastname
        _documentType.value = documentType
        _idNumber.value = idNumber
    }

    fun updateSymptoms(
        chestPainValue: Boolean,
        breathingDifficultyValue: Boolean,
        consciousnessAlterationsValue: Boolean,
        suddenWeaknessValue: Boolean,
        severeAbdominalPainValue: Boolean,
        severeTraumaValue: Boolean
    ) {
        _chestPain.value = chestPainValue
        _breathingDiff.value = breathingDifficultyValue
        _consciousnessAlt.value = consciousnessAlterationsValue
        _suddenWeakness.value = suddenWeaknessValue
        _sevAbdPain.value = severeAbdominalPainValue
        _sevTrauma.value = severeTraumaValue
    }

}