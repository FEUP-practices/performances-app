package com.feup.mobilecomputing.firsttask.services

import android.content.Context
import android.graphics.Bitmap
import com.feup.mobilecomputing.firsttask.db.TicketsDBAdapter
import com.feup.mobilecomputing.firsttask.models.QRInfoType
import com.feup.mobilecomputing.firsttask.models.TicketInternalType
import com.feup.mobilecomputing.firsttask.models.TicketType
import com.feup.mobilecomputing.firsttask.ui.utils.functions
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class ticketSrv(private val context: Context) {
    fun saveTicket(response: TicketType): Boolean {
        val dbAdapter = TicketsDBAdapter(context).open()
        val res = dbAdapter.saveTicket(
            TicketInternalType(
            _id = response.id,
            seatNumber = response.seatNumber,
            used = response.used,
            performanceId = response.performance.id,
            name = response.performance.name,
            price = response.performance.price,
            startDate = functions.dateFormat.parse(response.performance.startDate)!!,
            endDate = functions.dateFormat.parse(response.performance.endDate)!!,
            address = response.performance.address,
                numberBought = response.numberBought
        )
        ) > 0
        dbAdapter.close()
        return res
    }

    fun fetchAllTickets(): ArrayList<TicketInternalType> {
        val dbAdapter = TicketsDBAdapter(context).open()
        val res = dbAdapter.findAllTickets()
        dbAdapter.close()
        return res
    }

    fun deleteTicket(id: String): Boolean {
        val dbAdapter = TicketsDBAdapter(context).open()
        val res = dbAdapter.deleteTicket(id) > 0
        dbAdapter.close()
        return res
    }
}