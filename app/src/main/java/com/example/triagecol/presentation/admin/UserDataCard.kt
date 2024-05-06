package com.example.triagecol.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.traigecol.R
import com.example.triagecol.domain.models.dto.StaffMemberDto

@Composable
fun UserDataCard(
    medicalStaff: StaffMemberDto,
    onClick: (StaffMemberDto) -> Unit
) {

    val iconUser: Int =
        if (medicalStaff.role == "Doctor") R.drawable.stethoscope_icon else R.drawable.supervisor_icon

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onClick(medicalStaff) },
            )
            .clip(MaterialTheme.shapes.medium)
            .background(Color.White)//.shadow(3.dp)
    ) {
        Row {
            Column (horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                 modifier = Modifier.fillMaxHeight().padding(start = 10.dp)){
                Icon(
                    painter = painterResource(id = iconUser),
                    contentDescription = null,
                    modifier = Modifier
                        .size(45.dp).clip(CircleShape)
                        .background(Color(0xFFF3F1F1))
                        .padding(5.dp)
                )
            }
            /*Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFD3E5F7)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconUser),
                    contentDescription = null,
                    modifier = Modifier
                        .size(33.dp)
                        .align(Alignment.Center)
                )
            }*/
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${medicalStaff.name} ${medicalStaff.lastname}",
                    style = TextStyle(fontSize = 18.sp),
                    color = colorResource(
                        id = R.color.text_title
                    )
                )
                Box(
                    contentAlignment = Alignment.BottomStart,
                    modifier = Modifier.padding(top = 7.dp)
                ) {
                    Text(
                        text = medicalStaff.role,
                        style = TextStyle(fontSize = 16.sp),
                        color = Color(0xFF4F7396)
                    )
                }
            }
        }
    }
}