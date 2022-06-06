package com.feup.mobilecomputing.firsttask.ui.tickets.my_tickets

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.feup.mobilecomputing.firsttask.R
import com.feup.mobilecomputing.firsttask.models.TicketInternalType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson


class TicketsBottomsheetFragment : BottomSheetDialogFragment() {

    companion object {

        fun newInstance(bitmap: Bitmap, title: String) = TicketsBottomsheetFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
                putString("bitmap", Gson().toJson(bitmap))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottomsheet_fragment_qr_nfc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            findViewById<ImageView>(R.id.qr_bottom_sheet).setImageBitmap(Gson().fromJson(requireArguments().getString("bitmap"), Bitmap::class.java))
            findViewById<TextView>(R.id.title_ticket_bs).text = arguments?.getString("title")
        }
    }
}
