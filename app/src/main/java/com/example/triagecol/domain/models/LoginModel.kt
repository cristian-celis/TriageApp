package com.example.triagecol.domain.models

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("usuario") val username: String,
    @SerializedName("clave") val password: String
)
