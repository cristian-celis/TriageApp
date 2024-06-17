package com.example.triage.presentation.doctor.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.traigecol.R
import com.example.triage.domain.models.dto.SymptomDto
import com.example.triage.presentation.doctor.RoundedBoxTextMessage
import com.example.triage.presentation.doctor.SymptomsCard
import com.example.triage.utils.StringResources

@Composable
fun SymptomsAccordion(modifier: Modifier = Modifier, symptoms: List<SymptomDto>) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        Modifier.padding(vertical = 12.dp)
    ) {
        AccordionHeader(isExpanded = expanded) {
            expanded = !expanded
        }
        AnimatedVisibility(visible = expanded) {
            Surface(
                color = Color.White,
            ) {
                if(symptoms.isNotEmpty()){
                    Column (modifier = Modifier.padding(10.dp)){
                        for (i in symptoms.indices) {
                            SymptomsCard(symptoms[i].symptomName)
                        }
                    }
                }else{
                    RoundedBoxTextMessage(message = "Sin sintomas registrados", topPadding = 10.dp, bottomPadding = 5.dp)
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
        modifier = Modifier.fillMaxWidth()
            .clickable { onTapped() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(10.dp)
                .height(50.dp)
                .background(if(isExpanded) Color(0xFFB8CBFA) else Color(0xFF1A80E5))
        )
        Text(
            text = StringResources.SYMPTOMS_TEXT,
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold),
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, bottom = 10.dp, top = 10.dp)
        )
        Surface(
            shape = CircleShape,
            color = Color.White,
            modifier = Modifier.padding(end = 12.dp)
        ) {
            Icon(
                painter = painterResource(id = if (isExpanded) R.drawable.minus_icon else R.drawable.add),
                contentDescription = null,
                modifier = Modifier.size(if(isExpanded) 17.dp else 24.dp),
                tint = Color.Black
            )
        }
    }
}