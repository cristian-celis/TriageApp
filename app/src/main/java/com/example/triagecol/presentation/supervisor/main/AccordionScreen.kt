package com.example.triagecol.presentation.supervisor.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triagecol.domain.models.dto.PatientDtoForList

@Composable
fun AccordionScreen(
    modifier: Modifier = Modifier,
    mainSupervisorViewModel: MainSupervisorViewModel
) {
    val patientList by mainSupervisorViewModel.patientList.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Column(modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 12.dp)) {
        AccordionHeader(isExpanded = expanded) {
            expanded = !expanded
        }
        AnimatedVisibility(visible = expanded) {
            Surface(
                color = White,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.LightGray),
                modifier = Modifier.padding(top = 8.dp),
                tonalElevation = 1.dp
            ) {
                LazyColumn {
                    items(patientList.size) { row ->
                        AccordionRow(patientList[row], row)
                        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
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
    val degrees = if (isExpanded) 180f else 0f

    Surface(
        color = White,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .clickable { onTapped() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Pacientes", Modifier.weight(1f), color = Gray)
            Surface(shape = CircleShape) {
                Icon(
                    Icons.Outlined.ArrowDropDown,
                    contentDescription = "arrow-down",
                    modifier = Modifier.rotate(degrees),
                    tint = White
                )
            }
        }
    }
}

@Composable
private fun AccordionRow(patient: PatientDtoForList, level: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth().fillMaxHeight()
            .height(40.dp)
            .background(color = Color(0xFFF3F1F1), shape = ShapeDefaults.Small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$level",
            modifier = Modifier.padding(start = 3.dp)
        )
        Text(
            text = "${patient.name} ${patient.lastname}"
        )
        Text(
            text = patient.idNumber,
            modifier = Modifier.padding(end = 3.dp)
        )
    }
}