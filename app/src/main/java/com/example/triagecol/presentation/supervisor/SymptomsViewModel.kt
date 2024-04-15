package com.example.triagecol.presentation.supervisor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triagecol.domain.models.APIResult
import com.example.triagecol.domain.usecases.PatientRepository
import com.example.triagecol.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SymptomsViewModel @Inject constructor(
    private val patientRepository: PatientRepository
): ViewModel() {

    private val _id = MutableStateFlow("")
    val id: StateFlow<String> = _id

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

    private val _isSavingSymptoms = MutableStateFlow(false)
    val isSavingSymptoms: StateFlow<Boolean> = _isSavingSymptoms

    private val _successCall = MutableStateFlow(false)
    val successCall: StateFlow<Boolean> = _successCall

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _isDialogShown = MutableStateFlow(false)
    val isDialogShown: StateFlow<Boolean> = _isDialogShown

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

    private fun createSymptomsList(): ArrayList<Int>{
        val symptomsList = ArrayList<Int>()
        if(_chestPain.value) symptomsList.add(1) else symptomsList.remove(1)
        if(_breathingDiff.value) symptomsList.add(2) else symptomsList.remove(2)
        if(_consciousnessAlt.value) symptomsList.add(3) else symptomsList.remove(3)
        if(_suddenWeakness.value) symptomsList.add(4) else symptomsList.remove(4)
        if(_sevAbdPain.value) symptomsList.add(5) else symptomsList.remove(5)
        if(_sevTrauma.value) symptomsList.add(6) else symptomsList.remove(6)
        return symptomsList
    }

    fun sendSymptomsData(){
        if(!_isSavingSymptoms.value){
            val symptomsList = createSymptomsList()
            _isSavingSymptoms.value = true
            viewModelScope.launch {
                patientRepository.saveSymptomsPat(_id.value, createSymptomsList()).let {
                    when(it){
                        is APIResult.Success -> {
                            _successCall.value = true
                        }
                        is APIResult.Error -> {
                            _successCall.value = false
                            _error.value =
                                when(it.exception.message){
                                    null -> Constants.NULL_ERROR
                                    Constants.TIMEOUT -> Constants.TIMEOUT_ERROR
                                    else -> "${it.exception.message}"
                                }
                        }
                    }
                }
                _isSavingSymptoms.value= false
            }
        }
    }

    fun resetData(){
        _successCall.value = false
        _error.value = ""

        _chestPain.value = false
        _breathingDiff.value = false
        _consciousnessAlt.value = false
        _suddenWeakness.value = false
        _sevAbdPain.value = false
        _sevTrauma.value = false
    }

    fun setIdPatient(idPatient: String){
        _id.value = idPatient
    }

    fun setIsDialogShown(isDialogShown: Boolean){
        _isDialogShown.value = isDialogShown
    }
}