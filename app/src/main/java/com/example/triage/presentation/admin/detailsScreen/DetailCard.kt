package com.example.triage.presentation.admin.detailsScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.triage.presentation.common.TopBarScreen
import com.example.triage.utils.Errors
import com.example.triage.utils.StringResources

@Composable
fun DetailCard(
    navController: NavController,
    detailCardViewModel: DetailCardViewModel,
    editMode: Boolean,
    id: String = "0"
) {

    val saveEnable: Boolean by detailCardViewModel.saveEnable.collectAsState()
    val isLoading: Boolean by detailCardViewModel.isApiRequestPending.collectAsState()
    val successCall: Boolean by detailCardViewModel.successCall.collectAsState()
    val fetchingStaffMember by detailCardViewModel.fetchingStaffMember.collectAsState()
    val error by detailCardViewModel.error.collectAsState()

    val focusManager = LocalFocusManager.current

    var screenTitle by rememberSaveable { mutableStateOf(StringResources.ADD_STAFF_TITLE) }

    LaunchedEffect(key1 = true){
        detailCardViewModel.editMode = editMode
        if(editMode){
            screenTitle = StringResources.EDIT_STAFF_TITLE
            Log.d(Errors.TAG, "id obtenido: $id")
            if(id != "0") detailCardViewModel.getStaffMemberData(id)
        }else{
            screenTitle = StringResources.ADD_STAFF_TITLE
        }
    }

    if (successCall) {
        detailCardViewModel.resetData()
        navController.popBackStack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        TopBarScreen(
            modifier = Modifier
                .fillMaxWidth(),
            titleText = screenTitle,
            signOut = false
        ) {
            navController.popBackStack()
        }

        TextBoxesForData(modifier = Modifier.fillMaxWidth(), detailCardViewModel)

        if (isLoading) {
            ProgressIndicator(75.dp, 1.dp, Color.Black, 3.dp)
        } else {
            SaveUserButton(loginEnable = saveEnable, detailCardViewModel)
            if (detailCardViewModel.editMode) {
                DeleteUserButton(detailCardViewModel.staffMemberData.value.id){
                    detailCardViewModel.deleteUser(it)
                }
            }
            if(!successCall){
                Text(
                    text = error,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color(0xFFAF1717)
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
    if (fetchingStaffMember) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            ProgressIndicator(size = 80.dp, padding = 5.dp, color = Color(0xFF1A80E5), stroke = 7.dp)
        }
    }
}

@Composable
fun DeleteUserButton(
    idUser: Int, onAccept: (String) -> Unit
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
            containerColor = Color(0xFFFF3939)
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = StringResources.DELETE_TEXT, style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        )
    }

    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false },
            dismissButton = { Button(onClick = { showDialog = false }) { Text(text = "No") } },
            confirmButton = {
                Button(onClick = {
                    onAccept(idUser.toString())
                }) {
                    Text(
                        text = StringResources.YES_TEXT
                    )
                }
            },
            title = {
                Text(
                    text = StringResources.DELETE_TEXT
                )
            },
            text = { Text(text = StringResources.CONFIRM_DELETE_USER) })
    }
}

@Composable
fun SaveUserButton(
    loginEnable: Boolean, detailCardViewModel: DetailCardViewModel
) {
    Button(
        onClick = {
            if (detailCardViewModel.editMode)
                detailCardViewModel.editUser()
            else
                detailCardViewModel.addUser()
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1A80E5),
            disabledContainerColor = Color(0xFFAACBEB),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ), enabled = loginEnable,
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = StringResources.SAVE_TEXT, style = TextStyle(fontSize = 18.sp))
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