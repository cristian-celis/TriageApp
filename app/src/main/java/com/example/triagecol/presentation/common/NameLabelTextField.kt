package com.example.triagecol.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NameLabelTextField(modifier: Modifier = Modifier, nameLabel: String) {
    Text(
        text = nameLabel,
        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W500),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp, top = 10.dp),
        textAlign = TextAlign.Start
    )
}