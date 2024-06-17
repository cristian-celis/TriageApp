package com.example.triage.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.traigecol.R

@Composable
fun LabelTextField(modifier: Modifier = Modifier, nameLabel: String) {
    Text(
        text = nameLabel,
        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.W400, color = colorResource(id = R.color.body)),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 3.dp, top = 7.dp),
        textAlign = TextAlign.Start
    )
}