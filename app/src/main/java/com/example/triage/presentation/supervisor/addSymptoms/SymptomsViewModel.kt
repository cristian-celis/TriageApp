package com.example.triage.presentation.supervisor.addSymptoms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triage.domain.models.APIResult
import com.example.triage.domain.models.InitSymptoms
import com.example.triage.domain.models.SymptomsAdd
import com.example.triage.data.remote.repositoriesImpl.PatientRepositoryImpl
import com.example.triage.utils.Errors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SymptomsViewModel @Inject constructor(
    private val patientRepositoryImpl: PatientRepositoryImpl
): ViewModel() {

    private val _idPatient = MutableStateFlow("")
    val idPatient: StateFlow<String> = _idPatient

    private val _sexPatient = MutableStateFlow("Masculino")
    val sexPatient: StateFlow<String> = _sexPatient

    private val _symptoms = MutableStateFlow<SymptomsAdd>(
        InitSymptoms.symptoms
    )
    val symptoms: StateFlow<SymptomsAdd> = _symptoms

    private val _observations = MutableStateFlow<String>("")
    val observations: StateFlow<String> = _observations

    private val _isSavingSymptoms = MutableStateFlow(false)
    val isSavingSymptoms: StateFlow<Boolean> = _isSavingSymptoms

    private val _successCall = MutableStateFlow(false)
    val successCall: StateFlow<Boolean> = _successCall

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _successDeletion = MutableStateFlow(false)
    val successDeletion: StateFlow<Boolean> = _successDeletion

    private val _deleting = MutableStateFlow(false)
    val deleting: StateFlow<Boolean> = _deleting

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    fun updateSymptoms(
        value: Boolean,
        title: SymptomsTitle
    ) {
        _symptoms.update {
            when(title){
                SymptomsTitle.CHEST_PAIN -> {it.copy(chestPain = value)}
                SymptomsTitle.BREATH_DIFF -> {it.copy(breathingDiff = value)}
                SymptomsTitle.CONS_ALT -> {it.copy(consciousnessAlt = value)}
                SymptomsTitle.SUDDEN_WEAKNESS -> {it.copy(suddenWeakness = value)}
                SymptomsTitle.SEV_ABD_PAIN -> {it.copy(sevAbdPain = value)}
                SymptomsTitle.SEV_TRAUMA -> {it.copy(sevTrauma = value)}
                SymptomsTitle.PREGNANCY -> {it.copy(pregnancy = value)}
            }
        }
    }

    fun updateObservation(observation: String){
        _observations.value = observation
    }

    private fun createSymptomsList(): ArrayList<Int>{
        val symptomsList = ArrayList<Int>()
        val sym = _symptoms.value

        if (sym.chestPain) symptomsList.add(1) else symptomsList.remove(1)
        if (sym.breathingDiff) symptomsList.add(2) else symptomsList.remove(2)
        if (sym.consciousnessAlt) symptomsList.add(3) else symptomsList.remove(3)
        if (sym.suddenWeakness) symptomsList.add(4) else symptomsList.remove(4)
        if (sym.sevAbdPain) symptomsList.add(5) else symptomsList.remove(5)
        if (sym.sevTrauma) symptomsList.add(6) else symptomsList.remove(6)
        return symptomsList
    }

    fun sendSymptomsData(){
        if(!_isSavingSymptoms.value){
            val symptomsList = createSymptomsList()
            _isSavingSymptoms.value = true
            viewModelScope.launch {
                patientRepositoryImpl.saveSymptomsPat(
                    _idPatient.value,
                    symptomsList,
                    _symptoms.value.pregnancy,
                    _observations.value
                ).let {
                    when(it){
                        is APIResult.Success -> {
                            _successCall.value = true
                        }
                        is APIResult.Error -> {
                            _successCall.value = false
                            _error.value =
                                when(it.exception.message){
                                    null -> Errors.NULL_ERROR
                                    Errors.TIMEOUT -> Errors.TIMEOUT_ERROR
                                    else -> "${it.exception.message}"
                                }
                        }
                    }
                }
                _isSavingSymptoms.value= false
            }
        }
    }

    fun deletePatient(){
        _deleting.value = true
        viewModelScope.launch {
            patientRepositoryImpl.deletePatient(_idPatient.value).let {
                when(it){
                    is APIResult.Success -> {
                        _successDeletion.value = true
                    }
                    is APIResult.Error -> {
                        _successDeletion.value = false
                        _error.value =
                            when(it.exception.message){
                                null -> Errors.NULL_ERROR
                                Errors.TIMEOUT -> Errors.TIMEOUT_ERROR
                                else -> "${it.exception.message}"
                            }
                    }
                }
                _deleting.value = false
            }
        }
    }

    fun setShowDialog(showDialog: Boolean){
        _showDialog.value = showDialog
    }

    fun resetData(){
        _idPatient.value= ""
        _successCall.value = false
        _successDeletion.value = false
        _error.value = ""
        _symptoms.value = InitSymptoms.symptoms
    }

    fun setInitialValues(idPatient: String, sex: String){
        _idPatient.value = idPatient
        _sexPatient.value = sex
    }
}

enum class SymptomsTitle {
    CHEST_PAIN,
    BREATH_DIFF,
    CONS_ALT,
    SUDDEN_WEAKNESS,
    SEV_ABD_PAIN,
    SEV_TRAUMA,
    PREGNANCY
}