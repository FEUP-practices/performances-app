package com.feup.mobilecomputing.firsttask.ui.utils

import java.text.SimpleDateFormat
import java.util.*

class functions {
    companion object {
        var dateFormat: SimpleDateFormat = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:", Locale.getDefault()
        )

        private val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        fun getFormattedDate(date: Date): String{
            return format.format(date)
        }
    }
}