package com.feup.mobilecomputing.tickets_validator.api

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.feup.mobilecomputing.tickets_validator.models.QRInfoType
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

interface CallbackAPI<T>{
    fun onSuccess(response: T)
    fun onError(errorMsg: String?)
}

class TicketsAPI(context: Context) {
    private val requestQueue = Volley.newRequestQueue(context)

    fun validateTicket(qrInfo: QRInfoType, callbackAPI: CallbackAPI<Boolean>){
        val jsonObject = JSONObject()
        jsonObject.accumulate("ticketId", qrInfo.ticketId)
        jsonObject.accumulate("userId", qrInfo.userId)
        jsonObject.accumulate("performanceId", qrInfo.performanceId)
        val stringRequest = JsonObjectRequest(
            Request.Method.POST, "https://enigmatic-springs-73519.herokuapp.com/tickets/validate", jsonObject,
            { response ->
                try {
                    callbackAPI.onSuccess(response.getBoolean("valid"))
                } catch (e: JSONException) {
                    e.printStackTrace()
                    callbackAPI.onError(e.message)
                }
            }) { e ->
            callbackAPI.onError(e.message)
            e.printStackTrace()
        }
        //setRestartPolicy(stringRequest)
        requestQueue.add(stringRequest)
    }
}