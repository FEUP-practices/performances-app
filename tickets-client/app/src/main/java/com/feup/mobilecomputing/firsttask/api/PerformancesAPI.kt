package com.feup.mobilecomputing.firsttask.api

import android.content.Context
import android.util.Log
import androidx.viewbinding.BuildConfig
import com.android.volley.Request
import com.android.volley.toolbox.*
import com.feup.mobilecomputing.firsttask.models.PerformanceType
import com.google.gson.Gson
import org.json.JSONException

interface CallbackAPI<T>{
    fun onSuccess(response: T)
    fun onError(errorMsg: String?)
}

class PerformancesAPI(context: Context) {

    // Instantiate the cache
    //val cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap

    // Set up the network to use HttpURLConnection as the HTTP client.
//    private val network = BasicNetwork(HurlStack())
//
//    // Instantiate the RequestQueue with the cache and network. Start the queue.
//    private val requestQueue = RequestQueue(null, network).apply {
//        start()
//    }

    private val requestQueue = Volley.newRequestQueue(context)
    private val gson = Gson()
    private val API_URL = " https://enigmatic-springs-73519.herokuapp.com/"

    fun fetchAllPerformances(numPage: Int, callbackAPI: CallbackAPI<Array<PerformanceType>>) {
        val stringRequest = JsonArrayRequest(
            Request.Method.GET, "${API_URL}performances?numPage=${numPage}", null,
            { response ->
                try {
                    val performancesList = gson.fromJson(response.toString(), Array<PerformanceType>::class.java)
                    callbackAPI.onSuccess(performancesList)
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

    fun fetchPerformance(id: String, callbackAPI: CallbackAPI<String>) {
        val stringRequest = JsonObjectRequest(
            Request.Method.GET, "${API_URL}performances/${id}", null,
            { response ->
                try {
                    callbackAPI.onSuccess(response.getString("description"))
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