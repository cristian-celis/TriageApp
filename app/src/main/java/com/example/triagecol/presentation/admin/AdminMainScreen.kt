package com.example.triagecol.presentation.admin

import ArticleCardShimmerEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.traigecol.R
import com.example.triagecol.presentation.common.RefreshButton
import com.example.triagecol.presentation.navigation.AppScreens

@Composable
fun AdminMainScreen(navController: NavController) {

    val adminViewModel: AdminViewModel = hiltViewModel()

    val userList by adminViewModel.userList.collectAsState()
    val error by adminViewModel.error.collectAsState()

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
                        .clickable (
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {navController.navigate(route = AppScreens.LoginScreen.route)}
                        )
                        .size(25.dp))

                Box {
                    Text(
                        text = "Administrador",
                        style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (adminViewModel.isGettingData) {
                ShimmerEffect()
            } else if (error.isNotBlank()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = adminViewModel.error.value)
                    RefreshButton(onRefresh = {
                        adminViewModel.getUserList()
                    })
                }
            } else {
                //SearchBar()
                if(userList.isNullOrEmpty()){
                    Text(text = "No hay personal registrado.")
                }

                LazyColumn(modifier = Modifier.padding(7.dp)) {
                    items(count = userList.size) {
                        val user = userList[it]
                        UserDataCard(
                            medicalStaff = user,
                            onClick = {
                                adminViewModel.setUserDataDetails(user)
                                navController.navigate(AppScreens.DetailCard.route)
                            })
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
                Box (modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 5.dp)){
                    AddStaff(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp),
                        adminViewModel,
                        navController
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

@Composable
fun AddStaff(
    modifier: Modifier = Modifier,
    adminViewModel: AdminViewModel,
    navController: NavController
) {
    Button(
        onClick = {
            adminViewModel.setUserDataDetails()
            navController.navigate(AppScreens.DetailCard.route)
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
        Text(text = "Agregar", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
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