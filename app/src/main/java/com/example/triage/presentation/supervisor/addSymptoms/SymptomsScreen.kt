package com.example.triage.presentation.supervisor.addSymptoms

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.triage.presentation.common.TopBarScreen
import com.example.triage.presentation.navigation.AppScreens
import com.example.triage.presentation.supervisor.addSymptoms.components.AlertDialog
import com.example.triage.presentation.supervisor.addSymptoms.components.SymptomsSection
import com.example.triage.utils.StringResources

@Composable
fun SymptomsScreen(
    navController: NavController,
    symptomsViewModel: SymptomsViewModel
) {

    val isSavingData by symptomsViewModel.isSavingSymptoms.collectAsState()
    val successCall by symptomsViewModel.successCall.collectAsState()
    val error by symptomsViewModel.error.collectAsState()
    val successDeleting by symptomsViewModel.successDeletion.collectAsState()
    val showDialog by symptomsViewModel.showDialog.collectAsState()
    val idPatient by symptomsViewModel.idPatient.collectAsState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    BackHandler(true) {
        symptomsViewModel.setShowDialog(true)
    }

    LaunchedEffect(key1 = true) {
        if(idPatient.isEmpty()){
            Toast.makeText(context, "No es posible asignar sintomas. Vuelvalo a intentar.", Toast.LENGTH_LONG).show()
        }
    }

    if(showDialog){
        AlertDialog(onCancel = {symptomsViewModel.setShowDialog(false)},
            onAccept = {
                symptomsViewModel.setShowDialog(false)
                symptomsViewModel.deletePatient()
            })
    }

    if (successCall || successDeleting) {
        navController.popBackStack(AppScreens.MainSupervisorScreen.route, false)
        symptomsViewModel.resetData()
        Toast.makeText(
            LocalContext.current,
            if(successCall) "Paciente registrado en lista de espera" else "Proceso cancelado",
            Toast.LENGTH_LONG
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TopBarScreen(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp), titleText = StringResources.SYMPTOMS_TEXT,
            signOut = false
        ) {
            navController.popBackStack(AppScreens.MainSupervisorScreen.route, false)
        }

        SymptomsSection(symptomsViewModel = symptomsViewModel)

        if (isSavingData) {
            ProgressIndicator(75.dp, 10.dp, Color.Black, 3.dp)
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CancelButton(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(end = 5.dp),
                    symptomsViewModel = symptomsViewModel
                )
                SendDataButton(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(start = 5.dp),
                    symptomsViewModel = symptomsViewModel
                )
            }
        }

        if (!successCall || !successDeleting) {
            Text(
                text = error,
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
private fun ProgressIndicator(size: Dp, padding: Dp, color: Color, stroke: Dp) {
    CircularProgressIndicator(
        modifier = Modifier
            .size(size)
            .padding(padding),
        color = color,
        strokeWidth = stroke
    )
}

@Composable
fun CancelButton(modifier: Modifier = Modifier, symptomsViewModel: SymptomsViewModel) {
    val deletingPatient by symptomsViewModel.deleting.collectAsState()
    val savingSymptoms by symptomsViewModel.isSavingSymptoms.collectAsState()
    Button(
        onClick = {
            symptomsViewModel.deletePatient()
        },
        modifier = modifier.height(45.dp),
        shape = ShapeDefaults.Small,
        colors = ButtonColors(
            containerColor = Color(0xFFFF3232),
            disabledContainerColor = Color(0xFFF7A2A2),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        enabled = !savingSymptoms && !deletingPatient
    ) {
        if (!deletingPatient) {
            Text(
                text = StringResources.CANCEL_TEXT,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.W500)
            )
        } else {
            ProgressIndicator(size = 20.dp, padding = 3.dp, color = Color.White, stroke = 3.dp)
        }
    }
}

@Composable
fun SendDataButton(modifier: Modifier = Modifier, symptomsViewModel: SymptomsViewModel) {
    val savingSymptoms by symptomsViewModel.isSavingSymptoms.collectAsState()
    val focusManager = LocalFocusManager.current
    Button(
        onClick = {
            focusManager.clearFocus()
            symptomsViewModel.sendSymptomsData()
        },
        modifier = modifier.height(45.dp),
        shape = ShapeDefaults.Small,
        colors = ButtonColors(
            containerColor = Color(0xFF1A80E5),
            disabledContainerColor = Color(0xFFAACBEB),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        enabled = !savingSymptoms
    ) {
        if (!savingSymptoms) {
            Text(
                text = StringResources.SEND_TEXT,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.W500)
            )
        } else {
            ProgressIndicator(size = 20.dp, padding = 3.dp, color = Color.White, stroke = 5.dp)
        }
    }
}