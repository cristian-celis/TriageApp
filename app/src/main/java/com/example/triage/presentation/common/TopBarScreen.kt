package com.example.triage.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.traigecol.R

@Composable
fun TopBarScreen(
    modifier: Modifier = Modifier,
    titleText: String,
    signOut: Boolean,
    onReportClick: (() -> Unit?)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .height(70.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { onClick() }) {
            Icon(
                painter = painterResource(id = if(signOut) R.drawable.ic_go_back else R.drawable.close_icon),
                contentDescription = null,
                modifier = Modifier.size(34.dp),
                tint = Color.Black
            )
        }
        Box {
            Text(
                text = titleText,
                style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold)
            )
        }
        if(onReportClick != null){
            IconButton(onClick = { onReportClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.report_icon),
                    contentDescription = null,
                    modifier = Modifier.size(37.dp).padding(3.dp),
                    tint = Color.Black
                )
            }
        }else{
            Box{}
        }
    }
}