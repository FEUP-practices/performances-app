package com.feup.mobilecomputing.firsttask.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.feup.mobilecomputing.firsttask.models.SignatureType
import com.feup.mobilecomputing.firsttask.models.TicketType
import com.feup.mobilecomputing.firsttask.models.UserType
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class UsersAPI(context: Context) {
    private val requestQueue = Volley.newRequestQueue(context)
    private val API_URL = " https://enigmatic-springs-73519.herokuapp.com/"

    fun registerUser(user: UserType, signature: SignatureType, pubKey: String , callbackAPI: CallbackAPI<String>){
        val jsonObject = JSONObject()
        jsonObject.accumulate("nif", user.nif)
        jsonObject.accumulate("name", user.name)
        jsonObject.accumulate("cardNumber", user.cardNumber)
        jsonObject.accumulate("cardCVV", user.cardCVV)
        jsonObject.accumulate("cardType", user.cardType)
        jsonObject.accumulate("cardValidity", user.cardValidity)
        jsonObject.accumulate("signature", signature.signature)
        jsonObject.accumulate("challenge", signature.challenge)
        jsonObject.accumulate("pubKey", pubKey)
        val stringRequest = JsonObjectRequest(
            Request.Method.POST, "${API_URL}users", jsonObject,
            { response ->
                try {
                    callbackAPI.onSuccess(response.getString("userId"))
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