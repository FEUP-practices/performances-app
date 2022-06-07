package com.feup.mobilecomputing.tickets_validator.models

data class ErrorResponseType (
    val statusCode: String,
    val message: String,
    val timestamp: String
    )