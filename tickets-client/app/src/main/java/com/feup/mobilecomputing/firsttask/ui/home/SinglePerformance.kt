package com.feup.mobilecomputing.firsttask.ui.home

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.feup.mobilecomputing.firsttask.R
import com.feup.mobilecomputing.firsttask.api.CallbackAPI
import com.feup.mobilecomputing.firsttask.api.PerformancesAPI
import com.feup.mobilecomputing.firsttask.db.TicketsDBAdapter
import com.feup.mobilecomputing.firsttask.models.PerformanceType
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class SinglePerformance : AppCompatActivity() {

    private val MAX_TICKETS_TO_BUY = 5

    private var num = 0
    private lateinit var numberPicker: NumberPicker
    private lateinit var priceText: TextView
    private lateinit var swicth: SwitchMaterial
    private var rootView: ConstraintLayout? = null
    private lateinit var buttonBuyTickets: Button
    private lateinit var performance: PerformanceType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.single_performance)
        performance = Gson().fromJson(intent.extras?.getString("performance"), PerformanceType::class.java)
        num = performance.seatsLeft
        loadInfo()

        buttonBuyTickets.setOnClickListener {
            if (rootView != null && !swicth.isChecked){
                Snackbar.make(rootView!!, "You must accept the terms and conditions", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val buyBottomsheetFragment = BuyTicketBottomSheetFragment()
            buyBottomsheetFragment.arguments = Bundle().apply {
                putInt("numBought", numberPicker.value)
                putString("title", performance.name)
                putString("price", priceText.text.toString())
                putString("performanceId", performance.id)
            }
            buyBottomsheetFragment.show(
                supportFragmentManager,
                "BUY_BOTTOM_SHEET"
            )
        }
    }
    private fun loadInfo() {
        val pa = PerformancesAPI(applicationContext)
        pa.fetchPerformance(performance.id, object: CallbackAPI<String> {
            override fun onSuccess(response: String) {
                findViewById<TextView>(R.id.description).text = response
            }
            override fun onError(errorMsg: String?) {}
        })
        title = performance.name
        Picasso.with(applicationContext).load(performance.imageURI).into(findViewById<ImageView>(R.id.performace_card_image2))
        findViewById<TextView>(R.id.title_visual).text = performance.name
        findViewById<TextView>(R.id.date_watch).text = performance.startDate.split(" ",)[0]
        findViewById<TextView>(R.id.start_time_watch).text = performance.startDate.split(" ")[1].subSequence(0,5)
        findViewById<TextView>(R.id.end_time_watch).text = performance.endDate.split(" ")[1].subSequence(0,5)
        findViewById<TextView>(R.id.address_watch).text = performance.address
        priceText = findViewById(R.id.price_watch)
        priceText.text = performance.price.toString()
        swicth = findViewById(R.id.switch_watch)
        numberPicker = findViewById(R.id.np)
        if (num < 0) num = 1
        numberPicker.apply {
            minValue = 1
            maxValue = if (num < MAX_TICKETS_TO_BUY) num else MAX_TICKETS_TO_BUY
            setOnValueChangedListener { np, _, _ -> priceText.text = (performance.price * np.value).toString() }
        }
        buttonBuyTickets = findViewById(R.id.button_watch)
        rootView = findViewById(R.id.container_watch)

        findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            finish()
        }

    }

}