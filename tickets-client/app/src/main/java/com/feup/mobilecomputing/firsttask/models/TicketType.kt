package com.feup.mobilecomputing.firsttask.models

data class TicketType (
        val id: String,
        val seatNumber: Int,
        val performance: PerformanceType,
        val used: Boolean,
        val numberBought: Int
        )