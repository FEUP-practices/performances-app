package com.feup.mobilecomputing.firsttask.models

data class BuyPayloadType (
    val numberBought: Int,
    val seatNumber: Int,
    val userId: String,
    val performanceId: String
    )