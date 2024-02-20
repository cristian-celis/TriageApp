package com.example.triagecol.presentation.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.traigecol.R
import com.example.triagecol.presentation.common.TextFieldComponent
import com.example.triagecol.presentation.navigation.AppScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    navController: NavController
) {

    val userViewModel: UserViewModel = hiltViewModel()


    val name by userViewModel.name.collectAsState()
    val lastname by userViewModel.lastname.collectAsState()
    val documentType by userViewModel.documentType.collectAsState()
    val idNumber by userViewModel.idNumber.collectAsState()

    val chestPain by userViewModel.chestPain.collectAsState()
    val breathingDiff by userViewModel.breathingDiff.collectAsState()
    val consciousnessAlt by userViewModel.consciousnessAlt.collectAsState()
    val sudWeak by userViewModel.suddenWeakness.collectAsState()
    val sevAbdPain by userViewModel.sevAbdPain.collectAsState()
    val sevTrauma by userViewModel.sevTrauma.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(scrollState)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_go_back),
                contentDescription = null,
                modifier = Modifier.clickable { navController.navigate(route = AppScreens.LoginScreen.route)}.size(25.dp))

            Text(text = "TRIAGE", style = TextStyle(fontSize = 28.sp), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }

        TextFieldComponent(
            placeHolderText = "Nombre", value = name,isTextFieldEnable = true,
            onTextFieldChanged = {
                userViewModel.updateUserData(
                    it,
                    lastname,
                    documentType,
                    idNumber
                )
            })
        TextFieldComponent(
            placeHolderText = "Apellido", value = lastname, isTextFieldEnable = true,
            onTextFieldChanged = { userViewModel.updateUserData(name, it, documentType, idNumber) })
        TextFieldComponent(
            placeHolderText = "Tipo de documento", value = documentType,isTextFieldEnable = true,
            onTextFieldChanged = { userViewModel.updateUserData(name, lastname, it, idNumber) })
        TextFieldComponent(
            placeHolderText = "Numero de documento", value = idNumber,isTextFieldEnable = true,
            onTextFieldChanged = { userViewModel.updateUserData(name, lastname, documentType, it) })

        CheckBoxes(
            checked = chestPain,
            onCheckedChange = {
                userViewModel.updateSymptoms(
                    it, breathingDiff, consciousnessAlt, sudWeak, sevAbdPain, sevTrauma
                )
                }, text = "Dolor de pecho"
        )
        CheckBoxes(
            checked = breathingDiff,
            onCheckedChange = {
                userViewModel.updateSymptoms(
                    chestPain, it, consciousnessAlt, sudWeak, sevAbdPain, sevTrauma
                )
            }, text = "Dificultad para respirar"
        )
        CheckBoxes(
            checked = consciousnessAlt,
            onCheckedChange = {
                userViewModel.updateSymptoms(
                    chestPain, breathingDiff, it, sudWeak, sevAbdPain, sevTrauma
                )
            }, text = "Alteracion de conciencia"
        )
        CheckBoxes(
            checked = sudWeak,
            onCheckedChange = {
                userViewModel.updateSymptoms(
                    chestPain, breathingDiff, consciousnessAlt, it, sevAbdPain, sevTrauma
                )
            }, text = "Debilidad repentina"
        )
        CheckBoxes(
            checked = sevAbdPain,
            onCheckedChange = {
                userViewModel.updateSymptoms(
                    chestPain, breathingDiff, consciousnessAlt, sudWeak, it, sevTrauma
                )
            }, text = "Dolor abdominal intenso"
        )
        CheckBoxes(
            checked = sevTrauma,
            onCheckedChange = {
                userViewModel.updateSymptoms(
                    chestPain, breathingDiff, consciousnessAlt, sudWeak, sevAbdPain, it
                )
            }, text = "Traumas severos"
        )

        Button(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.End), shape = ShapeDefaults.Large) {
            Text(text = "Continuar")
        }
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