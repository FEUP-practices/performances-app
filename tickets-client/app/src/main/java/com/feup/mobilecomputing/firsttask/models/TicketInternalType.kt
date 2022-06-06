package com.feup.mobilecomputing.firsttask.models

import java.util.*

data class TicketInternalType (
    val _id: String,
    val seatNumber: Int,
    val used: Boolean,
    val performanceId: String,
    val name: String,
    val price: Double,
    val startDate: Date,
    val endDate: Date,
    val address: String,
    val numberBought: Int
)