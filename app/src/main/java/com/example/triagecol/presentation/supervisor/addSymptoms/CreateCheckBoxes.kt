package com.example.triagecol.presentation.supervisor.addSymptoms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.triagecol.presentation.common.LabelTextField
import com.example.triagecol.utils.SupervisorConstants

@Composable
fun CreateCheckBoxes(symptomsViewModel: SymptomsViewModel) {
    val symptoms by symptomsViewModel.symptoms.collectAsState()
    val sexPatient by symptomsViewModel.sexPatient.collectAsState()

    Column (modifier = Modifier.padding(start = 16.dp, end = 16.dp)){
        CheckBoxes(
            checked = symptoms.chestPain,
            onCheckedChange = { symptomsViewModel
                .updateSymptoms(it, SymptomsTitle.CHEST_PAIN)
            }, text = SupervisorConstants.CHEST_PAIN
        )
        CheckBoxes(
            checked = symptoms.breathingDiff,
            onCheckedChange = {
                symptomsViewModel.updateSymptoms(
                    it, SymptomsTitle.BREATH_DIFF
                )
            }, text = SupervisorConstants.BREATH_DIFF
        )
        CheckBoxes(
            checked = symptoms.consciousnessAlt,
            onCheckedChange = {
                symptomsViewModel.updateSymptoms(
                    it, SymptomsTitle.CONS_ALT
                )
            }, text = SupervisorConstants.CONS_ALT
        )
        CheckBoxes(
            checked = symptoms.suddenWeakness,
            onCheckedChange = {
                symptomsViewModel.updateSymptoms(
                    it, SymptomsTitle.SUDDEN_WEAKNESS
                )
            }, text = SupervisorConstants.SUDDEN_WEAKNESS
        )
        CheckBoxes(
            checked = symptoms.sevAbdPain,
            onCheckedChange = {
                symptomsViewModel.updateSymptoms(
                    it, SymptomsTitle.SEV_ABD_PAIN
                )
            }, text = SupervisorConstants.SEV_ABD_PAIN
        )
        CheckBoxes(
            checked = symptoms.sevTrauma,
            onCheckedChange = {
                symptomsViewModel.updateSymptoms(
                    it, SymptomsTitle.SEV_TRAUMA
                )
            }, text = SupervisorConstants.SEV_TRAUMA
        )
        if(sexPatient == "Femenino"){
            LabelTextField(nameLabel = "¿Se encuentra en embarazo?")
            CheckBoxes(
                checked = symptoms.pregnancy,
                onCheckedChange = {
                    symptomsViewModel.updateSymptoms(
                        it, SymptomsTitle.PREGNANCY
                    )
                }, text = if(symptoms.pregnancy) "Sí" else "No"
            )
        }
        LabelTextField(nameLabel = "Observaciones medicas")
        Textarea(symptomsViewModel = symptomsViewModel)
    }
}

@Composable
fun CheckBoxes(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = {
                onCheckedChange(it)
            }
        )
        Text(text = text, modifier = Modifier.padding(start = 10.dp, top = 11.dp))
    }
}