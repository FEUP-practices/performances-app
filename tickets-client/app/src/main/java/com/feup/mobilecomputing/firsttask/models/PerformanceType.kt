package com.feup.mobilecomputing.firsttask.models

import java.net.URL
import java.util.*

data class PerformanceType (
    val id: String,
    var imageURI: String?,
    var name: String,
    var price: Double,
    var startDate: String,
    var endDate: String,
    var seatsLeft: Int,
    var address: String,
)