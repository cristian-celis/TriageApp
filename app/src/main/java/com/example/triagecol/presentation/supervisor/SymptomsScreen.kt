package com.example.triagecol.presentation.supervisor

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.traigecol.R
import com.example.triagecol.presentation.navigation.AppScreens
import com.example.triagecol.utils.SupervisorConstants

@Composable
fun SymptomsScreen(
    navController: NavController,
    symptomsViewModel: SymptomsViewModel
) {

    val isSavingData by symptomsViewModel.isSavingSymptoms.collectAsState()
    val successCall by symptomsViewModel.successCall.collectAsState()

    val focusManager = LocalFocusManager.current

    /*
    * CUANDO SE INICIA EL SYMPTOM SCREEN SE SETEA EL NUMERO DE DOCUMENTO DEL PACIENTE AGREGADO.
    * DESPUES DE AGREGAR UN PACIENTE Y SETEAR LOS SINTOMAS, AL AGREGAR OTRO PACIENTE EL
    * NUMERO DE DOCUMENTO SE SETEA MAL!!!
    * */

    if (successCall) {
        navController.popBackStack()
        symptomsViewModel.resetData()
        Toast.makeText(
            LocalContext.current,
            "Paciente registrado en lista de espera.",
            Toast.LENGTH_LONG
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_go_back),
                contentDescription = null,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            navController.navigate(route = AppScreens.SupervisorScreen.route)
                        }
                    )
                    .size(25.dp))

            Text(
                text = SupervisorConstants.SYMPTOMS_TEXT,
                style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        CreateCheckBoxes(symptomsViewModel = symptomsViewModel)

        if (isSavingData) {
            ProgressIndicator()
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GoBackButton(symptomsViewModel = symptomsViewModel, navController = navController)
                SendDataButton(symptomsViewModel = symptomsViewModel)
            }
        }

        if (!successCall) {
            Text(
                text = symptomsViewModel.error.value,
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color(0xFFFF0000)
                ),
                modifier = Modifier
                    .padding(20.dp)
            )
        }

    }
}

@Composable
private fun ProgressIndicator() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(75.dp)
            .padding(10.dp),
        color = Color.Black,
        strokeWidth = 3.dp
    )
}

@Composable
fun CreateCheckBoxes(symptomsViewModel: SymptomsViewModel) {

    val chestPain by symptomsViewModel.chestPain.collectAsState()
    val breathDiff by symptomsViewModel.breathingDiff.collectAsState()
    val conscAlt by symptomsViewModel.consciousnessAlt.collectAsState()
    val suddenWeakness by symptomsViewModel.suddenWeakness.collectAsState()
    val sevAbdPain by symptomsViewModel.sevAbdPain.collectAsState()
    val sevTrauma by symptomsViewModel.sevTrauma.collectAsState()

    CheckBoxes(
        checked = chestPain,
        onCheckedChange = {
            symptomsViewModel.updateSymptoms(
                it, breathDiff, conscAlt, suddenWeakness, sevAbdPain, sevTrauma
            )
        }, text = SupervisorConstants.CHEST_PAIN
    )
    CheckBoxes(
        checked = breathDiff,
        onCheckedChange = {
            symptomsViewModel.updateSymptoms(
                chestPain, it, conscAlt, suddenWeakness, sevAbdPain, sevTrauma
            )
        }, text = SupervisorConstants.BREATH_DIFF
    )
    CheckBoxes(
        checked = conscAlt,
        onCheckedChange = {
            symptomsViewModel.updateSymptoms(
                chestPain, breathDiff, it, suddenWeakness, sevAbdPain, sevTrauma
            )
        }, text = SupervisorConstants.CONSC_ALT
    )
    CheckBoxes(
        checked = suddenWeakness,
        onCheckedChange = {
            symptomsViewModel.updateSymptoms(
                chestPain, breathDiff, conscAlt, it, sevAbdPain, sevTrauma
            )
        }, text = SupervisorConstants.SUDDEN_WEAKNESS
    )
    CheckBoxes(
        checked = sevAbdPain,
        onCheckedChange = {
            symptomsViewModel.updateSymptoms(
                chestPain, breathDiff, conscAlt, suddenWeakness, it, sevTrauma
            )
        }, text = SupervisorConstants.SEV_ABD_PAIN
    )
    CheckBoxes(
        checked = sevTrauma,
        onCheckedChange = {
            symptomsViewModel.updateSymptoms(
                chestPain, breathDiff, conscAlt, suddenWeakness, sevAbdPain, it
            )
        }, text = SupervisorConstants.SEV_TRAUMA
    )
}

@Composable
fun GoBackButton(symptomsViewModel: SymptomsViewModel, navController: NavController) {
    Button(
        onClick = {
            symptomsViewModel.resetData()
            navController.popBackStack()
        },
        modifier = Modifier,
        shape = ShapeDefaults.Small,
        colors = ButtonColors(
            containerColor = Color(0xFFFF3232),
            disabledContainerColor = Color(0xFFF7A2A2),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        enabled = !symptomsViewModel.isSavingSymptoms.value
    ) {
        Text(
            text = SupervisorConstants.CANCEL_TEXT,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.W500)
        )
    }
}

@Composable
fun SendDataButton(symptomsViewModel: SymptomsViewModel) {
    Button(
        onClick = { symptomsViewModel.sendSymptomsData() },
        modifier = Modifier,
        shape = ShapeDefaults.Small,
        colors = ButtonColors(
            containerColor = Color(0xFF1A80E5),
            disabledContainerColor = Color(0xFFAACBEB),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        enabled = !symptomsViewModel.isSavingSymptoms.value
    ) {
        Text(
            text = SupervisorConstants.SEND_TEXT,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.W500)
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
            onCheckedChange = {
                onCheckedChange(it)
            }
        )
        Text(text = text, modifier = Modifier.padding(start = 10.dp, top = 11.dp))
    }
}