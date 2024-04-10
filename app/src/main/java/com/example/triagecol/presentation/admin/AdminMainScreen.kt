package com.example.triagecol.presentation.admin

import ArticleCardShimmerEffect
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.triagecol.presentation.common.RefreshButton
import com.example.triagecol.presentation.navigation.AppScreens
import com.example.triagecol.utils.Constants
import com.example.triagecol.utils.TextConstants

@Composable
fun AdminMainScreen(navController: NavController, adminViewModel: AdminViewModel) {

    val userList by adminViewModel.userList.collectAsState()
    val fetchingData by adminViewModel.fetchingData.collectAsState()
    val successCall by adminViewModel.successCall.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
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
                                navController.navigate(AppScreens.LoginScreen.route) {
                                    popUpTo(AppScreens.AdminScreen.route) { inclusive = true }
                                }
                                /*navController.popBackStack()
                                navController.navigate(route = AppScreens.LoginScreen.route)*/
                            }
                        )
                        .size(34.dp)
                        .background(color = Color(0xA3FF0000), shape = CircleShape),
                    tint = Color.White)

                Box {
                    Text(
                        text = TextConstants.ADMIN_TITLE,
                        style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (fetchingData) {
                ShimmerEffect()
                adminViewModel.clearError()
            } else if (!successCall) {
                ErrorMessage(adminViewModel.error.value) { adminViewModel.getUserList() }
            } else {
                if (userList.isEmpty()) {
                    Box (modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)){
                        Text(
                            text = TextConstants.NO_STAFF_REGISTERED,
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.W500)
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.padding(7.dp)) {
                        items(count = userList.size) {
                            val user = userList[it]
                            UserDataCard(
                                medicalStaff = user,
                                onClick = {
                                    adminViewModel.setUserDataDetails(user)
                                    adminViewModel.setClickOnAddButton(false)
                                    navController.navigate(AppScreens.DetailCard.route)
                                })
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 5.dp)
                ) {
                    AddStaff(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp),
                        { navController.navigate(AppScreens.DetailCard.route) }
                    ){adminViewModel.setClickOnAddButton(true)}
                }
                Spacer(modifier = Modifier.height(50.dp))
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
                fontSize = 16.sp),
            modifier = Modifier.padding(10.dp)
        )
        RefreshButton(onRefresh = {
            onRefresh()
        })
    }
}

@Composable
fun AddStaff(
    modifier: Modifier = Modifier,
    navigationOnAddClick: () -> Unit,
    onAddClick: (Boolean) -> Unit
) {
    Button(
        onClick = {
            onAddClick(true)
            navigationOnAddClick()
        },
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1A80E5),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = TextConstants.ADD_TEXT, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
    }
}

@Composable
private fun ShimmerEffect() {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        repeat(10) {
            ArticleCardShimmerEffect(
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
    }
}