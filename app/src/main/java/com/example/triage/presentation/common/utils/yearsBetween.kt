package com.example.triage.presentation.common.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.Period

@RequiresApi(Build.VERSION_CODES.O)
fun yearsBetween(date: LocalDate? = LocalDate.now()): String {
    val bornDate = date ?: LocalDate.now()
    val currentDate = LocalDate.now()
    val periodBetween = Period.between(bornDate, currentDate)
    return periodBetween.years.toString()
}