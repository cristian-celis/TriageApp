package com.example.triage.domain.models

import com.example.triage.domain.models.dto.AddPatientRequest
import com.example.triage.domain.models.dto.PatientDto
import com.example.triage.domain.models.dto.StaffMemberAccount
import com.example.triage.domain.models.dto.StaffMemberDto

object GlobalObjects {
    val StaffMemberInit = StaffMemberDto(0, "", "", "", "", "", "", "Desconectado")

    val PatientInit = PatientDto(0, "", "","", "", "", "", "", "", "", 0, "",0)

    val PatientRequestInit = AddPatientRequest("CC", "","", "", "", "Femenino", "", "", "")

    val StaffMemberAccount = StaffMemberAccount(0,"","","Desconectado")
}