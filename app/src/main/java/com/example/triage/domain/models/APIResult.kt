package com.example.triage.domain.models

import java.lang.Exception

sealed class APIResult<out T>{
    data class Success<out T>(val data: T): APIResult<T>()
    data class Error(val exception: Exception): APIResult<Nothing>()
}