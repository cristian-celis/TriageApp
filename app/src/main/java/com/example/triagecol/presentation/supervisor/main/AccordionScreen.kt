package com.example.triagecol.presentation.supervisor.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Maximize
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.traigecol.R
import com.example.triagecol.domain.models.dto.PatientDtoForList

@Composable
fun AccordionScreen(
    modifier: Modifier = Modifier,
    mainSupervisorViewModel: MainSupervisorViewModel
) {
    val patientList by mainSupervisorViewModel.patientList.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier//.padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 12.dp)
    ) {
        AccordionHeader(isExpanded = expanded) {
            expanded = !expanded
        }
        AnimatedVisibility(visible = expanded) {
            Surface(
                color = White,
                //shape = RoundedCornerShape(8.dp),
                //border = BorderStroke(1.dp, Color.LightGray),
            ) {
                Column {
                    AccordionDescription()
                    HorizontalDivider(color = Color.LightGray, thickness = 0.7.dp)
                    for (i in 0 until patientList.size) {
                        AccordionRow(patientList[i], i + 1)
                        HorizontalDivider(color = Color.LightGray, thickness = 0.7.dp)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AccordionHeader(
    isExpanded: Boolean = false,
    onTapped: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .border(width = 0.7.dp, color = LightGray)
            .clickable { onTapped() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(10.dp)
                .height(50.dp)
                .background(if(isExpanded) Color(0xFFB8CBFA) else Color(0xFF1A80E5))
        ) {
        }
        Text(
            text = "Informacion pacientes",
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            color = Color.Black,
            fontWeight = FontWeight.W500,
            fontSize = 18.sp
        )
        Surface(
            shape = CircleShape,
            color = Color.White,
            modifier = Modifier.padding(end = 12.dp)
        ) {
            Icon(
                painter = painterResource(id = if (isExpanded) R.drawable.minus_icon else R.drawable.add),
                contentDescription = null,
                modifier = if (isExpanded) Modifier.size(15.dp) else Modifier.size(22.dp),
                tint = Color.Black
            )
        }
    }
}

@Composable
private fun AccordionRow(patient: PatientDtoForList, level: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(color = Color.White, shape = ShapeDefaults.Small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$level",
            modifier = Modifier.padding(start = 20.dp)
        )
        Text(
            text = "${patient.name} ${patient.lastname}"
        )
        Text(
            text = patient.idNumber,
            modifier = Modifier.padding(end = 20.dp)
        )
    }
}

@Composable
fun AccordionDescription(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(color = White, shape = ShapeDefaults.Small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Prioridad", fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 10.dp)
        )
        Text(
            text = "Paciente", fontWeight = FontWeight.Medium,
        )
        Text(
            text = "Identificación", fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(end = 10.dp)
        )
    }
}