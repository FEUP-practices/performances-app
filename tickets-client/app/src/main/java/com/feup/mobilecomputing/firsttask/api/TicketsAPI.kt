package com.feup.mobilecomputing.firsttask.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.feup.mobilecomputing.firsttask.models.BuyPayloadType
import com.feup.mobilecomputing.firsttask.models.PerformanceType
import com.feup.mobilecomputing.firsttask.models.SignatureType
import com.feup.mobilecomputing.firsttask.models.TicketType
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class TicketsAPI(context: Context) {
    private val requestQueue = Volley.newRequestQueue(context)
    private val gson = Gson()
    private val API_URL = " https://enigmatic-springs-73519.herokuapp.com/"

    fun buyTicket(payload: BuyPayloadType, signature: SignatureType, callbackAPI: CallbackAPI<TicketType>){
        val jsonObject = JSONObject()
        jsonObject.accumulate("numberBought", payload.numberBought)
        jsonObject.accumulate("userId", payload.userId)
        jsonObject.accumulate("performanceId", payload.performanceId)
        jsonObject.accumulate("seatNumber", payload.seatNumber)
        jsonObject.accumulate("signature", signature.signature)
        jsonObject.accumulate("challenge", signature.challenge)
        val stringRequest = JsonObjectRequest(
            Request.Method.POST, "${API_URL}tickets", jsonObject,
            { response ->
                try {
                    callbackAPI.onSuccess(gson.fromJson(response.toString(), TicketType::class.java))
                } catch (e: JSONException) {
                    e.printStackTrace()
                    callbackAPI.onError(e.message)
                }
            }) { e ->
            callbackAPI.onError(e.message)
            e.printStackTrace()
        }
        requestQueue.add(stringRequest)
    }
}