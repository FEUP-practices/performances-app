package com.feup.mobilecomputing.firsttask.ui.tickets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.feup.mobilecomputing.firsttask.R
import com.feup.mobilecomputing.firsttask.services.ticketSrv
import com.feup.mobilecomputing.firsttask.ui.home.HomeViewModel
import com.feup.mobilecomputing.firsttask.ui.tickets.my_tickets.TicketPagerAdapter
import com.feup.mobilecomputing.firsttask.ui.tickets.my_tickets.TicketsPageTransformer


class TicketsFragment : Fragment() {

    private val ticketsViewModel : TicketsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_tickets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mViewPager = view.findViewById<ViewPager2>(R.id.tickets_pager)
        ticketsViewModel.setTicketList(ticketSrv(requireContext()).fetchAllTickets())
        val mViewPagerAdapter = TicketPagerAdapter(childFragmentManager, lifecycle,ticketsViewModel.ticketsList.value ?: arrayListOf())
        mViewPager.adapter = mViewPagerAdapter
        mViewPager.currentItem = 1

        ticketsViewModel.ticketsList.observe(viewLifecycleOwner) {
            mViewPagerAdapter.setItems(it)
        }

        mViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (position == 0) {
                    mViewPager.setPageTransformer { _, _ -> }
                } else {
                    mViewPager.setPageTransformer(TicketsPageTransformer())
                }
            }

            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })

    }
}