package com.feup.mobilecomputing.firsttask.models

data class RegisterPayload (
    val user: UserType,
    val signature: SignatureType,
        )