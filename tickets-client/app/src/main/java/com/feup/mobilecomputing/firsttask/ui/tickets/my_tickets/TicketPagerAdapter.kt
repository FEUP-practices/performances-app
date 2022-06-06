package com.feup.mobilecomputing.firsttask.ui.tickets.my_tickets

import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.feup.mobilecomputing.firsttask.models.TicketInternalType
import kotlin.collections.ArrayList

class TicketPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, ticketList: ArrayList<TicketInternalType>) : FragmentStateAdapter(fm, lifecycle){

    private var mTicketList = ticketList

    override fun getItemCount(): Int = mTicketList.size + 1

    override fun createFragment(position: Int): Fragment = if (position == 0) {
            MyPastTicketsFragment()
        } else {
            MyTicketFragment.newInstance(mTicketList[position - 1])
        }

    fun setItems(ticketList: ArrayList<TicketInternalType>){
        mTicketList = ArrayList(ticketList)
        notifyDataSetChanged()
    }
}