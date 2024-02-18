package com.example.triagecol.presentation.admin.details

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.traigecol.R
import com.example.triagecol.presentation.navigation.AppScreens

@Composable
fun DetailCard(
    navController: NavController,
    detailCardViewModel: DetailCardViewModel
) {

    val saveEnable: Boolean by detailCardViewModel.saveEnable.observeAsState(initial = false)
    val detailState: DetailState by detailCardViewModel.detailState.collectAsState()
    val isLoading: Boolean by detailCardViewModel.isAddingOrEditingUsers.collectAsState()

    var screenTitle by rememberSaveable { mutableStateOf("Agregar Personal") }

    Log.d("prueba", "--------------- DETAIL CARD")

    if(detailCardViewModel.detailMode.value == DetailMode.NONE){
        Log.d("prueba", "Reevalua?: ${detailCardViewModel.userData.value.id}")
        if (detailCardViewModel.userData.value.id != 0) {
            Log.d("prueba", "Edit Mode")
            screenTitle = "Editar Personal"
            detailCardViewModel.setEditMode()
        } else{
            Log.d("prueba", "Add Mode")
            detailCardViewModel.setAddMode()
            screenTitle = "Agregar Personal"
        }
        detailCardViewModel.setDetailMode(DetailMode.SOME)
    }

    if (detailState == DetailState.SAVED) {
        detailCardViewModel.setDetailState(DetailState.ENTERING)
        detailCardViewModel.setDetailMode(DetailMode.NONE)
        navController.navigate(AppScreens.AdminScreen.route)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(10.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_go_back),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {detailCardViewModel.setDetailMode(DetailMode.NONE)
                                navController.navigate(route = AppScreens.AdminScreen.route)})
                        .size(25.dp))

                Box {
                    Text(
                        text = screenTitle,
                        style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            TextBoxesForData(modifier = Modifier.fillMaxWidth(), detailCardViewModel)

            if(isLoading){
                Box{
                    Icon(
                        painter = painterResource(id = R.drawable.ic_time),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp).zIndex(1f)
                            .align(Alignment.Center),
                        tint = Color(0xC3C0C0C0)
                    )
                }
            }else{
                SaveUserButton(loginEnable = saveEnable, detailCardViewModel)
                if (detailCardViewModel.editMode.value) {
                    DeleteUserButton(detailCardViewModel, detailCardViewModel.userData.value.id)
                }
            }
        }
    }
}

@Composable
fun DeleteUserButton(
    detailCardViewModel: DetailCardViewModel, idUser: Int
) {
    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = {
            showDialog = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFEE1D1D)
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = "Eliminar", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold)
        )
    }

    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false },
            dismissButton = { Button(onClick = { showDialog = false }) { Text(text = "No") } },
            confirmButton = {
                Button(onClick = {
                    detailCardViewModel.setDetailMode(DetailMode.NONE)
                    detailCardViewModel.deleteUser(idUser.toString())
                }) {
                    Text(
                        text = "Sí"
                    )
                }
            },
            title = {
                Text(
                    text = "Eliminar"
                )
            },
            text = { Text(text = "¿Estas seguro que desea eliminar al usuario?") })
    }
}

@Composable
fun SaveUserButton(
    loginEnable: Boolean, detailCardViewModel: DetailCardViewModel
) {
    Button(
        onClick = {
            if (detailCardViewModel.editMode.value) detailCardViewModel.editUser() else detailCardViewModel.addUser()
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .height(48.dp), colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1A80E5),
            disabledContainerColor = Color(0xFFAACBEB),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ), enabled = loginEnable,
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = "Guardar")
    }
}