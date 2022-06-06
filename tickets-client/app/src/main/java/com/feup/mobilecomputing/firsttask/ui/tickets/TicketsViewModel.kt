package com.feup.mobilecomputing.firsttask.ui.tickets

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.feup.mobilecomputing.firsttask.models.TicketInternalType
import com.feup.mobilecomputing.firsttask.services.ticketSrv
import kotlin.coroutines.coroutineContext

class TicketsViewModel : ViewModel() {

    private val _ticketsList = MutableLiveData<ArrayList<TicketInternalType>>().apply {
        value = arrayListOf()
    }
    val ticketsList: MutableLiveData<ArrayList<TicketInternalType>> = _ticketsList

    fun setTicketList(mTicketsList: ArrayList<TicketInternalType>){
        _ticketsList.value = mTicketsList
    }
}