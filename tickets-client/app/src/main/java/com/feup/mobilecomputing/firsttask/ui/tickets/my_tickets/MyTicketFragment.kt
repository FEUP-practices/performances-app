package com.feup.mobilecomputing.firsttask.ui.tickets.my_tickets

import android.content.Context
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.feup.mobilecomputing.firsttask.R
import com.feup.mobilecomputing.firsttask.config.Config
import com.feup.mobilecomputing.firsttask.db.TicketsDBAdapter
import com.feup.mobilecomputing.firsttask.models.QRInfoType
import com.feup.mobilecomputing.firsttask.models.TicketInternalType
import com.feup.mobilecomputing.firsttask.services.qrSrv
import com.feup.mobilecomputing.firsttask.services.ticketSrv
import com.feup.mobilecomputing.firsttask.services.userSrv
import com.feup.mobilecomputing.firsttask.ui.tickets.TicketsViewModel
import com.feup.mobilecomputing.firsttask.ui.utils.functions
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class MyTicketFragment : Fragment() {

    private lateinit var ticketInfo: TicketInternalType
    private val ticketsViewModel : TicketsViewModel by activityViewModels()

    companion object {

        fun newInstance(ticket: TicketInternalType) = MyTicketFragment().apply {
            arguments = Bundle().apply {
                putString("ticket", Gson().toJson(ticket))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ticketInfo = Gson().fromJson(arguments?.getString("ticket"), TicketInternalType::class.java)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View = inflater.inflate(R.layout.ticket_visual, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val qrNFCLayout: LinearLayout = view.findViewById(R.id.ticket_visual)
        registerForContextMenu(qrNFCLayout)
        view.apply {
            runBlocking {
                val qrBitmap = qrSrv().createQR(
                    QRInfoType(
                        ticketId = ticketInfo._id,
                        performanceId = ticketInfo.performanceId,
                        userId = userSrv(requireActivity().getSharedPreferences(Config.SP_NAME, Context.MODE_PRIVATE)).getUserId(),

                    )
                )
                if (qrBitmap == null) {
                    Toast.makeText(context, "Error generating QR code", Toast.LENGTH_LONG).show()
                } else {
                    findViewById<ImageView>(R.id.qr_visual).setImageBitmap(qrBitmap)
                    qrNFCLayout.setOnClickListener {
                        val bottomSheetQrNFC = TicketsBottomsheetFragment.newInstance(qrBitmap, ticketInfo.name)
                            bottomSheetQrNFC.show(
                                requireActivity().supportFragmentManager,
                                "QR_NFC_MODAL"
                            )
                    }
                }
            }
            val startDate = ticketInfo.startDate
            val endDate = ticketInfo.endDate
            findViewById<TextView>(R.id.title_visual).text = ticketInfo.name
            findViewById<TextView>(R.id.date_visual).text =
                "${functions.getFormattedDate(startDate)}"
            findViewById<TextView>(R.id.start_time_visual).text =
                "${startDate.hours}:${startDate.minutes}"
            findViewById<TextView>(R.id.end_time_visual).text =
                "${endDate.hours}:${endDate.minutes}"
            findViewById<TextView>(R.id.price_visual).text = "${ticketInfo.price * ticketInfo.numberBought}€"
            findViewById<TextView>(R.id.seats_bought_visual).text =
                "x${ticketInfo.numberBought}"
            findViewById<TextView>(R.id.seat_number_visual).text =
                "Seat number: ${ticketInfo.seatNumber}"
            findViewById<TextView>(R.id.address_visual).text =
                "${ticketInfo.address}"
            val buttonDelete = findViewById<ImageButton>(R.id.ticket_delete)

            if (endDate.after(Calendar.getInstance().time)) {
                buttonDelete.visibility = View.GONE
            } else {
                buttonDelete.setOnClickListener {
                    ticketSrv(requireContext()).deleteTicket(ticketInfo._id)
                    ticketsViewModel.setTicketList(ticketSrv(requireContext()).fetchAllTickets())
                    Toast.makeText(requireContext(), "Ticket deleted correctly", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}