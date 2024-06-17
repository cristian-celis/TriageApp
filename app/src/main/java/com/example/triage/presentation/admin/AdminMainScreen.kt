package com.example.triage.presentation.admin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.traigecol.R
import com.example.triage.presentation.admin.components.ArticleCardShimmerEffect
import com.example.triage.presentation.admin.components.ReportDialog
import com.example.triage.presentation.admin.components.UserDataCard
import com.example.triage.presentation.common.ConfirmScreenDialog
import com.example.triage.presentation.common.TopBarScreen
import com.example.triage.presentation.navigation.AppScreens
import com.example.triage.utils.StringResources

@Composable
fun AdminMainScreen(navController: NavController, adminViewModel: AdminViewModel) {

    val userList by adminViewModel.userList.collectAsState()
    val fetchingData by adminViewModel.fetchingData.collectAsState()
    val fetchingStaffCount by adminViewModel.fetchingStaffCount.collectAsState()
    val successCall by adminViewModel.successCall.collectAsState()
    val showDialog by adminViewModel.showDialog.collectAsState()
    val error by adminViewModel.error.collectAsState()
    val showReportDialog by adminViewModel.showReportDialog.collectAsState()

    LaunchedEffect(key1 = true){
        adminViewModel.getCountStaff()
    }

    BackHandler(true) {
        adminViewModel.setDialog(true)
    }

    if(showDialog){
        ConfirmScreenDialog(mainText = StringResources.CONFIRM_SIGN_OFF,
            onDismiss = {adminViewModel.setDialog(false)}) {
            adminViewModel.setDialog(false)
            navController.navigate(AppScreens.LoginScreen.route) {
                popUpTo(AppScreens.AdminScreen.route) { inclusive = true }
            }
        }
    }

    if(showReportDialog){
        ReportDialog(adminViewModel = adminViewModel)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF3F1F1))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
        ) {

            TopBarScreen(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                titleText = StringResources.ADMIN_TITLE,
                signOut = true,
                onReportClick = { adminViewModel.setReportDialog(true) }
            ) {
                adminViewModel.setDialog(true)
            }

            if (fetchingData || fetchingStaffCount) {
                adminViewModel.clearError()
                ShimmerEffect()
            } else if (!successCall) {
                ErrorMessage(error) { adminViewModel.getUserList() }
            } else {
                if (userList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.8f)
                    ) {
                        Text(
                            text = StringResources.NO_STAFF_REGISTERED,
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.W500)
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxHeight(0.9f)) {
                        items(count = userList.size) {
                            val user = userList[it]
                            UserDataCard(
                                medicalStaff = user,
                                onClick = {
                                    adminViewModel.setUserDetails(user)
                                    navController.navigate(AppScreens.DetailCard.setInitialValues(user.id.toString(), true))
                                })
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 13.dp, end = 13.dp, bottom = 16.dp)
                        .background(Color.Transparent)
                ) {
                    AddStaff(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(70.dp),
                    ){ navController.navigate(AppScreens.DetailCard.setInitialValues("0",false)) }
                }
            }
        }
    }
}

@Composable
fun ErrorMessage(error: String, onRefresh: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = error,
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = Color(0xFF383838),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            modifier = Modifier.padding(10.dp)
        )
        IconButton(
            onClick = {
                onRefresh()
            }
        ) {
            Icon(
                painterResource(id = R.drawable.ic_refresh),
                contentDescription = "Refresh",
                modifier = Modifier.size(50.dp),
                tint = Color.LightGray
            )
        }
    }
}

@Composable
fun AddStaff(
    modifier: Modifier = Modifier,
    navigationOnAddClick: () -> Unit,
) {
    Button(
        onClick = {
            navigationOnAddClick()
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1A80E5),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = StringResources.ADD_TEXT,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
private fun ShimmerEffect() {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        repeat(10) {
            ArticleCardShimmerEffect(
                modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 5.dp)
            )
        }
    }
}