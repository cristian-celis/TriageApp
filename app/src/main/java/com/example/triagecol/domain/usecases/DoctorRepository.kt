package com.example.triagecol.domain.usecases

import retrofit2.Retrofit
import javax.inject.Inject

class DoctorRepository @Inject constructor(
    private val retrofit: Retrofit
){
}