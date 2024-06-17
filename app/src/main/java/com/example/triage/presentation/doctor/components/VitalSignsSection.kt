package com.example.triage.presentation.doctor.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.traigecol.R
import com.example.triage.utils.StringResources

@Composable
fun VitalSignsSection(modifier: Modifier = Modifier, temperature: String, bloodOxygen: String, heartRate: String) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        VitalSignsCards(
            iconResource = painterResource(id = R.drawable.temperature_icon),
            vitalSignName = StringResources.TEMPERATURE,
            vitalSignValue = "$temperature °C",
            modifier = Modifier.fillMaxWidth(0.33f)
        )
        Spacer(modifier = Modifier.width(9.dp))

        VitalSignsCards(
            iconResource = painterResource(id = R.drawable.blood_oxygen_icon),
            vitalSignName = StringResources.BLOOD_OXYGEN,
            vitalSignValue = "$bloodOxygen%",
            modifier = Modifier.fillMaxWidth(0.5f)
        )
        Spacer(modifier = Modifier.width(9.dp))

        VitalSignsCards(
            iconResource = painterResource(id = R.drawable.heart_rate_icon),
            vitalSignName = StringResources.HEART_RATE,
            vitalSignValue = "$heartRate bpm",
            modifier = Modifier.fillMaxWidth(1f)
        )
    }
}

@Composable
fun VitalSignsCards(
    iconResource: Painter,
    vitalSignName: String,
    vitalSignValue: String,
    modifier: Modifier = Modifier
) {


    Box(
        modifier = modifier
            .height(100.dp)
    ) {
        Column(
            modifier = Modifier
                .border(
                    border = BorderStroke(width = 1.dp, color = Color(0xFFBEBEBE)),
                    shape = ShapeDefaults.Small
                )
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
                    .padding(top = 3.dp),
                text = vitalSignName,
                style = TextStyle(color = Color.Gray, fontSize = 14.sp),
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    Icon(
                        painter = iconResource,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.CenterStart)
                    )
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = vitalSignValue,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}