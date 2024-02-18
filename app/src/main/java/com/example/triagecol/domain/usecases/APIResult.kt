package com.example.triagecol.domain.usecases

import java.lang.Exception

sealed class APIResult<out T>{
    data class Success<out T>(val data: T): APIResult<T>()
    data class Error(val exception: Exception): APIResult<Nothing>()
}
