package com.example.triagecol.presentation.supervisor.addSymptoms

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.traigecol.R
import com.example.triagecol.presentation.common.TopBarScreen
import com.example.triagecol.presentation.navigation.AppScreens
import com.example.triagecol.utils.Constants
import com.example.triagecol.utils.SupervisorConstants

@Composable
fun SymptomsScreen(
    navController: NavController,
    symptomsViewModel: SymptomsViewModel
) {

    val isSavingData by symptomsViewModel.isSavingSymptoms.collectAsState()
    val successCall by symptomsViewModel.successCall.collectAsState()
    val error by symptomsViewModel.error.collectAsState()
    val successDeleting by symptomsViewModel.successDeletion.collectAsState()

    val focusManager = LocalFocusManager.current

    if (successCall) {
        navController.navigate(AppScreens.SupervisorScreen.route) {
            popUpTo(AppScreens.SymptomsScreen.route) { inclusive = true }
        }
        symptomsViewModel.resetData()
        Toast.makeText(
            LocalContext.current,
            "Paciente registrado en lista de espera",
            Toast.LENGTH_LONG
        ).show()
    }

    if (successDeleting) {
        navController.popBackStack()
        symptomsViewModel.resetData()
        Toast.makeText(
            LocalContext.current,
            "Proceso cancelado",
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
                .padding(bottom = 10.dp), titleText = SupervisorConstants.SYMPTOMS_TEXT,
            backColor = Color.White,
            tintColor = Color.Black
        ) {
            navController.navigate(route = AppScreens.SupervisorScreen.route)
        }

        CreateCheckBoxes(symptomsViewModel = symptomsViewModel)

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
        enabled = !symptomsViewModel.isSavingSymptoms.value && !deletingPatient
    ) {
        if (!deletingPatient) {
            Text(
                text = SupervisorConstants.CANCEL_TEXT,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.W500)
            )
        } else {
            ProgressIndicator(size = 20.dp, padding = 3.dp, color = Color.White, stroke = 5.dp)
        }
    }
}

@Composable
fun SendDataButton(modifier: Modifier = Modifier, symptomsViewModel: SymptomsViewModel) {
    val savingSymptoms by symptomsViewModel.isSavingSymptoms.collectAsState()
    Button(
        onClick = {
            Log.d(Constants.TAG, "Boton oprimido")
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
        enabled = !symptomsViewModel.isSavingSymptoms.value
    ) {
        if (!savingSymptoms) {
            Text(
                text = SupervisorConstants.SEND_TEXT,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.W500)
            )
        } else {
            ProgressIndicator(size = 20.dp, padding = 3.dp, color = Color.White, stroke = 5.dp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Textarea(symptomsViewModel: SymptomsViewModel) {
    val observations by symptomsViewModel.observations.collectAsState()
    TextField(
        value = observations,
        onValueChange = { symptomsViewModel.updateObservation(it) },
        placeholder = {
            Text(
                text = "Ingrese las observaciones medicas del paciente (No es obligatorio).",
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(id = R.color.placeholder)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(10.dp)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(0xFFE8EDF2),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}