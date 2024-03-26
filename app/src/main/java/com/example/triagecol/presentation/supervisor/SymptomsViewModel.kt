package com.example.triagecol.presentation.supervisor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SymptomsViewModel @Inject constructor(): ViewModel() {

    private val _idPatient = MutableStateFlow("")
    val idPatient: StateFlow<String> = _idPatient

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

    fun sendSymptomsData(){
        viewModelScope.launch {

        }
    }

    fun setIdPatient(idPatient: String){
        _idPatient.value = idPatient
    }
}