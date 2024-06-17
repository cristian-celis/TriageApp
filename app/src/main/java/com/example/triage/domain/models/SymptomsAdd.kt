package com.example.triage.domain.models

data class SymptomsAdd(
    var chestPain: Boolean,
    var breathingDiff: Boolean,
    var consciousnessAlt: Boolean,
    var suddenWeakness: Boolean,
    var sevAbdPain: Boolean,
    var sevTrauma: Boolean,
    var pregnancy: Boolean
)

object InitSymptoms{
    val symptoms = SymptomsAdd(
        chestPain = false,
        breathingDiff = false,
        consciousnessAlt = false,
        suddenWeakness = false,
        sevAbdPain = false,
        sevTrauma = false,
        pregnancy = false
    )
}