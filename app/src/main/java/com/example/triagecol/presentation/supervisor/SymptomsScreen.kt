package com.example.triagecol.presentation.supervisor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.traigecol.R
import com.example.triagecol.presentation.navigation.AppScreens

@Composable
fun VitalSignsScreen(
    navController: NavController,
    symptomsViewModel: SymptomsViewModel
) {

    val chestPain by symptomsViewModel.chestPain.collectAsState()
    val breathingDiff by symptomsViewModel.breathingDiff.collectAsState()
    val consciousnessAlt by symptomsViewModel.consciousnessAlt.collectAsState()
    val sudWeak by symptomsViewModel.suddenWeakness.collectAsState()
    val sevAbdPain by symptomsViewModel.sevAbdPain.collectAsState()
    val sevTrauma by symptomsViewModel.sevTrauma.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_go_back), contentDescription = null,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            navController.navigate(route = AppScreens.AdminScreen.route)
                        })
                    .size(25.dp)
            )

            Box {
                Text(
                    text = "Signos Vitales",
                    style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        CheckBoxes(
            checked = chestPain,
            onCheckedChange = {
                symptomsViewModel.updateSymptoms(
                    it, breathingDiff, consciousnessAlt, sudWeak, sevAbdPain, sevTrauma
                )
            }, text = "Dolor de pecho"
        )
        CheckBoxes(
            checked = breathingDiff,
            onCheckedChange = {
                symptomsViewModel.updateSymptoms(
                    chestPain, it, consciousnessAlt, sudWeak, sevAbdPain, sevTrauma
                )
            }, text = "Dificultad para respirar"
        )
        CheckBoxes(
            checked = consciousnessAlt,
            onCheckedChange = {
                symptomsViewModel.updateSymptoms(
                    chestPain, breathingDiff, it, sudWeak, sevAbdPain, sevTrauma
                )
            }, text = "Alteracion de conciencia"
        )
        CheckBoxes(
            checked = sudWeak,
            onCheckedChange = {
                symptomsViewModel.updateSymptoms(
                    chestPain, breathingDiff, consciousnessAlt, it, sevAbdPain, sevTrauma
                )
            }, text = "Debilidad repentina"
        )
        CheckBoxes(
            checked = sevAbdPain,
            onCheckedChange = {
                symptomsViewModel.updateSymptoms(
                    chestPain, breathingDiff, consciousnessAlt, sudWeak, it, sevTrauma
                )
            }, text = "Dolor abdominal intenso"
        )
        CheckBoxes(
            checked = sevTrauma,
            onCheckedChange = {
                symptomsViewModel.updateSymptoms(
                    chestPain, breathingDiff, consciousnessAlt, sudWeak, sevAbdPain, it
                )
            }, text = "Traumas severos"
        )
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
            onCheckedChange = { onCheckedChange(it) }
        )
        Text(text = text, modifier = Modifier.padding(start = 10.dp, top = 11.dp))
    }
}