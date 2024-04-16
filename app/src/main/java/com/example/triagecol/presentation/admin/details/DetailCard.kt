package com.example.triagecol.presentation.admin.details

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.traigecol.R
import com.example.triagecol.presentation.navigation.AppScreens
import com.example.triagecol.utils.TextConstants

@Composable
fun DetailCard(
    navController: NavController,
    detailCardViewModel: DetailCardViewModel
) {

    val saveEnable: Boolean by detailCardViewModel.saveEnable.collectAsState()
    val isLoading: Boolean by detailCardViewModel.isApiRequestPending.collectAsState()
    val successCall: Boolean by detailCardViewModel.successCall.collectAsState()

    val focusManager = LocalFocusManager.current

    var screenTitle by rememberSaveable { mutableStateOf(TextConstants.ADD_STAFF_TITLE) }

    if (detailCardViewModel.detailMode.value == DetailMode.ENTERING) {
        screenTitle = if (detailCardViewModel.userData.value.id != 0) {
            detailCardViewModel.setEditMode()
            TextConstants.EDIT_STAFF_TITLE
        } else {
            detailCardViewModel.setAddMode()
            TextConstants.ADD_STAFF_TITLE
        }
        detailCardViewModel.setDetailMode(DetailMode.ENTERED)
    }

    if (successCall) {
        detailCardViewModel.setDetailState(DetailState.ENTERING)
        detailCardViewModel.setDetailMode(DetailMode.ENTERING)
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
                        onClick = {
                            detailCardViewModel.setDetailMode(DetailMode.ENTERING)
                            navController.popBackStack()
                        })
                    .size(25.dp)
            )

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

        if (isLoading) {
            ProgressIndicator()
        } else {
            if(!successCall){
                SaveUserButton(loginEnable = saveEnable, detailCardViewModel)
                Text(
                    text = detailCardViewModel.error.value,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color(0xFFAF1717)
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(10.dp)
                )
            }
            if (detailCardViewModel.editMode.value) {
                DeleteUserButton(detailCardViewModel, detailCardViewModel.userData.value.id)
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
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
            containerColor = Color(0xFFFF3939)
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = TextConstants.DELETE_TEXT, style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        )
    }

    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false },
            dismissButton = { Button(onClick = { showDialog = false }) { Text(text = "No") } },
            confirmButton = {
                Button(onClick = {
                    detailCardViewModel.deleteUser(idUser.toString())
                    detailCardViewModel.setDetailMode(DetailMode.ENTERING)
                }) {
                    Text(
                        text = TextConstants.YES_TEXT
                    )
                }
            },
            title = {
                Text(
                    text = TextConstants.DELETE_TEXT
                )
            },
            text = { Text(text = TextConstants.CONFIRM_DELETE_USER) })
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
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1A80E5),
            disabledContainerColor = Color(0xFFAACBEB),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ), enabled = loginEnable,
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = TextConstants.SAVE_TEXT, style = TextStyle(fontSize = 18.sp))
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