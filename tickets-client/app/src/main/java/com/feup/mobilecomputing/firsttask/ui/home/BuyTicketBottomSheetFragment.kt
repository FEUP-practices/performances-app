package com.feup.mobilecomputing.firsttask.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.feup.mobilecomputing.firsttask.MainActivity
import com.feup.mobilecomputing.firsttask.R
import com.feup.mobilecomputing.firsttask.api.CallbackAPI
import com.feup.mobilecomputing.firsttask.api.TicketsAPI
import com.feup.mobilecomputing.firsttask.config.Config
import com.feup.mobilecomputing.firsttask.db.TicketsDBAdapter
import com.feup.mobilecomputing.firsttask.middleware.SecureStore
import com.feup.mobilecomputing.firsttask.models.BuyPayloadType
import com.feup.mobilecomputing.firsttask.models.SignatureType
import com.feup.mobilecomputing.firsttask.models.TicketType
import com.feup.mobilecomputing.firsttask.services.ticketSrv
import com.feup.mobilecomputing.firsttask.services.userSrv
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar


class BuyTicketBottomSheetFragment : BottomSheetDialogFragment() {

    private val seatNumber = (1..100).random()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottomsheet_fragment_buy_tickets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val numBought = requireArguments().getInt("numBought")
        with(view) {
            findViewById<TextView>(R.id.title_buy_bs).text = requireArguments().getString("title")
            findViewById<TextView>(R.id.num_bought_buy_bs).text = "x$numBought"
            findViewById<TextView>(R.id.seat_number_buy_bs).text = "Seat number: $seatNumber"
            findViewById<TextView>(R.id.price_buy_bs).text = "${requireArguments().getString("price")} €"
            findViewById<Button>(R.id.button_buy_bs).setOnClickListener {
                buyTicket(numBought)
            }
        }
    }

    private fun buyTicket(numBought: Int){
        val ta = TicketsAPI(requireContext())
        val challenge = SecureStore().genRandomChallenge()
        ta.buyTicket(
            BuyPayloadType(numBought, seatNumber, userSrv(requireActivity().getSharedPreferences(Config.SP_NAME, Context.MODE_PRIVATE)).getUserId(), requireArguments().getString("performanceId")!!), signature = SignatureType(
            challenge = Base64.encodeToString(challenge, Base64.DEFAULT),
            signature = SecureStore().signContent(challenge)!!
        ),object: CallbackAPI<TicketType> {
            override fun onSuccess(response: TicketType) {
                if (!ticketSrv(requireContext()).saveTicket(response)) {
                    onError("Sqlite insert error")
                } else {
                    Toast.makeText(activity?.applicationContext, "Success!", Toast.LENGTH_LONG).show()
                    activity?.onBackPressed()
                }
            }
            override fun onError(errorMsg: String?) {
                Toast.makeText(activity?.applicationContext, "Something went wrong. Ticket not bought. Cause: $errorMsg", Toast.LENGTH_LONG).show()
                Log.e("error", errorMsg ?: "Errorrrr")
            }
        })
    }

}
