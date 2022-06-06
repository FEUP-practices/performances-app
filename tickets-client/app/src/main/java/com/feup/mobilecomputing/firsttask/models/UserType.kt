package com.feup.mobilecomputing.firsttask.models

data class UserType (
    val nif: String,
    val name: String,
    val cardType: String,
    val cardNumber: String,
    val cardValidity: String,
    val cardCVV: String
)